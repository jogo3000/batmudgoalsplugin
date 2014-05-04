package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

/**
 * Catches parameterised goal command, e.g. 'goal attack' and sets the goal if
 * possible
 */
class GoalCommandProcessor extends AbstractCommandProcessor {

	public GoalCommandProcessor(BatClientPlugin plugin, BatMUDGoalsPluginData data) {
		super("goal\\s*(.+)\\s*", plugin, data);
	}

	@Override
	protected boolean process(Matcher m) {
		String goalParameter = m.group(1);
		// If a skill is given as goal parameter, normalize skill name and
		// set goal
		data.setGoalSkill(normalizeSkillName(goalParameter));
		if (!data.isSkillInCostLibrary(data.getGoalSkill())) {
			printMessage("%s not in library", data.getGoalSkill());
		} else {
			printMessage("Next goal: %s", data.getGoalSkill());
		}
		return true; // Stop command from being processed by client
	}
}