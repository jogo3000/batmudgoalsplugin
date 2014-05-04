package batmudgoalsplugin.data;

import static org.junit.Assert.assertEquals;
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

	@Test
	public void testGetGoalPercent() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 87);
		assertEquals(88, data.getGoalPercent());
	}

	@Test
	public void testGetGoalPercentFull() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("attack");
		data.setSkillStatus("attack", 100);
		assertEquals(101, data.getGoalPercent());
	}

	@Test
	public void testImproveGoalSkillCost() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		data.setGoalSkill("brawling");
		data.setSkillStatus("brawling", 0);
		data.setSkillCost("brawling", 1, 1011);
		assertEquals(1011, data.getImproveGoalSkillCost());
	}

}
