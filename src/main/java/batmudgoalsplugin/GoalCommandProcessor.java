package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Catches parameterised goal command, e.g. 'goal attack' and sets the goal if
 * possible
 */
class GoalCommandProcessor extends AbstractCommandProcessor {

    private final ClientGUIModel guiModel;
    private final BatMUDGoalsPluginData data;

    public GoalCommandProcessor(ClientGUIModel guiModel, BatMUDGoalsPluginData data) {
        super("goal\\s*(.+)\\s*");
        this.data = data;
        if (guiModel == null) {
            throw new IllegalStateException("Cannot function without access to the GUI");
        }
        if (data == null) {
            throw new IllegalStateException("Cannot function without data model!");
        }
        this.guiModel = guiModel;
    }

    @Override
    protected boolean process(Matcher m) {
        // If a skill is given as goal parameter, normalize skill name and
        // set goal
        String skillName = normalizeSkillName(m.group(1));
        if (!data.isSkillInCostLibrary(skillName)) {
            guiModel.printMessage(String.format("%s not in library", skillName));
        } else {
            data.setGoalSkill(skillName);
            guiModel.printMessage(String.format("Next goal: %s", skillName));
        }
        return true; // Stop command from being processed by client
    }
}