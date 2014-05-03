package test.batmudgoalsplugin;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class TestBatMUDGoalsPluginData {

	@Test
	public void testIsGoalSetFalse() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		assertFalse(data.isGoalSet());
	}

	@Test
	public void testIsGoalSetTrue() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		assertTrue(data.isGoalSet());
	}

}
