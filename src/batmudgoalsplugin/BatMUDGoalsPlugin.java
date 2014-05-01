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
			.compile("\\|\\s+(\\d+)%\\s+=\\s+(\\d+)");
	private Pattern skillstatuspattern = Pattern
			.compile("\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+|\\(n/a\\))\\s+\\|\\s+");
	private Pattern goalcommandpattern = Pattern.compile("goal\\s*(.+)*",
			Pattern.CASE_INSENSITIVE);
	private Pattern exppattern = Pattern
			.compile("Exp: (\\d+) Money: (\\d+)\\.?(\\d*) Bank: (\\d+)\\.?(\\d*) Exp pool: (\\d+)\\.?(\\d*)\\s+");
	private Pattern trainpattern = Pattern
			.compile("You now have '([^']+)' at (\\d+)% without special bonuses.\\s+");

	private String latestSkillName;

	private BatMUDGoalsPluginData data = new BatMUDGoalsPluginData(
			new HashMap<String, Map<String, String>>(),
			new HashMap<String, SkillStatus>());

	/*
	 * Catches 'goal' command. (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger#trigger
	 * (java.lang.String)
	 */
	@Override
	public String trigger(String input) {
		Matcher m = goalcommandpattern.matcher(input);
		if (m.matches()) {
			String goalParameter = m.group(1);
			// If a skill is given as goal parameter, normalize skill name and
			// set goal
			if (goalParameter != null) {
				data.goalSkill = normalizeSkillName(goalParameter);
				if (!data.skills.containsKey(data.goalSkill)) {
					printMessage("%s not in library", data.goalSkill);
				} else {
					printMessage("Next goal is %s", data.goalSkill);
				}
			} else {
				for (String skillName : data.skills.keySet())
					printMessage("%s%s", skillName,
							skillName.equals(data.goalSkill) ? " (*)" : "");
			}
			return ""; // Stop command from being processed by client
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
		catchTrainedSkillOutput(input);
		catchExpCommandOutput(input);
		return input; // return input to be processed by the client
	}

	/**
	 * Exp command output is something like this: <br>
	 * Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0<br>
	 * Takes exp amount, outputs goal skill, needed exp and amount missing from
	 * next percent
	 * 
	 * @param input
	 */
	private void catchExpCommandOutput(ParsedResult input) {
		Matcher m;
		m = exppattern.matcher(input.getOriginalText());
		if (m.matches()) {
			SkillStatus skillStatus = data.skillStatuses.get(data.goalSkill);
			if (skillStatus.cur >= skillStatus.max) {
				if (skillStatus.cur == 100) {
					printMessage("Goal %s: full", data.goalSkill);
				} else {
					printMessage("Goal %s: needs level", data.goalSkill);
				}
			} else {
				int neededExp = Integer.parseInt(data.skills
						.get(data.goalSkill).get(
								Integer.toString(skillStatus.cur + 1)));
				int currentExp = Integer.parseInt(m.group(1));
				if (currentExp < neededExp) {
					printMessage("Goal %s: %d You need: %d", data.goalSkill,
							neededExp, neededExp - currentExp);
				} else {
					printMessage("Goal %s: %d You have enough to advance",
							data.goalSkill, neededExp);
				}
			}
		}
	}

	private void catchTrainedSkillOutput(ParsedResult input) {
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
	}

	private void printMessage(String message) {
		getClientGUI().printText("generic", String.format("%s\n", message));
	}

	private void printMessage(String format, Object... args) {
		printMessage(String.format(format, args));
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
			printMessage(e.toString());
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
			printMessage(e.toString());
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
