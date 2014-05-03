package batmudgoalsplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.ClientGUI;

/**
 * Base implementation for command processors.
 * 
 * @author Matti
 *
 */
abstract class AbstractCommandProcessor {
	private final Pattern pattern;
	private ClientGUI clientGUI;

	/**
	 * Extending classes should call this constructor to provide a regular
	 * expression and optionally a ClientGUI if they want to print messages to
	 * the user
	 * 
	 * @param regexp
	 * @param clientGui
	 */
	public AbstractCommandProcessor(String regexp, ClientGUI clientGui) {
		this.clientGUI = clientGui;
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
	 * Extending classes may override this method to provide logic of deciding
	 * what to return. They can also use other than Matcher.matches() to trigger
	 * actions.
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
	 * Extending classes implement the logic what happens when the regexp is
	 * matched in this method.
	 * 
	 * @param m
	 * @return true if input should not be returned to the client for processing
	 */
	protected abstract boolean process(Matcher m);

	protected void printMessage(String message) {
		clientGUI.printText("generic", String.format("%s\n", message));
	}

	protected void printMessage(String format, Object... args) {
		printMessage(String.format(format, args));
	}
}