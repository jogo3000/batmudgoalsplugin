package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from guildname info command, e.g. 'ranger info'. Stores
 * skill max from the output. This depends on the earlier rows printed by the
 * client telling the guild name and guild level.
 */
class InfoCommandSkillMaxOutputProcessor extends AbstractCommandProcessor
		implements IGuildNameListener {

	private int level;
	private String guild;

	public InfoCommandSkillMaxOutputProcessor(BatMUDGoalsPluginData data) {
		super(
				"\\s*May\\s+(?:train\\s+skill|study\\s+spell)\\s+(.+)\\s+to\\s+(\\d+)%\\s*",
				null, data);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see batmudgoalsplugin.IGuildNameListener#setGuild(java.lang.String)
	 */
	@Override
	public void setGuild(String guild) {
		this.guild = guild;
	}

	@Override
	protected boolean process(Matcher m) {
		String skillName = normalizeSkillName(m.group(1));
        int skillMax = Integer.parseInt(m.group(2));
        data.setSkillMaxInfo(guild, skillName, level,
				skillMax);
		return false;
	}
}