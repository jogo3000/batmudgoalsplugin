package batmudgoalsplugin;

import java.util.Collection;
import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Catches goal command without parameters and prints list of possible goal
 * skills
 */
class GoalCommandWithoutParametersProcessor extends AbstractCommandProcessor {
    public GoalCommandWithoutParametersProcessor(ClientGUIModel guiModel,
            BatMUDGoalsPluginData data) {
        super("\\s*goal\\s*", guiModel, data);
    }

    @Override
    protected boolean process(Matcher m) {

        Collection<String> storedSkills = data.getStoredSkills();
        if (storedSkills.isEmpty()) {
            printMessage("No data.");
        } else {
            storedSkills
                    .forEach(skillName -> printMessage("%s%s", skillName, data.isGoalSkill(skillName) ? " (*)" : ""));

        }
        return true;
    }
}