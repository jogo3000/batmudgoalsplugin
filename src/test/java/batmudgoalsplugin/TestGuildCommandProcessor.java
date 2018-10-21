package batmudgoalsplugin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for guildname info command processor. When e.g. 'tzarakk info' is
 * received by the processor, 'tzarakk' should be sent to all the
 * {@link IGuildNameListener} implementations
 */
public class TestGuildCommandProcessor {

    private IGuildNameListener callReceive(String input) {
        IGuildNameListener mock = mock(IGuildNameListener.class);
        new GuildCommandProcessor(mock).receive(input);
        return mock;
    }

    @Test
    public void testProcessorIgnoresRandomStrings() {
        IGuildNameListener mock = callReceive("foo bar");
        verify(mock, never()).setGuild(anyString());
    }

    private IGuildNameListener callReceiveAndVerify(String input) {
        return verify(callReceive(input));
    }

    @Test
    public void testRangerInfo() throws Exception {
        callReceiveAndVerify("ranger info").setGuild("ranger");
    }

    @Test
    public void testRangerInfoWhiteSpaces() throws Exception {
        callReceiveAndVerify("  ranger   info ").setGuild("ranger");
    }

    @Test
    public void testRangerInfoMixedCase() throws Exception {
        callReceiveAndVerify("  raNger   INFO ").setGuild("ranger");
    }

    @Test
    public void testRangerInfoWithDelimiter() throws Exception {
        callReceiveAndVerify("ranger info;foo").setGuild("ranger");
    }

    @Test
    public void testBarbarianPartOfDelimitedCommand() throws Exception {
        callReceiveAndVerify("n;barbarian info;foo").setGuild("barbarian");
    }

    @Test
    public void testTzarakkLastOfDelimitedCommand() throws Exception {
        callReceiveAndVerify("n;w;tzarakk info").setGuild("tzarakk");
    }

}
