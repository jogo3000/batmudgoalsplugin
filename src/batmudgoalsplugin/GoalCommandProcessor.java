package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

/**
 * Catches parameterised goal command, e.g. 'goal attack' and sets the goal if
 * possible
 */
class GoalCommandProcessor extends AbstractCommandProcessor {

	public GoalCommandProcessor(BatClientPlugin plugin,
			BatMUDGoalsPluginData data) {
		super("goal\\s*(.+)\\s*", plugin, data);
	}

	@Override
	protected boolean process(Matcher m) {
		// If a skill is given as goal parameter, normalize skill name and
		// set goal
		String skillName = normalizeSkillName(m.group(1));
		if (!data.isSkillInCostLibrary(skillName)) {
			printMessage("%s not in library", skillName);
		} else {
			data.setGoalSkill(skillName);
			printMessage("Next goal: %s", skillName);
		}
		return true; // Stop command from being processed by client
	}
}