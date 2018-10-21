package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'cost train <skill>' command. Stores the experience
 * costs of skill percents.
 */
class PercentCostOutputProcessor extends AbstractOutputProcessor {

    private final BatMUDGoalsPluginData data;
    private String skill;

    public PercentCostOutputProcessor(BatMUDGoalsPluginData data) {
        super("\\|\\s+(\\d+)%\\s+=\\s+(\\d+)");
        this.data = data;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    protected void decideProcess(Matcher m) {
        processAllMatchesOnALine(m);
    }

    private void processAllMatchesOnALine(Matcher matcher) {
        while (matcher.find()) {
            process(matcher);
        }
    }

    @Override
    protected void process(Matcher m) {
        int skillLevel = Integer.parseInt(m.group(1));
        int skillCost = Integer.parseInt(m.group(2));
        data.setSkillCostForLevel(skill, skillLevel, skillCost);
    }
}