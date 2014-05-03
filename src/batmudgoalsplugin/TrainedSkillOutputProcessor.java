package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from train skill command. Sets the current skill percent to
 * the new percent.
 * 
 * @author jogo3000
 *
 */
class TrainedSkillOutputProcessor extends AbstractCommandProcessor {
	public TrainedSkillOutputProcessor(BatMUDGoalsPluginData data) {
		super("You now have '([^']+)' at (\\d+)% without special bonuses.\\s+",
				null, data);
	}

	@Override
	protected boolean process(Matcher m) {
		data.setSkillStatus(m.group(1).trim().toLowerCase(),
				Integer.parseInt(m.group(2).trim()));
		return false;
	}

}