package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from guildname info command - e.g. 'barbarian info'. First
 * level skills are given after a message 'Abilities gained when joining:'
 */
class InfoCommandFirstLevelProcessor extends AbstractCommandProcessor {

	private InfoCommandSkillMaxOutputProcessor op;

	public InfoCommandFirstLevelProcessor(
			InfoCommandSkillMaxOutputProcessor op, BatMUDGoalsPluginData data) {
		super("Abilities gained when joining:\\s*", null, data);
		this.op = op;
	}

	@Override
	protected boolean process(Matcher m) {
		op.setLevel(1);
		return false;
	}
}