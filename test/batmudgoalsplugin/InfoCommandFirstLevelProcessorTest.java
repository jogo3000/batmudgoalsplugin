package batmudgoalsplugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import batmudgoalsplugin.BatMUDGoalsPlugin.InfoCommandSkillMaxOutputProcessor;

public class InfoCommandFirstLevelProcessorTest {

	@Test
	public void testAbilitiesGainedWhenJoining() {
		InfoCommandSkillMaxOutputProcessor mock = mock(InfoCommandSkillMaxOutputProcessor.class);
		new InfoCommandFirstLevelProcessor(mock, null)
				.receive("Abilities gained when joining:");

		verify(mock).setLevel(1);
	}

}
