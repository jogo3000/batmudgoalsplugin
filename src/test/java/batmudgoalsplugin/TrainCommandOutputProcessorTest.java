package batmudgoalsplugin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

public class TrainCommandOutputProcessorTest {

    @Test
    public void testTrainCommandOutput() {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        TrainCommandOutputProcessor op = new TrainCommandOutputProcessor(data);
        op.receive("| Attack                        |   0 |  85 | 10  |       22015 |");
        op.receive("| Brawling 						|  15 |  85 | 10  |       (n/a) |");
        assertEquals(0, data.getCurrentSkillStatus("attack"));
        assertEquals(15, data.getCurrentSkillStatus("brawling"));
    }

    @Test
    public void testStudyCommandOutput() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        TrainCommandOutputProcessor op = new TrainCommandOutputProcessor(data);

        op.receive(",-------------------------------+-----------------+-------------|");
        op.receive("| Spells available at level 35  | Cur | Rac | Max | Exp         |");
        op.receive("|===============================|=====|=====|=====|=============|");
        op.receive("| Cure light wounds             |  85 | 101 |  95 |       16940 |");
        op.receive("| Detect alignment              |  50 | 101 | 100 |        2444 |");
        op.receive("| Paranoia                      |   1 | 101 | 100 |          25 |");

        assertEquals(85, data.getCurrentSkillStatus("cure light wounds"));
        assertEquals(50, data.getCurrentSkillStatus("detect alignment"));
        assertEquals(1, data.getCurrentSkillStatus("paranoia"));
    }

    @Test
    public void testTrainCommandOutputWithPartialTrains() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        TrainCommandOutputProcessor op = new TrainCommandOutputProcessor(data);
        op.receive("| Bladed fury | 93 | 85 | 100 | 968645 | (partially trained)");
        assertEquals(93, data.getCurrentSkillStatus("bladed fury"));
    }

    @Test
    public void testTrainCommandOutputWithPartialStudy() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        TrainCommandOutputProcessor op = new TrainCommandOutputProcessor(data);
        op.receive("| Cure critical wounds | 93 | 85 | 100 | 968645 | (partially studied)");
        assertEquals(93, data.getCurrentSkillStatus("cure critical wounds"));
    }

}
