package batmudgoalsplugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base implementation for command processors.
 */
abstract class AbstractOutputProcessor {
    private final Pattern pattern;

    /**
     * Extending classes should call this constructor to provide a regular
     * expression which is matched against command output. On matches, the extending
     * class will receive a call to its process method with the {@link Matcher}
     *
     * @param regexp
     * @param plugin optional
     */
    public AbstractOutputProcessor(final String regexp) {
        this.pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Extending classes should not need to override this method.
     *
     * @param input
     */
    public final void receive(final String input) {
        decideProcess(pattern.matcher(input));
    }

    /**
     * Extending classes may override this method to provide logic of deciding when
     * to process
     *
     * @param m
     */
    protected void decideProcess(final Matcher m) {
        if (m.matches()) {
            process(m);
        }
    }

    /**
     * Extending classes implement the logic what happens when the regexp is matched
     * in this method.
     *
     * @param m
     */
    protected abstract void process(Matcher m);

    /**
     * Removes extra whitespaces and puts to lowercase
     *
     * @param originalSkillName
     * @return normalized skill name
     */
    protected String normalizeSkillName(final String originalSkillName) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : originalSkillName.split("\\s+")) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString().trim().toLowerCase();
    }
}
