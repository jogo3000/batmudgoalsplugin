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

	public void receive(String input) {
		Matcher m = pattern.matcher(input);
		if (m.matches()) {
			process(m);
		}
	}

	protected abstract void process(Matcher m);
}