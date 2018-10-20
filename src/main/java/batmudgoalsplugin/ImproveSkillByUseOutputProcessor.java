package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class ImproveSkillByUseOutputProcessor extends AbstractCommandProcessor {

	public ImproveSkillByUseOutputProcessor(BatMUDGoalsPluginData data) {
		super("You feel like you just got slightly better in (.+)", null, data);
	}

	@Override
	protected boolean process(Matcher m) {
		String skillName = normalizeSkillName(m.group(1));
		data.setSkillStatus(skillName,
				data.getCurrentSkillStatus(skillName) + 1);
		return false;
	}

}
