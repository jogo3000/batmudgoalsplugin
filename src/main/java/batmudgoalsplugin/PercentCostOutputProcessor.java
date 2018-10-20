package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'cost train <skill>' command. Stores the experience
 * costs of skill percents.
 */
class PercentCostOutputProcessor extends AbstractCommandProcessor {

	private String skill;

	public PercentCostOutputProcessor(BatMUDGoalsPluginData data) {
		super("\\|\\s+(\\d+)%\\s+=\\s+(\\d+)", null, data);
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	@Override
	protected boolean decideReturn(Matcher m) {
		while (m.find()) {
			process(m);
		}
		return false;
	}

	@Override
	protected boolean process(Matcher m) {
		data.setSkillCost(skill, Integer.parseInt(m.group(1)),
				Integer.parseInt(m.group(2)));
		return false;
	}
}