package batmudgoalsplugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GoalCommandWithoutParametersProcessorTest {

    @Mock
    private ClientGUIModel model;
    private GoalCommandWithoutParametersProcessor op;
    private BatMUDGoalsPluginData data;

    @BeforeEach
    public void setup() {
        data = new BatMUDGoalsPluginData();
        op = new GoalCommandWithoutParametersProcessor(model, data);
    }

    private void whenGoalCalled() {
        op.receive("goal");

    }

    public void testGoalCommandListsAvailableGoalSkills_noCommands() {
        whenGoalCalled();

        Mockito.verify(model).printMessage("No data.");
    }

    @Test
    public void testGoalCommandListsAvailableGoalSkills() throws Exception {
        data.setSkillCostForLevel("attack", 1, 1);
        whenGoalCalled();

        Mockito.verify(model).printMessage("attack");
    }

    @Test
    public void testGoalCommandListsMultipleAvailableGoalSkills() throws Exception {
        data.setSkillCostForLevel("attack", 1, 1);
        data.setSkillCostForLevel("brawling", 1, 1);
        whenGoalCalled();

        Mockito.verify(model).printMessage("attack");
        Mockito.verify(model).printMessage("brawling");
    }

    @Test
    public void testGoalCommandListsMultipleAvailableGoalSkillsAndMarksGoal() throws Exception {
        data.setSkillCostForLevel("attack", 1, 1);
        data.setSkillCostForLevel("brawling", 1, 1);
        data.setGoalSkill("brawling");
        whenGoalCalled();

        Mockito.verify(model).printMessage("attack");
        Mockito.verify(model).printMessage("brawling (*)");
    }
}
