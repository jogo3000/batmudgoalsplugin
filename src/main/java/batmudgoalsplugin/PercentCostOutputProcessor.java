package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'cost train <skill>' command. Stores the experience
 * costs of skill percents.
 */
class PercentCostOutputProcessor extends AbstractCommandProcessor {

    private String skill;

    public PercentCostOutputProcessor(BatMUDGoalsPluginData data) {
        super("\\|\\s+(\\d+)%\\s+=\\s+(\\d+)", null, data);
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    protected boolean decideReturn(Matcher m) {
        processAllMatchesOnALine(m);
        return false;
    }

    private void processAllMatchesOnALine(Matcher matcher) {
        while (matcher.find()) {
            process(matcher);
        }
    }

    @Override
    protected boolean process(Matcher m) {
        int skillLevel = Integer.parseInt(m.group(1));
        int skillCost = Integer.parseInt(m.group(2));
        data.setSkillCostForLevel(skill, skillLevel, skillCost);
        return false;
    }
}