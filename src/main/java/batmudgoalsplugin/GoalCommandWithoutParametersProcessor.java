package batmudgoalsplugin;

import java.util.Collection;
import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Catches goal command without parameters and prints list of possible goal
 * skills
 */
class GoalCommandWithoutParametersProcessor extends AbstractCommandProcessor {
    private final ClientGUIModel guiModel;
    private final BatMUDGoalsPluginData data;

    public GoalCommandWithoutParametersProcessor(ClientGUIModel guiModel,
            BatMUDGoalsPluginData data) {
        super("\\s*goal\\s*");
        this.guiModel = guiModel;
        this.data = data;
    }

    @Override
    protected boolean process(Matcher m) {

        Collection<String> storedSkills = data.getStoredSkills();
        if (storedSkills.isEmpty()) {
            guiModel.printMessage("No data.");
        } else {
            storedSkills
                    .forEach(skillName -> guiModel.printMessage(String.format("%s%s", skillName,
                            data.isGoalSkill(skillName) ? " (*)" : "")));

        }
        return true;
    }
}