package batmudgoalsplugin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.ParsedResult;

public class GoalCommandPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger, BatClientPluginTrigger {

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
		// No action for now
	}

}
