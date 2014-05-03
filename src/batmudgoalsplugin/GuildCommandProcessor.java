package batmudgoalsplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;

/**
 * Catches guild info commands, e.g. 'barbarian info' and stores the guild name
 * from that command.
 */
public class GuildCommandProcessor extends AbstractCommandProcessor {

	private Collection<IGuildNameListener> listeners;

	public GuildCommandProcessor(IGuildNameListener... listeners) {
		super("(?:.*;)*\\s*(.+)\\sinfo\\s*;*.*", null);
		this.listeners = new ArrayList<IGuildNameListener>(
				Arrays.asList(listeners));
	}

	@Override
	protected boolean process(Matcher m) {
		for (IGuildNameListener l : listeners) {
			l.setGuild(m.group(1).trim().toLowerCase());
		}
		return false; // Always forward the command to client
	}

}