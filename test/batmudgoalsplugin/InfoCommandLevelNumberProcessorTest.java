package batmudgoalsplugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class InfoCommandLevelNumberProcessorTest {

	@Test
	public void testLevel2() {
		InfoCommandSkillMaxOutputProcessor mock = mock(InfoCommandSkillMaxOutputProcessor.class);
		new InfoCommandLevelNumberProcessor(mock, null).receive("Level 2:");
		verify(mock).setLevel(2);
	}

	@Test
	public void testLevel20() {
		InfoCommandSkillMaxOutputProcessor mock = mock(InfoCommandSkillMaxOutputProcessor.class);
		new InfoCommandLevelNumberProcessor(mock, null).receive("Level 20:");
		verify(mock).setLevel(20);
	}
}
