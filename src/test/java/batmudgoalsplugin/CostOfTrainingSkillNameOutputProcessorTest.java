package batmudgoalsplugin;

import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CostOfTrainingSkillNameOutputProcessorTest {
    @Mock
    PercentCostOutputProcessor percentCostOutputProcessor;
    @InjectMocks
    CostOfTrainingSkillNameOutputProcessor op;

    private void batmudPrints(String... strings) {
        Arrays.stream(strings)
            .forEach(op::receive);
    }

    @Test
    @DisplayName("Given user lists cost of training Looting and burning, PercentCostOutputProcessor is set to 'looting and burning'")
    public void test() {
        batmudPrints(",-------------------------------------------------.",
                     "| Cost of training Looting and burning            |",
                     "|-------------------------------------------------|",
                     "| Percent     Exp        | Percent     Exp        |",
                     "|=================================================|",
                     "|    1% =            24  |   51% =          2444  |",
                     "|    2% =            25  |   52% =          2598  |",
                     "|    3% =            27  |   53% =          2761  |");

        verify(percentCostOutputProcessor).setSkill("looting and burning");
    }

    @Test
    @DisplayName("Given user lists cost of studying Cure light wounds, PercentCostOutputProcessor is set to 'cure light wounds'")
    public void testCostOfStudyingOutput() throws Exception {
        batmudPrints(",-------------------------------------------------.",
                     "| Cost of studying Cure light wounds              |",
                     "|-------------------------------------------------|",
                     "| Percent     Exp        | Percent     Exp        |",
                     "|=================================================|",
                     "|    1% =            24  |   51% =          2444  |",
                     "|    2% =            25  |   52% =          2598  |",
                     "|    3% =            27  |   53% =          2761  |");

        verify(percentCostOutputProcessor).setSkill("cure light wounds");
    }

}
