package batmudgoalsplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.ClientGUI;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Base implementation for command processors.
 */
abstract class AbstractCommandProcessor {
    private final Pattern pattern;
    private final ClientGUIModel gui;
    protected BatMUDGoalsPluginData data;

    /**
     * Extending classes should call this constructor to provide a regular
     * expression and optionally a {@link ClientGUI} if they want to print messages
     * to the user and {@link BatMUDGoalsPluginData} if they need the data scraped
     * from commands and outputs
     * 
     * @param regexp
     * @param plugin optional
     * @param data   optional
     */
    public AbstractCommandProcessor(String regexp, ClientGUIModel gui, BatMUDGoalsPluginData data) {
        this.gui = gui;
        this.data = data;
        this.pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Extending classes should not need to override this method.
     * 
     * @param input
     */
    public final boolean receive(String input) {
        return decideReturn(pattern.matcher(input));
    }

    /**
     * Extending classes may override this method to provide logic of deciding what
     * to return. They can also use other than Matcher.matches() to trigger actions.
     * 
     * @param m
     * @return
     */
    protected boolean decideReturn(Matcher m) {
        if (m.matches()) {
            return process(m);
        }
        return false;
    }

    /**
     * Extending classes implement the logic what happens when the regexp is matched
     * in this method.
     * 
     * @param m
     * @return true if input should not be returned to the client for processing
     */
    protected abstract boolean process(Matcher m);

    /**
     * Prints message to the batclient text input area
     * 
     * @param message
     */
    protected void printMessage(String message) {
        gui.printText("generic", String.format("%s%n", message));
    }

    protected void printMessage(String format, Object... args) {
        printMessage(String.format(format, args));
    }

    /**
     * Removes extra whitespaces and puts to lowercase
     * 
     * @param originalSkillName
     * @return normalized skill name
     */
    protected String normalizeSkillName(String originalSkillName) {
        StringBuilder sb = new StringBuilder();
        for (String s : originalSkillName.split("\\s+")) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim().toLowerCase();
    }
}