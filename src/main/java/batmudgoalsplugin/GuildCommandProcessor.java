package batmudgoalsplugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;

/**
 * Catches guild info commands, e.g. 'barbarian info' and stores the guild name
 * from that command.
 */
class GuildCommandProcessor extends AbstractCommandProcessor {

    private Collection<IGuildNameListener> listeners;

    public GuildCommandProcessor(IGuildNameListener... listeners) {
        super("(?:.*;)*\\s*(.+)\\sinfo\\s*;*.*");
        this.listeners = Arrays.asList(listeners);
    }

    @Override
    protected boolean process(Matcher m) {
        listeners.forEach(l -> l.setGuild(m.group(1).trim().toLowerCase()));
        return false; // Always forward the command to client
    }

}