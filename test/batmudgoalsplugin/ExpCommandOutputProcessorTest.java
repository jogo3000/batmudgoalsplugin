package batmudgoalsplugin;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.ClientGUI;

public class ExpCommandOutputProcessorTest {

	/**
	 * Should print 'Goal <skill>: full'
	 */
	@Test
	public void testGoalIsFull() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI mock = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(mock);

		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 100);
		op.receive("Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0");

		verify(mock).printText("generic", "Goal attack: full\n");
	}

	/**
	 * Should print amount needed to improve the skill
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNotEnoughExp() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI clientGUI = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(clientGUI);

		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 1);
		data.setSkillCost("attack", 2, 200);
		data.setGuildLevel("tzarakk", 1);
		data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
		op.receive("Exp: 13 Money: 0.00 Bank: 644404.00 Exp pool: 0");

		verify(clientGUI).printText("generic",
				String.format("Goal attack: 200 You need: %d\n", 200 - 13));
	}

	/**
	 * Should print list of guilds where can advance the skill. In this case
	 * only one guild is provided.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEnoughExp() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI clientGUI = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(clientGUI);
		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 1);
		data.setSkillCost("attack", 2, 200);
		data.setGuildLevel("tzarakk", 1);
		data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
		op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

		verify(clientGUI).printText("generic",
				"Goal attack: 200 You have enough to advance in: tzarakk\n");
	}

	/**
	 * Should print list of guilds where can advance the skill
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEnoughExpToAdvanceInMultipleGuilds() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI clientGUI = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(clientGUI);
		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 1);
		data.setSkillCost("attack", 2, 200);
		data.setGuildLevel("tzarakk", 1);
		data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
		data.setGuildLevel("tarmalen", 12);
		data.setSkillMaxInfo("tarmalen", "attack", 1, 12);
		data.setGuildLevel("barbarian", 1);
		data.setSkillMaxInfo("tarmalen", "attack", 1, 1);
		op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

		verify(clientGUI)
				.printText("generic",
						"Goal attack: 200 You have enough to advance in: tarmalen, tzarakk\n");
	}

	/**
	 * Should print that player needs levels
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGoalNeedsLevel() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI clientGUI = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(clientGUI);
		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 1);
		data.setSkillCost("attack", 2, 200);
		data.setGuildLevel("tzarakk", 1);
		data.setSkillMaxInfo("tzarakk", "attack", 12, 12);
		data.setGuildLevel("tarmalen", 12);
		data.setSkillMaxInfo("tarmalen", "attack", 13, 12);
		data.setGuildLevel("barbarian", 1);
		data.setSkillMaxInfo("tarmalen", "attack", 1, 1);
		op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

		verify(clientGUI).printText("generic", "Goal attack: needs level\n");

	}

	/**
	 * Goal is not set, should not print anything
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNoGoal() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		ClientGUI clientGUI = mock(ClientGUI.class);
		BatClientPlugin plugin = mock(BatClientPlugin.class);
		when(plugin.getClientGUI()).thenReturn(clientGUI);
		ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(plugin,
				data);

		op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");
		verify(clientGUI, never()).printText(anyString(), anyString());
		;
	}

}
