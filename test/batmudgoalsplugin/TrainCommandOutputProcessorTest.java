package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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

}
