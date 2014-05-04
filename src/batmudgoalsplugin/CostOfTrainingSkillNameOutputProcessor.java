package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'cost train <skill>' command. Stores the skill name
 * from the outputted table.
 */
class CostOfTrainingSkillNameOutputProcessor extends
		AbstractCommandProcessor {

	private PercentCostOutputProcessor op;

	public CostOfTrainingSkillNameOutputProcessor(
			PercentCostOutputProcessor op, BatMUDGoalsPluginData data) {
		super("\\|\\s+Cost\\s+of\\s+training\\s+([^\\|]+)\\s+\\|\\s+",
				null, data);
		this.op = op;
	}

	@Override
	protected boolean process(Matcher m) {
		String skill = m.group(1).toLowerCase().trim();
		op.setSkill(skill);
		return false;
	}
}