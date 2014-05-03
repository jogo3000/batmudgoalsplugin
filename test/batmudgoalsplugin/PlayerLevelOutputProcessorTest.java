package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class PlayerLevelOutputProcessorTest {

	@Test
	public void testLevel1() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		PlayerLevelOutputProcessor op = new PlayerLevelOutputProcessor(data);
		op.setGuild("ranger");
		op.receive("Your level: 1");
		assertEquals(1, data.getGuildLevel("ranger"));
	}
}
