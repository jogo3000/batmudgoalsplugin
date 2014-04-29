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

public class BatMUDGoalsPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger, BatClientPluginTrigger,
		BatClientPluginUtil {

	private Pattern skillpattern = Pattern.compile(
			"\\|\\s+Cost\\s+of\\s+training\\s+([^\\|]+)\\s+\\|\\s+",
			Pattern.CASE_INSENSITIVE);
	private Pattern percentcostpattern = Pattern
			.compile("(\\d+)%\\s+=\\s+(\\d+)");
	private Pattern skillstatuspattern = Pattern
			.compile("\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+");
	private Pattern goalcommandpattern = Pattern.compile("goal\\s*(.+)*",
			Pattern.CASE_INSENSITIVE);
	private Pattern exppattern = Pattern.compile("\\s*exp\\s*");

	private String latestSkillName;

	private BatMUDGoalsPluginData data = new BatMUDGoalsPluginData(
			new HashMap<String, Map<String, String>>(),
			new HashMap<String, SkillStatus>());

	@Override
	public String trigger(String input) {
		// Handle goal command
		Matcher m = goalcommandpattern.matcher(input);
		if (m.matches()) {
			String goalParameter = m.group(1);
			if (goalParameter != null) {
				StringBuilder sb = new StringBuilder();
				for (String s : goalParameter.split("\\s")) {
					sb.append(s);
					sb.append(" ");
				}
				data.goalSkill = sb.toString().trim();
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
			return "";
		}

		// Handle exp command
		m = exppattern.matcher(input);
		if (m.matches()) {
			data.goalPercent = Integer.toString(data.skillStatuses
					.get(data.goalSkill).cur + 1);
			getClientGUI().printText(
					"generic",
					"Goal "
							+ data.goalSkill
							+ ": "
							+ data.skills.get(data.goalSkill).get(
									data.goalPercent) + "\n");
			return input;
		}
		return null;
	}

	@Override
	public ParsedResult trigger(ParsedResult input) {
		Matcher skillmatcher = skillpattern.matcher(input.getOriginalText());
		if (skillmatcher.matches()) {
			latestSkillName = skillmatcher.group(1).toLowerCase().trim();
			if (!data.skills.containsKey(latestSkillName)) {
				data.skills.put(latestSkillName, new HashMap<String, String>());
			}
		}
		Matcher percentcostmatcher = percentcostpattern.matcher(input
				.getOriginalText());
		while (percentcostmatcher.find()) {
			Map<String, String> skilltable = data.skills.get(latestSkillName);
			skilltable.put(percentcostmatcher.group(1),
					percentcostmatcher.group(2));
		}

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
		return input;
	}

	@Override
	public String getName() {
		return "BatMUDGoalsPlugin";
	}

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
}
