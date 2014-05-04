package batmudgoalsplugin;

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
		plugin.assertPluginPrints("Next goal: attack", 0);
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
		plugin.receiveText("Exp: 2 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		plugin.assertPluginPrints(
				"Goal attack: 82 You need: " + Integer.toString(82 - 2), 1);

		plugin.trigger("goal");
		plugin.assertPluginPrints("attack (*)", 2);

	}

	@Test
	public void testExpOutputWithZeroValues() {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 2 Money: 0 Bank: 0 Exp pool: 0\n");
		plugin.assertPluginPrints(
				"Goal attack: 82 You need: " + Integer.toString(82 - 2), 1);
	}

	@Test
	public void testEnoughExp() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 100000 Money: 0 Bank: 0 Exp pool: 0\n");
		plugin.assertPluginPrints(
				"Goal attack: 82 You have enough to advance in: ranger, barbarian",
				1);
	}

	@Test
	public void testNeedAnotherLevel() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("| Attack                      |  85 |  85 | 85 |       22015 |\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		plugin.assertPluginPrints("Goal attack: needs level", 1);
	}

	@Test
	public void testSkillIsFull() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("| Attack                      |  100 |  85 | 100 |       (n/a) |\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		plugin.assertPluginPrints("Goal attack: full", 1);

	}

	@Test
	public void testTrainOutput() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();
		plugin.receiveText("You now have 'Attack' at 100% without special bonuses.\n");
		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");
		plugin.assertPluginPrints("Goal attack: full", 1);
	}

	@Test
	public void testGuildinfo() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();

		plugin.trigger("train");
		plugin.receiveText("| Skills available at level  1  | Cur | Rac | Max | Exp         |");
		plugin.receiveText("| Attack                        |   0 |  85 | 10  |       22015 |");

		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

		plugin.assertPluginPrints(
				"Goal attack: 80 You have enough to advance in: ranger, barbarian",
				1);
	}

	@Test
	public void testGuildinfoCanAdvanceInOneOfGuilds() throws Exception {
		MockGoalCommandPlugin plugin = initiatePlugin();

		plugin.trigger("train");
		plugin.receiveText("| Skills available at level  1  | Cur | Rac | Max | Exp         |");
		// Attack at max in barbarians but this will still allow improvement in
		// Rangers
		plugin.receiveText("| Attack                        |   10 |  85 | 10  |       22015 |");

		plugin.trigger("goal attack");
		plugin.receiveText("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

		plugin.assertPluginPrints(
				"Goal attack: 203 You have enough to advance in: ranger", 1);
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

		plugin.receiveText("| Attack                      |  1 |  85 | 100 |       22015 |");

		plugin.trigger("ranger info");
		plugin.receiveText("Name: Rangers");
		plugin.receiveText("Command: ranger");
		plugin.receiveText("Creators: Duke");
		plugin.receiveText("Your level: 2");
		plugin.receiveText("Maximum level: 35");

		plugin.receiveText("Description:");
		plugin.receiveText("The mighty barbarian guild is a loosely run group of battle hardened warriors.");
		plugin.receiveText(" Through intense training in the wilds, members become both mentally and");
		plugin.receiveText("physically tough.  These ferocious warriors excel at many combat skills.");

		plugin.receiveText("Joining requirements:");
		plugin.receiveText(" Background must be nomad (passed)");
		plugin.receiveText("Abilities gained when joining:");
		plugin.receiveText(" In the name of Groo wear the shrunken skull necklace with pride.");
		plugin.receiveText(" May train skill Attack to 20%");
		plugin.receiveText(" May train skill Push to 40%");
		plugin.receiveText(" May train skill Alcohol tolerance to 3%");
		plugin.receiveText(" May train skill Consider to 10%");
		plugin.receiveText(" May train skill Hunting to 20%");
		plugin.receiveText(" May train skill Fishing to 20%");
		plugin.receiveText(" May train skill Torch creation to 15%");
		plugin.receiveText(" May train skill Looting and burning to 30%");
		plugin.receiveText(" May train skill Vandalism to 10%");
		plugin.receiveText(" May train skill Axes to 10%");

		plugin.receiveText("Abilities and requirements at each level:");
		plugin.receiveText(" Level 2:");
		plugin.receiveText("  Abilities:");
		plugin.receiveText("   May train skill Attack to 57%");
		plugin.receiveText("   May train skill Push to 100%");
		plugin.receiveText("   May train skill Bash to 10%");

		plugin.trigger("barbarian info");
		plugin.receiveText("Name: Barbarian Guild");
		plugin.receiveText("Command: barbarian");
		plugin.receiveText("Creators: Duke");
		plugin.receiveText("Your level: 1");
		plugin.receiveText("Maximum level: 35");

		plugin.receiveText("Description:");
		plugin.receiveText("The mighty barbarian guild is a loosely run group of battle hardened warriors.");
		plugin.receiveText(" Through intense training in the wilds, members become both mentally and");
		plugin.receiveText("physically tough.  These ferocious warriors excel at many combat skills.");

		plugin.receiveText("Joining requirements:");
		plugin.receiveText(" Background must be nomad (passed)");
		plugin.receiveText("Abilities gained when joining:");
		plugin.receiveText(" In the name of Groo wear the shrunken skull necklace with pride.");
		plugin.receiveText(" May train skill Attack to 10%");
		plugin.receiveText(" May train skill Push to 40%");
		plugin.receiveText(" May train skill Alcohol tolerance to 3%");
		plugin.receiveText(" May train skill Consider to 10%");
		plugin.receiveText(" May train skill Hunting to 20%");
		plugin.receiveText(" May train skill Fishing to 20%");
		plugin.receiveText(" May train skill Torch creation to 15%");
		plugin.receiveText(" May train skill Looting and burning to 30%");
		plugin.receiveText(" May train skill Vandalism to 10%");
		plugin.receiveText(" May train skill Axes to 10%");

		plugin.receiveText("Abilities and requirements at each level:");
		plugin.receiveText(" Level 2:");
		plugin.receiveText("  Abilities:");
		plugin.receiveText("   May train skill Attack to 57%");
		plugin.receiveText("   May train skill Push to 100%");
		plugin.receiveText("   May train skill Bash to 10%");

		return plugin;
	}

	private static class MockGoalCommandPlugin extends BatMUDGoalsPlugin {
		List<String> prints = new ArrayList<String>();

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
			trigger(new ParsedResult(String.format("%s\n", text)));
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
