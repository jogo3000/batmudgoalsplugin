package batmudgoalsplugin;

import java.util.regex.Matcher;

/**
 * Processes output from guildname info command - e.g. 'ranger info'. Skill
 * maxes for each level are reported after a row containing the level number.
 */
class InfoCommandLevelNumberProcessor extends AbstractOutputProcessor {

    private InfoCommandSkillMaxOutputProcessor op;

    public InfoCommandLevelNumberProcessor(
            InfoCommandSkillMaxOutputProcessor op) {
        super("\\s*Level\\s+(\\d+):\\s*");
        this.op = op;
    }

    @Override
    protected void process(Matcher m) {
        op.setLevel(Integer.parseInt(m.group(1)));
    }
}