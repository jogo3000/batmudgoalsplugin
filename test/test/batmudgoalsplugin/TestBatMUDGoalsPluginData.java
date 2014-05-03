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

	@Test
	public void testIsGoalSkillFalse() throws Exception {
		BatMUDGoalsPluginData d = new BatMUDGoalsPluginData();
		d.setGoalSkill("attack");
		assertFalse(d.isGoalSkill("looting and burning"));
	}

	@Test
	public void testIsGoalSkillTrue() throws Exception {
		BatMUDGoalsPluginData d = new BatMUDGoalsPluginData();
		d.setGoalSkill("attack");
		assertTrue(d.isGoalSkill("attack"));
	}

	@Test
	public void testGoalSkillIs100False() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 1);
		assertFalse(data.isGoalSkillMaxed());
	}

	@Test
	public void testGoalSkillIs100True() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 100);
		assertTrue(data.isGoalSkillMaxed());
	}

}
