package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from guildname info command - e.g. 'ranger info'. Skill
 * maxes for each level are reported after a row containing the level number.
 */
class InfoCommandLevelNumberProcessor extends AbstractCommandProcessor {

	private InfoCommandSkillMaxOutputProcessor op;

	public InfoCommandLevelNumberProcessor(
			InfoCommandSkillMaxOutputProcessor op, BatMUDGoalsPluginData data) {
		super("\\s*Level\\s+(\\d+):\\s*", null, data);
		this.op = op;
	}

	@Override
	protected boolean process(Matcher m) {
		op.setLevel(Integer.parseInt(m.group(1)));
		return false;
	}
}