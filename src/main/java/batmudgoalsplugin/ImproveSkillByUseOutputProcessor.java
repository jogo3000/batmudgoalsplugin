package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class ImproveSkillByUseOutputProcessor extends AbstractOutputProcessor {

    private final BatMUDGoalsPluginData data;

    public ImproveSkillByUseOutputProcessor(BatMUDGoalsPluginData data) {
        super("You feel like you just got slightly better in (.+)");
        this.data = data;
    }

    @Override
    protected void process(Matcher m) {
        String skillName = normalizeSkillName(m.group(1));
        data.setSkillStatus(skillName,
                data.getCurrentSkillStatus(skillName) + 1);
    }

}
