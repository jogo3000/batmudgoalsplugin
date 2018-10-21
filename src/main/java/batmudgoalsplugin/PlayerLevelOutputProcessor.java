package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from guildcommand info command - e.g. 'ranger info'. Stores
 * the player level in that guild.
 */
class PlayerLevelOutputProcessor extends AbstractCommandProcessor implements
		IGuildNameListener {

	private String guild;

	public PlayerLevelOutputProcessor(BatMUDGoalsPluginData data) {
		super("Your level:\\s+(\\d+)\\s*", null, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see batmudgoalsplugin.GuildNameListener#setGuild(java.lang.String)
	 */
	@Override
	public void setGuild(String guild) {
		this.guild = guild;
	}

	@Override
	protected boolean process(Matcher m) {
		int level = Integer.parseInt(m.group(1));
        data.setGuildLevel(guild, level);
		return false;
	}
}