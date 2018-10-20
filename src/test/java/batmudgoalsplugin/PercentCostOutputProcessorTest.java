package batmudgoalsplugin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class PercentCostOutputProcessorTest {

    @Test
    public void testProcessPercentCosts() {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        PercentCostOutputProcessor op = new PercentCostOutputProcessor(data);
        op.setSkill("cast generic");
        op.receive("|    1% =            80  |   51% =          9046  |");
        op.setSkill("cast teleportation");
        op.receive("|    15% =            840  |   56% =          19046  |");

        assertEquals(80, data.getSkillCost("cast generic", 1));
        assertEquals(9046, data.getSkillCost("cast generic", 51));
        assertEquals(840, data.getSkillCost("cast teleportation", 15));
        assertEquals(19046, data.getSkillCost("cast teleportation", 56));
    }
}
