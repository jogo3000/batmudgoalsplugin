package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from guildcommand info command - e.g. 'ranger info'. Stores
 * the player level in that guild.
 */
class PlayerLevelOutputProcessor extends AbstractOutputProcessor implements
        IGuildNameListener {

    private final BatMUDGoalsPluginData data;

    private String guild;

    public PlayerLevelOutputProcessor(BatMUDGoalsPluginData data) {
        super("Your level:\\s+(\\d+)\\s*");
        this.data = data;
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
    protected void process(Matcher m) {
        int level = Integer.parseInt(m.group(1));
        data.setGuildLevel(guild, level);
    }
}