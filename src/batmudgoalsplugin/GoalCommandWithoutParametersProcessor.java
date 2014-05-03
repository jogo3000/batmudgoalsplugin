package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.ClientGUI;

/**
 * Catches goal command without parameters and prints list of possible goal
 * skills
 */
class GoalCommandWithoutParametersProcessor extends
		AbstractCommandProcessor {
	public GoalCommandWithoutParametersProcessor(ClientGUI clientGUI, BatMUDGoalsPluginData data) {
		super("\\s*goal\\s*", clientGUI, data);
	}

	@Override
	protected boolean process(Matcher m) {
		for (String skillName : data.getStoredSkills())
			printMessage("%s%s", skillName,
					data.isGoalSkill(skillName) ? " (*)" : "");
		return true;
	}
}