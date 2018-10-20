package batmudgoalsplugin.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
        data.setSkillCostForLevel("brawling", 1, 1011);
        assertEquals(1011, data.getImproveGoalSkillCost());
    }

    /**
     * Player can use 300k to train or study partially. Each partial train lessens
     * the cost of next percent by 250k.
     * 
     * @throws Exception
     */
    @Test
    public void testTrainPartially() throws Exception {
        int skillCost = 600000;

        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setGoalSkill("attack");
        data.setSkillCostForLevel("attack", 10, skillCost);
        data.setSkillStatus("attack", 9);
        assertEquals(skillCost, data.getImproveGoalSkillCost());
        data.trainPartially("attack");
        assertEquals(skillCost - 250000, data.getImproveGoalSkillCost());
        data.trainPartially("attack");
        assertEquals(skillCost - 2 * 250000, data.getImproveGoalSkillCost());
    }

    @Test
    public void testClearPartialTrains() throws Exception {
        int skillCost = 60000;
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setGoalSkill("attack");
        data.setSkillCostForLevel("attack", 10, skillCost);
        data.setSkillStatus("attack", 9);
        assertEquals(skillCost, data.getImproveGoalSkillCost());
        data.trainPartially("attack");
        assertEquals(skillCost - 250000, data.getImproveGoalSkillCost());
        data.clearPartialTrains("attack");
        assertEquals(skillCost, data.getImproveGoalSkillCost());
    }

}
