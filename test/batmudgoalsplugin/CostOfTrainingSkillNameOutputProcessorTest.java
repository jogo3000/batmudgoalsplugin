package batmudgoalsplugin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class CostOfTrainingSkillNameOutputProcessorTest {

	@Test
	public void test() {
		PercentCostOutputProcessor mock = mock(PercentCostOutputProcessor.class);
		CostOfTrainingSkillNameOutputProcessor op = new CostOfTrainingSkillNameOutputProcessor(
				mock, null);
		op.receive(",-------------------------------------------------.");
		op.receive("| Cost of training Looting and burning                         |\n");

		verify(mock).setSkill("looting and burning");
	}

}
