package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Catches parameterised goal command, e.g. 'goal attack' and sets the goal if
 * possible
 */
class GoalCommandProcessor extends AbstractCommandProcessor {

    public GoalCommandProcessor(ClientGUIModel guiModel, BatMUDGoalsPluginData data) {
        super("goal\\s*(.+)\\s*", guiModel, data);
        if (guiModel == null) {
            throw new IllegalStateException("Cannot function without access to the GUI");
        }
        if (data == null) {
            throw new IllegalStateException("Cannot function without data model!");
        }
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