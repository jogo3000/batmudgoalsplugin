package batmudgoalsplugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

@ExtendWith(MockitoExtension.class)
public class GoalCommandProcessorTest {

    @Mock
    ClientGUIModel model;

    protected ClientGUIModel verifyOutput(BatMUDGoalsPluginData data, String input) {
        assertNotNull(model);
        new GoalCommandProcessor(model, data).receive(input);
        ClientGUIModel verify = verify(model);
        return verify;
    }

    @Test
    public void testSkillNotInLibrary() {
        verifyOutput(new BatMUDGoalsPluginData(), "goal vandalism").printText("generic",
                String.format("vandalism not in library%n"));
    }

    @Test
    public void testSkillInLibrary() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setSkillCost("torch creation", 1, 1);
        verifyOutput(data, "goal torch creation").printText("generic", String.format("Next goal: torch creation%n"));
    }

    @Test
    public void testSkillInLibrary_MixedCaseAndWhiteSpace() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setSkillCost("torch creation", 1, 1);
        verifyOutput(data, "goal torch   Creation").printText("generic", String.format("Next goal: torch creation%n"));
    }

    @Test
    public void testInvalidParameterShouldNotClearGoal() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.setGoalSkill("attack");
        GoalCommandProcessor cp = new GoalCommandProcessor(model, data);

        cp.receive("goal foo");
        assertEquals("attack", data.getGoalSkill());
    }

    @Test
    void testPlainGoalCommand() throws Exception {
        new GoalCommandProcessor(model, new BatMUDGoalsPluginData()).receive("goal");
    }
}
