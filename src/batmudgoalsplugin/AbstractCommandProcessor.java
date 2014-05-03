package batmudgoalsplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base implementation for command processors.
 * 
 * @author Matti
 *
 */
abstract class AbstractCommandProcessor {
	private final Pattern pattern;

	/**
	 * Extending classes should call this constructor to provide a regular
	 * expression
	 * 
	 * @param regexp
	 */
	public AbstractCommandProcessor(String regexp) {
		this.pattern = Pattern.compile(regexp);
	}

	/**
	 * Extending classes should not need to override this method.
	 * 
	 * @param input
	 */
	public final void receive(String input) {
		Matcher m = pattern.matcher(input);
		if (m.matches()) {
			process(m);
		}
	}

	/**
	 * Extending classes implement the logic what happens when the regexp is
	 * matched in this method.
	 * 
	 * @param m
	 */
	protected abstract void process(Matcher m);
}