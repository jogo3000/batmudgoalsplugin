package batmudgoalsplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base implementation for command processors.
 */
abstract class AbstractCommandProcessor {
    private final Pattern pattern;

    /**
     * Extending classes should call this constructor to provide a regular
     * expression which is matched against command output. On matches, the extending
     * class will receive a call to its process method with the {@link Matcher}
     * 
     * @param regexp
     * @param plugin optional
     */
    public AbstractCommandProcessor(String regexp) {
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