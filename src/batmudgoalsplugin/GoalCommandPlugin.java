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
			"\\|\\s+Cost\\s+of\\s+training\\s+([^\\|]+)\\s+\\|",
			Pattern.CASE_INSENSITIVE);
	private Pattern percentcostpattern = Pattern
			.compile("(\\d+)%\\s+=\\s+(\\d+)");
	private Pattern goalcommandpattern = Pattern.compile("goal\\s+(.+)",
			Pattern.CASE_INSENSITIVE);
	private Pattern exppattern = Pattern.compile("\\s*exp\\s*");

	private Map<String, Map<String, String>> skills = new HashMap<String, Map<String, String>>();
	private String latestSkillName;
	private String goalSkill;

	@Override
	public String trigger(String input) {
		Matcher m = goalcommandpattern.matcher(input);
		if (m.matches()) {
			StringBuilder sb = new StringBuilder();
			for (String s : m.group(1).split("\\s")) {
				sb.append(s);
				sb.append(" ");
			}
			goalSkill = sb.toString().trim();
			if (!skills.containsKey(goalSkill)) {
				getClientGUI().printText("generic",
						goalSkill + " not in library");
			} else {
				getClientGUI()
						.printText("generic", "Next goal is " + goalSkill);
			}
		}
		m = exppattern.matcher(input);
		if (m.matches()) {
			getClientGUI().printText("generic",
					goalSkill + ": " + skills.get(goalSkill).get("1"));
		}
		return input;
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
