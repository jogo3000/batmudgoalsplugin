package batmudgoalsplugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class TrainedPartiallyOutputProcessorTest {

	@Test
	public void testTrainBladedFuryPartially() {
		verifyPartialTrain(
				"You partially train bladed fury which cost you 300000 experience.",
				"bladed fury");
	}

	@Test
	public void testTrainAttackPartially() throws Exception {
		verifyPartialTrain(
				"You partially train attack which cost you 300000 experience.",
				"attack");
	}

	@Test
	public void testStudyCureLightWoundsPartially() throws Exception {
		verifyPartialTrain(
				"You partially study cure light wounds which cost you 300000 experience.",
				"cure light wounds");
	}

	@Test
	public void testHandlesWhitespace() throws Exception {
		verifyPartialTrain(
				"You partially train attack which cost you 300000 experience.\n",
				"attack");

	}

	@Test
	public void testCRLF() throws Exception {
		verifyPartialTrain(
				"You partially train bladed fury which cost you 300000 experience.\r\n",
				"bladed fury");
	}

	private void verifyPartialTrain(String input, String skill) {
		BatMUDGoalsPluginData mock = mock(BatMUDGoalsPluginData.class);
		TrainedPartiallyOutputProcessor op = new TrainedPartiallyOutputProcessor(
				mock);
		op.receive(input);
		verify(mock).trainPartially(skill);
	}

}
