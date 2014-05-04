package batmudgoalsplugin;

import java.util.Collection;
import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

/**
 * Catches goal command without parameters and prints list of possible goal
 * skills
 */
class GoalCommandWithoutParametersProcessor extends AbstractCommandProcessor {
	public GoalCommandWithoutParametersProcessor(BatClientPlugin plugin,
			BatMUDGoalsPluginData data) {
		super("\\s*goal\\s*", plugin, data);
	}

	@Override
	protected boolean process(Matcher m) {

		Collection<String> storedSkills = data.getStoredSkills();
		if (storedSkills.isEmpty()) {
			printMessage("No data.");
		} else {

			for (String skillName : storedSkills)
				printMessage("%s%s", skillName,
						data.isGoalSkill(skillName) ? " (*)" : "");
		}
		return true;
	}
}