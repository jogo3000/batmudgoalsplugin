package batmudgoalsplugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.ClientGUI;

public class GoalCommandProcessorTest {

	protected ClientGUI verifyOutput(BatMUDGoalsPluginData data, String input) {
		ClientGUI mock = mock(ClientGUI.class);
		new GoalCommandProcessor(mock, data).receive(input);
		ClientGUI verify = verify(mock);
		return verify;
	}

	@Test
	public void testSkillNotInLibrary() {
		verifyOutput(new BatMUDGoalsPluginData(), "goal vandalism").printText(
				"generic", "vandalism not in library\n");
	}

	@Test
	public void testSkillInLibrary() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setSkillCost("torch creation", 1, 1);
		verifyOutput(data, "goal torch creation").printText("generic",
				"Next goal: torch creation\n");
	}

	@Test
	public void testSkillInLibrary_MixedCaseAndWhiteSpace() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setSkillCost("torch creation", 1, 1);
		verifyOutput(data, "goal torch   Creation").printText("generic",
				"Next goal: torch creation\n");
	}
}
