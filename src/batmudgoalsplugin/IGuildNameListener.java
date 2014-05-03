package batmudgoalsplugin;

/**
 * Implementing classes want to be notified that an
 * {@link AbstractCommandProcessor} has picked a guild name from the output
 * 
 * @author jogo3000
 *
 */
interface IGuildNameListener {

	public abstract void setGuild(String guild);

}