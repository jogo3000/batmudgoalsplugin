package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from train skill command. Sets the current skill percent to
 * the new percent. Output from studying a spell or skill e.g. <code>
 * 
 * This costs you 4830 experience points.
 * Studied total 1% of the spell.
 * You now have 'Make scar' at 51% without special bonuses.
 * With current bonuses it is at 51%. Current maximum without bonuses is 100%.
 * </code>
 */
class TrainedSkillOutputProcessor extends AbstractCommandProcessor {
	public TrainedSkillOutputProcessor(BatMUDGoalsPluginData data) {
		super("You now have '([^']+)' at (\\d+)% without special bonuses.\\s*",
				null, data);
	}

	@Override
	protected boolean process(Matcher m) {
		String skillName = normalizeSkillName(m.group(1));
		data.setSkillStatus(skillName, Integer.parseInt(m.group(2).trim()));
		data.clearPartialTrains(skillName);
		return false;
	}

}