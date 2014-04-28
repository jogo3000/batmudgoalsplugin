package batmudgoalsplugin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private Map<String, Map<String, String>> skills = new HashMap<String, Map<String, String>>();
	private Map<String, SkillStatus> skillStatuses = new HashMap<String, SkillStatus>();

	public static class SkillStatus {
		public SkillStatus(int cur, int max) {
			this.cur = cur;
			this.max = max;
		}

		public SkillStatus(String string, String string2) {
			this(Integer.parseInt(string), Integer.parseInt(string2));
		}

		public Integer cur;
		public Integer max;
	}

	private String latestSkillName;
	private String goalSkill;
	private String goalPercent;

	@Override
	public String trigger(String input) {
		Matcher m = goalcommandpattern.matcher(input);
		if (m.matches()) {
			String goalParameter = m.group(1);
			if (goalParameter != null) {
				StringBuilder sb = new StringBuilder();
				for (String s : goalParameter.split("\\s")) {
					sb.append(s);
					sb.append(" ");
				}
				goalSkill = sb.toString().trim();
				if (!skills.containsKey(goalSkill)) {
					getClientGUI().printText("generic",
							goalSkill + " not in library\n");
				} else {
					getClientGUI().printText("generic",
							"Next goal is " + goalSkill + "\n");
				}
			} else {
				for (String skillName : skills.keySet())
					getClientGUI().printText("generic", skillName + "\n");
			}
			return "";
		}
		m = exppattern.matcher(input);
		if (m.matches()) {
			goalPercent = Integer
					.toString(skillStatuses.get(goalSkill).cur + 1);
			getClientGUI().printText(
					"generic",
					"Goal " + goalSkill + ": "
							+ skills.get(goalSkill).get(goalPercent) + "\n");
			return input;
		}
		return null;
	}

	@Override
	public ParsedResult trigger(ParsedResult input) {
		Matcher skillmatcher = skillpattern.matcher(input.getOriginalText());
		if (skillmatcher.matches()) {
			latestSkillName = skillmatcher.group(1).toLowerCase().trim();
			if (!skills.containsKey(latestSkillName)) {
				skills.put(latestSkillName, new HashMap<String, String>());
			}
		}
		Matcher percentcostmatcher = percentcostpattern.matcher(input
				.getOriginalText());
		while (percentcostmatcher.find()) {
			Map<String, String> skilltable = skills.get(latestSkillName);
			skilltable.put(percentcostmatcher.group(1),
					percentcostmatcher.group(2));
		}

		Matcher skillstatusmatcher = skillstatuspattern.matcher(input
				.getOriginalText());
		if (skillstatusmatcher.matches()) {
			String skillName = skillstatusmatcher.group(1).trim().toLowerCase();
			String cur = skillstatusmatcher.group(2);
			String max = skillstatusmatcher.group(4);

			skillStatuses.put(skillName, new SkillStatus(Integer.parseInt(cur),
					Integer.parseInt(max)));
		}
		return input;
	}

	@Override
	public String getName() {
		return "BatMUDGoalsPlugin";
	}

	@Override
	public void loadPlugin() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new FileReader(getPersistenceFileName()));
			assertSectionIsNext(reader, "[Goal]");
			goalSkill = reader.readLine().replace("skill=", "");
			goalPercent = reader.readLine().replace("percent=", "");
			reader.readLine(); // empty row

			// Read skills table
			assertSectionIsNext(reader, "[skills]");
			String line;
			while ((line = reader.readLine()) != "") {
				String[] parts = line.split(",");
				if (!skills.containsKey(parts[0])) {
					skills.put(parts[0], new HashMap<String, String>());
				}
				skills.get(parts[0]).put(parts[1], parts[2]);
			}

			assertSectionIsNext(reader, "skillStatuses");
			while ((line = reader.readLine()) != "") {
				String[] parts = line.split(",");
				skillStatuses
						.put(parts[0], new SkillStatus(parts[1], parts[2]));
			}

			reader.close();
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void assertSectionIsNext(BufferedReader reader, String sectionName)
			throws IOException {
		if (!sectionName.equalsIgnoreCase(reader.readLine())) {
			throw new IOException(sectionName + " section is missing");
		}
	}

	@Override
	public void clientExit() {
		try {
			PrintWriter writer = new PrintWriter(getPersistenceFileName());
			writer.println("[Goal]");
			writer.println("skill=" + goalSkill);
			writer.println("percent=" + goalPercent);

			writer.println();
			writer.println("[skills]");
			for (Entry<String, Map<String, String>> key : skills.entrySet()) {
				for (Entry<String, String> value : key.getValue().entrySet()) {
					writer.println(key.getKey() + "," + value.getKey() + ","
							+ value.getValue());
				}
			}
			writer.println();
			writer.print("[skillStatuses]");
			for (Entry<String, SkillStatus> skillstatus : skillStatuses
					.entrySet()) {
				writer.println(skillstatus.getKey() + ","
						+ skillstatus.getValue().cur + ","
						+ skillstatus.getValue().max);
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getPersistenceFileName() {
		return getClientGUI().getBaseDirectory()
				+ "/conf/batmudgoalsplugin/BatMUDGoalsInfo.file";
	}
}
