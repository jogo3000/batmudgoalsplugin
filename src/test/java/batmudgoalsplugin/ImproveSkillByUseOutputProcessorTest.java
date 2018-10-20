package batmudgoalsplugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.jupiter.api.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class ImproveSkillByUseOutputProcessorTest {

    @Test
    public void testIgnoresRandomOutput() throws Exception {
        BatMUDGoalsPluginData data = mock(BatMUDGoalsPluginData.class);
        ImproveSkillByUseOutputProcessor op = new ImproveSkillByUseOutputProcessor(data);

        op.receive("foo");
        op.receive("Player [chat] You feel like you just got slightly better in Attack");

        verifyZeroInteractions(data);
    }

    /**
     * Test that improved skill and only improved skill gets better
     */
    @Test
    public void testImproveByUse() {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setSkillStatus("attack", 1);
        data.setSkillStatus("cast generic", 11);
        ImproveSkillByUseOutputProcessor op = new ImproveSkillByUseOutputProcessor(data);

        op.receive("You feel like you just got slightly better in Attack");

        assertEquals(2, data.getCurrentSkillStatus("attack"));
        assertEquals(11, data.getCurrentSkillStatus("cast generic"));
    }

    /**
     * Make sure skill names with multiple words are supported
     * 
     * @throws Exception
     */
    @Test
    public void testLootingAndBurningGotBetter() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setSkillStatus("looting and burning", 21);
        ImproveSkillByUseOutputProcessor op = new ImproveSkillByUseOutputProcessor(data);

        op.receive("You feel like you just got slightly better in Looting and burning");

        assertEquals(22, data.getCurrentSkillStatus("looting and burning"));
    }

}
