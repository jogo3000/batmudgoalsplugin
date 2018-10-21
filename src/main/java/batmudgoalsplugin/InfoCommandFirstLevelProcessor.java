package batmudgoalsplugin;

import java.util.regex.Matcher;

/**
 * Processes output from guildname info command - e.g. 'barbarian info'. First
 * level skills are given after a message 'Abilities gained when joining:'
 */
class InfoCommandFirstLevelProcessor extends AbstractOutputProcessor {

    private InfoCommandSkillMaxOutputProcessor op;

    public InfoCommandFirstLevelProcessor(
            InfoCommandSkillMaxOutputProcessor op) {
        super("Abilities gained when joining:\\s*");
        this.op = op;
    }

    @Override
    protected void process(Matcher m) {
        op.setLevel(1);
    }
}