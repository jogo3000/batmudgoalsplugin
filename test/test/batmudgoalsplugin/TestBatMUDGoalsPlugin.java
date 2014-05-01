package test.batmudgoalsplugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;

import org.junit.Test;

import batmudgoalsplugin.BatMUDGoalsPlugin;

import com.mythicscape.batclient.interfaces.BatButton;
import com.mythicscape.batclient.interfaces.BatScrollPane;
import com.mythicscape.batclient.interfaces.BatTooltipManager;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;
import com.mythicscape.batclient.interfaces.SoundPlayer;

public class TestBatMUDGoalsPlugin {

	@Test
	public void testSkillNotInLibrary() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal looting and burning");
		plugin.assertPluginPrints("looting and burning not in library", 0);
	}

	@Test
	public void testSetGoalSuccesfully() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.assertPluginPrints("Next goal is attack", 0);
	}

	@Test
	public void testGoalSetListGoals() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText(",-------------------------------------------------.");
		plugin.receiveText("| Cost of training Looting and burning                         |\n");
		plugin.receiveText("|-------------------------------------------------|");
		plugin.receiveText("| Percent     Exp        | Percent     Exp        |");
		plugin.receiveText("|=================================================|");
		plugin.receiveText("|    1% =            80  |   51% =          9046  |");
		plugin.receiveText("|    2% =            82  |   52% =          9700  |");
		plugin.receiveText("|    3% =            86  |   53% =         10395  |");
		plugin.trigger("goal attack");
		plugin.trigger("goal");

		plugin.assertPluginPrints("attack (*)", 1);
		plugin.assertPluginPrints("looting and burning", 2);
	}

	@Test
	public void testParseCostTrain() {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		plugin.assertPluginPrints(
				"Goal attack: 17768 You need: "
						+ Integer.toString(17768 - 12920), 1);

		plugin.trigger("goal");
		plugin.assertPluginPrints("attack (*)", 2);

	}

	@Test
	public void testExpOutputWithZeroValues() {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 0 Bank: 0 Exp pool: 0\n");
		plugin.assertPluginPrints(
				"Goal attack: 17768 You need: "
						+ Integer.toString(17768 - 12920), 1);
	}

	@Test
	public void testEnoughExp() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 100000 Money: 0 Bank: 0 Exp pool: 0\n");
		plugin.assertPluginPrints(
				"Goal attack: 17768 You have enough to advance", 1);
	}

	@Test
	public void testNeedAnotherLevel() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("| Attack                      |  85 |  85 | 85 |       22015 |\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		assertEquals("Goal attack: needs level\n", plugin.getPrints().get(1));
	}

	@Test
	public void testSkillIsFull() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("| Attack                      |  100 |  85 | 100 |       (n/a) |\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		assertEquals("Goal attack: full\n", plugin.getPrints().get(1));

	}

	@Test
	public void testTrainOutput() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("You now have 'Attack' at 100% without special bonuses.\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		assertEquals("Goal attack: full\n", plugin.getPrints().get(1));
	}

	private MockGoalCommandPlugin initiatePlugin() {
		MockGoalCommandPlugin plugin = new MockGoalCommandPlugin();

		plugin.receiveText(",-------------------------------------------------.");
		plugin.receiveText("| Cost of training Attack                         |\n");
		plugin.receiveText("|-------------------------------------------------|");
		plugin.receiveText("| Percent     Exp        | Percent     Exp        |");
		plugin.receiveText("|=================================================|");
		plugin.receiveText("|    1% =            80  |   51% =          9046  |");
		plugin.receiveText("|    2% =            82  |   52% =          9700  |");
		plugin.receiveText("|    3% =            86  |   53% =         10395  |");
		plugin.receiveText("|    4% =            91  |   54% =         11135  |");
		plugin.receiveText("|    5% =            99  |   55% =         11921  |");
		plugin.receiveText("|    6% =           109  |   56% =         12756  |");
		plugin.receiveText("|    7% =           121  |   57% =         13642  |");
		plugin.receiveText("|    8% =           137  |   58% =         14584  |");
		plugin.receiveText("|    9% =           155  |   59% =         15583  |");
		plugin.receiveText("|   10% =           177  |   60% =         16643  |");
		plugin.receiveText("|   11% =           203  |   86% =         17768  |");
		plugin.receiveText("|   1% to 86% =         200000000  |");

		plugin.receiveText("| Attack                      |  85 |  85 | 100 |       22015 |\n");
		return plugin;
	}

	private static class MockGoalCommandPlugin extends BatMUDGoalsPlugin {
		List<String> prints = new ArrayList<String>();

		public List<String> getPrints() {
			return prints;
		}

		/**
		 * Convenience method to assert a print generated by plugin
		 * 
		 * @param expected
		 * @param printNumber
		 */
		public void assertPluginPrints(String expected, int printNumber) {
			assertTrue(prints.size() - 1 >= printNumber);
			assertEquals(expected + "\n", prints.get(printNumber));
		}

		/**
		 * Convenience method to call client's trigger(ParsedResult) method
		 * 
		 * @param text
		 */
		public void receiveText(String text) {
			trigger(new ParsedResult(text));
		}

		@Override
		public ClientGUI getClientGUI() {
			return new ClientGUI() {

				@Override
				public void setWallpaper(String imagePath, float brightness) {
					// Not needed

				}

				@Override
				public void setWallpaper(String imagePath) {
					// Not needed

				}

				@Override
				public void setActionButton(int type, int button, String title,
						String action, String customImageURL) {
					// Not needed

				}

				@Override
				public void printTextToWindow(String windowname, String msg,
						boolean useTriggers) {
					// Not needed

				}

				@Override
				public void printTextToWindow(String windowname, String msg) {
					// Not needed

				}

				@Override
				public void printText(String channel, String msg,
						boolean useTriggers, boolean catchAll) {
					// Not needed

				}

				@Override
				public void printText(String channel, String msg,
						boolean useTriggers) {
					// Not needed

				}

				@Override
				public void printText(String channel, String msg,
						String htmlColorCode) {
					// Not needed

				}

				@Override
				public void printText(String channel, String msg) {
					prints.add(msg);
				}

				@Override
				public void printAttributedStringToWindow(String windowname,
						ParsedResult msg, boolean useTriggers) {
					// Not needed

				}

				@Override
				public void printAttributedStringToWindow(String windowname,
						ParsedResult msg) {
					// Not needed

				}

				@Override
				public void printAttributedString(String channel,
						ParsedResult msg, boolean useTriggers, boolean catchAll) {
					// Not needed

				}

				@Override
				public void printAttributedString(String channel,
						ParsedResult msg, boolean useTriggers) {
					// Not needed

				}

				@Override
				public void printAttributedString(String channel,
						ParsedResult msg) {
					// Not needed

				}

				@Override
				public SoundPlayer getSoundPlayer() {
					// Not needed
					return null;
				}

				@Override
				public BatTooltipManager getBatTooltipManager() {
					// Not needed
					return null;
				}

				@Override
				public String getBaseDirectory() {
					// Not needed
					return null;
				}

				@Override
				public void doCommand(String cmd, ArrayList<String> vars) {
					// Not needed

				}

				@Override
				public void doCommand(String cmd) {
					// Not needed

				}

				@Override
				public BatScrollPane createScrollPane(Component view,
						int verticalPolicy, int horizontalPolicy) {
					// Not needed
					return null;
				}

				@Override
				public BatScrollPane createScrollPane(Component view) {
					// Not needed
					return null;
				}

				@Override
				public BatWindow createBatWindow(String name, int x, int y,
						int width, int height) {
					// Not needed
					return null;
				}

				@Override
				public JTextPane createBatTextArea() {
					// Not needed
					return null;
				}

				@Override
				public BatButton createBatButton(String title,
						ActionListener listener) {
					// Not needed
					return null;
				}
			};
		}
	}
}
