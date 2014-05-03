package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class TrainedSkillOutputProcessorTest {

	@Test
	public void testImproveAttack() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		new TrainedSkillOutputProcessor(data)
				.receive("You now have 'Attack' at 100% without special bonuses.\n");
		assertEquals(100, data.getCurrentSkillStatus("attack"));
	}

	@Test
	public void testImproveCastGeneric() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		new TrainedSkillOutputProcessor(data)
				.receive("You now have 'Cast Generic' at 51% without special bonuses.\n");
		assertEquals(51, data.getCurrentSkillStatus("cast generic"));
	}

}
