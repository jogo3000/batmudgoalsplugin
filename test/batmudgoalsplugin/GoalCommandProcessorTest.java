package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.ClientGUI;

public class GoalCommandProcessorTest {

	protected ClientGUI verifyOutput(BatMUDGoalsPluginData data, String input) {
		ClientGUI mock = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(mock);
		new GoalCommandProcessor(plugin, data).receive(input);
		ClientGUI verify = verify(mock);
		return verify;
	}

	@Test
	public void testSkillNotInLibrary() {
		verifyOutput(new BatMUDGoalsPluginData(), "goal vandalism").printText(
				"generic", String.format("vandalism not in library%n"));
	}

	@Test
	public void testSkillInLibrary() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setSkillCost("torch creation", 1, 1);
		verifyOutput(data, "goal torch creation").printText("generic",
				String.format("Next goal: torch creation%n"));
	}

	@Test
	public void testSkillInLibrary_MixedCaseAndWhiteSpace() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setSkillCost("torch creation", 1, 1);
		verifyOutput(data, "goal torch   Creation").printText("generic",
				String.format("Next goal: torch creation%n"));
	}

	@Test
	public void testInvalidParameterShouldNotClearGoal() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		BatMUDGoalsPlugin plugin = mock(BatMUDGoalsPlugin.class);
		when(plugin.getClientGUI()).thenReturn(mock(ClientGUI.class));
		GoalCommandProcessor cp = new GoalCommandProcessor(plugin, data);

		cp.receive("goal foo");
		assertEquals("attack", data.getGoalSkill());
	}
}
