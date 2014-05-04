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
		op.receive("| Cost of training Looting and burning            |");
		op.receive("|-------------------------------------------------|");
		op.receive("| Percent     Exp        | Percent     Exp        |");
		op.receive("|=================================================|");
		op.receive("|    1% =            24  |   51% =          2444  |");
		op.receive("|    2% =            25  |   52% =          2598  |");
		op.receive("|    3% =            27  |   53% =          2761  |");

		verify(mock).setSkill("looting and burning");
	}

	@Test
	public void testCostOfStudyingOutput() throws Exception {
		PercentCostOutputProcessor mock = mock(PercentCostOutputProcessor.class);
		CostOfTrainingSkillNameOutputProcessor op = new CostOfTrainingSkillNameOutputProcessor(
				mock, null);
		op.receive(",-------------------------------------------------.");
		op.receive("| Cost of studying Cure light wounds              |");
		op.receive("|-------------------------------------------------|");
		op.receive("| Percent     Exp        | Percent     Exp        |");
		op.receive("|=================================================|");
		op.receive("|    1% =            24  |   51% =          2444  |");
		op.receive("|    2% =            25  |   52% =          2598  |");
		op.receive("|    3% =            27  |   53% =          2761  |");

		verify(mock).setSkill("cure light wounds");
	}

}
