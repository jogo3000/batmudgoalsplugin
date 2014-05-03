package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'train' command. Stores the skill percents shown in the
 * train skill table.
 */
class TrainCommandOutputProcessor extends AbstractCommandProcessor {
	public TrainCommandOutputProcessor(BatMUDGoalsPluginData data) {
		super(
				"\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+|\\(n/a\\))\\s+\\|\\s*",
				null, data);
	}

	@Override
	protected boolean process(Matcher m) {
		data.setSkillStatus(readSkillName(m), readSkillStatus(m));
		return false;
	}

	private int readSkillStatus(Matcher m) {
		return Integer.parseInt(m.group(2));
	}

	private String readSkillName(Matcher m) {
		return m.group(1).trim().toLowerCase();
	}
}