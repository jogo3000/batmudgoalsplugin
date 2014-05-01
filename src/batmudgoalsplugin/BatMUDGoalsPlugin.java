package batmudgoalsplugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

/**
 * Plugin for BatClient. Player can set a goal of improving a skill in her
 * guild. Experience needed to reach the next percent is then shown upon 'exp'
 * command.
 * 
 * @author Jogo
 */
public class BatMUDGoalsPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger, BatClientPluginTrigger,
		BatClientPluginUtil {

	private Pattern skillpattern = Pattern.compile(
			"\\|\\s+Cost\\s+of\\s+training\\s+([^\\|]+)\\s+\\|\\s+",
			Pattern.CASE_INSENSITIVE);
	private Pattern percentcostpattern = Pattern
			.compile("(\\d+)%\\s+=\\s+(\\d+)");
	private Pattern skillstatuspattern = Pattern
			.compile("\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+|\\(n/a\\))\\s+\\|\\s+");
	private Pattern goalcommandpattern = Pattern.compile("goal\\s*(.+)*",
			Pattern.CASE_INSENSITIVE);
	private Pattern exppattern = Pattern.compile("\\s*exp\\s*");
	private Pattern trainpattern = Pattern
			.compile("You now have '([^']+)' at (\\d+)% without special bonuses.\n");

	private String latestSkillName;

	private BatMUDGoalsPluginData data = new BatMUDGoalsPluginData(
			new HashMap<String, Map<String, String>>(),
			new HashMap<String, SkillStatus>());

	/*
	 * Catches 'goal' and 'exp' commands. (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger#trigger
	 * (java.lang.String)
	 */
	@Override
	public String trigger(String input) {
		// Handle goal command
		Matcher m = goalcommandpattern.matcher(input);
		if (m.matches()) {
			String goalParameter = m.group(1);
			// If a skill is given as goal parameter, normalize skill name and
			// set goal
			if (goalParameter != null) {
				data.goalSkill = normalizeSkillName(goalParameter);
				if (!data.skills.containsKey(data.goalSkill)) {
					getClientGUI().printText("generic",
							data.goalSkill + " not in library\n");
				} else {
					getClientGUI().printText("generic",
							"Next goal is " + data.goalSkill + "\n");
				}
			} else {
				for (String skillName : data.skills.keySet())
					getClientGUI().printText("generic", skillName + "\n");
			}
			return ""; // Stop command from being processed by client
		}

		// Handle exp command
		m = exppattern.matcher(input);
		if (m.matches()) {
			SkillStatus skillStatus = data.skillStatuses.get(data.goalSkill);
			String goalString = "Goal " + data.goalSkill + ": ";
			if (skillStatus.max <= skillStatus.cur) {
				if (skillStatus.cur == 100) {
					getClientGUI().printText("generic", goalString + "full\n");
				} else {
					getClientGUI().printText("generic",
							goalString + "needs level\n");
				}
			} else {
				data.goalPercent = Integer.toString(skillStatus.cur + 1);
				getClientGUI().printText(
						"generic",
						goalString
								+ data.skills.get(data.goalSkill).get(
										data.goalPercent) + "\n");
			}
			return input; // Let client process the command after we're done
		}
		return null;
	}

	/**
	 * Removes extra whitespaces and puts to lowercase
	 * 
	 * @param originalSkillName
	 * @return normalized skill name
	 */
	private String normalizeSkillName(String originalSkillName) {
		StringBuilder sb = new StringBuilder();
		for (String s : originalSkillName.split("\\s")) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString().trim().toLowerCase();
	}

	/*
	 * Catch output from 'cost train skill' and 'train' commands (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginTrigger#trigger(com
	 * .mythicscape.batclient.interfaces.ParsedResult)
	 */
	@Override
	public ParsedResult trigger(ParsedResult input) {
		catchSkillName(input);
		catchPercentCost(input);
		catchTrainCommandOutput(input);

		Matcher m = trainpattern.matcher(input.getOriginalText());
		if (m.matches()) {
			String skillName = m.group(1).trim().toLowerCase();
			String percent = m.group(2).trim();
			if (!data.skillStatuses.containsKey(skillName)) {
				data.skillStatuses.put(skillName, new SkillStatus());
			}
			SkillStatus skillStatus = data.skillStatuses.get(skillName);
			skillStatus.cur = Integer.parseInt(percent);
		}
		return input;
	}

	private void catchTrainCommandOutput(ParsedResult input) {
		Matcher skillstatusmatcher = skillstatuspattern.matcher(input
				.getOriginalText());
		if (skillstatusmatcher.matches()) {
			String skillName = skillstatusmatcher.group(1).trim().toLowerCase();
			String cur = skillstatusmatcher.group(2);
			String max = skillstatusmatcher.group(4);

			data.skillStatuses.put(
					skillName,
					new SkillStatus(Integer.parseInt(cur), Integer
							.parseInt(max)));
		}
	}

	private void catchPercentCost(ParsedResult input) {
		Matcher percentcostmatcher = percentcostpattern.matcher(input
				.getOriginalText());
		while (percentcostmatcher.find()) {
			Map<String, String> skilltable = data.skills.get(latestSkillName);
			skilltable.put(percentcostmatcher.group(1),
					percentcostmatcher.group(2));
		}
	}

	private void catchSkillName(ParsedResult input) {
		Matcher skillmatcher = skillpattern.matcher(input.getOriginalText());
		if (skillmatcher.matches()) {
			latestSkillName = skillmatcher.group(1).toLowerCase().trim();
			if (!data.skills.containsKey(latestSkillName)) {
				data.skills.put(latestSkillName, new HashMap<String, String>());
			}
		}
	}

	@Override
	public String getName() {
		return "BatMUDGoalsPlugin";
	}

	/*
	 * Load state from persistent storage (non-Javadoc)
	 * 
	 * @see com.mythicscape.batclient.interfaces.BatClientPlugin#loadPlugin()
	 */
	@Override
	public void loadPlugin() {
		try {
			data = (BatMUDGoalsPluginData) generateJAXBContext()
					.createUnmarshaller().unmarshal(createPersistenceFile());
		} catch (Exception e) {
			getClientGUI().printText("generic", e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * Persist current goal and stored skill information (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginUtil#clientExit()
	 */
	@Override
	public void clientExit() {
		try {
			generateJAXBContext().createMarshaller().marshal(data,
					createPersistenceFile());
		} catch (JAXBException | IOException e) {
			getClientGUI().printText("generic", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * If file exists opens the file, otherwise a new one is created
	 * 
	 * @return {@link File} used to store plugin's state
	 * @throws IOException
	 */
	private File createPersistenceFile() throws IOException {
		File file = new File(getClientGUI().getBaseDirectory()
				+ "/conf/batmudgoalsplugin/BatMUDGoalsInfo.xml");
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	private JAXBContext generateJAXBContext() throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class);
		return ctx;
	}
}
