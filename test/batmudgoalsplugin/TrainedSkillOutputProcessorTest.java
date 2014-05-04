package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class TrainedSkillOutputProcessorTest {

	@Test
	public void testImproveAttack() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		TrainedSkillOutputProcessor op = new TrainedSkillOutputProcessor(data);
		op.receive("This costs you 4830 experience points.");
		op.receive("Studied total 1% of the spell.");
		op.receive("You now have 'Attack' at 100% without special bonuses.");
		op.receive("With current bonuses it is at 151%. Current maximum without bonuses is 100%.");

		assertEquals(100, data.getCurrentSkillStatus("attack"));
	}

	@Test
	public void testImproveCastGeneric() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		TrainedSkillOutputProcessor op = new TrainedSkillOutputProcessor(data);
		op.receive("This costs you 4830 experience points.");
		op.receive("Studied total 1% of the spell.");
		op.receive("You now have 'Cast Generic' at 51% without special bonuses.");
		op.receive("With current bonuses it is at 51%. Current maximum without bonuses is 100%.");

		assertEquals(51, data.getCurrentSkillStatus("cast generic"));
	}

	@Test
	public void testImproveMakeScar() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		TrainedSkillOutputProcessor op = new TrainedSkillOutputProcessor(data);
		op.receive("This costs you 4830 experience points.");
		op.receive("Studied total 1% of the spell.");
		op.receive("You now have 'Make scar' at 51% without special bonuses.");
		op.receive("With current bonuses it is at 51%. Current maximum without bonuses is 100%.");

		assertEquals(51, data.getCurrentSkillStatus("make scar"));

	}

}
