package batmudgoalsplugin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

@ExtendWith(MockitoExtension.class)
public class ExpCommandOutputProcessorTest {

    @Mock
    ClientGUIModel guiModel;
    BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();

    /**
     * Should print 'Goal <skill>: full'
     */
    @Test
    public void testGoalIsFull() {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 100);
        op.receive("Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic", String.format("Goal attack: full%n"));
    }

    /**
     * Should print amount needed to improve the skill
     * 
     * @throws Exception
     */
    @Test
    public void testNotEnoughExp() throws Exception {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 1);
        data.setSkillCost("attack", 2, 200);
        data.setGuildLevel("tzarakk", 1);
        data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
        op.receive("Exp: 13 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic", String.format("Goal attack: 200 You need: %d%n", 200 - 13));
    }

    /**
     * Should print list of guilds where can advance the skill. In this case only
     * one guild is provided.
     * 
     * @throws Exception
     */
    @Test
    public void testEnoughExp() throws Exception {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 1);
        data.setSkillCost("attack", 2, 200);
        data.setGuildLevel("tzarakk", 1);
        data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic",
                String.format("Goal attack: 200 You have enough to advance in: tzarakk%n"));
    }

    /**
     * Should print list of guilds where can advance the skill
     * 
     * @throws Exception
     */
    @Test
    public void testEnoughExpToAdvanceInMultipleGuilds() throws Exception {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 1);
        data.setSkillCost("attack", 2, 200);
        data.setGuildLevel("tzarakk", 1);
        data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
        data.setGuildLevel("tarmalen", 12);
        data.setSkillMaxInfo("tarmalen", "attack", 1, 12);
        data.setGuildLevel("barbarian", 1);
        data.setSkillMaxInfo("tarmalen", "attack", 1, 1);
        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic",
                String.format("Goal attack: 200 You have enough to advance in: tarmalen, tzarakk%n"));
    }

    /**
     * Should print that player needs levels
     * 
     * @throws Exception
     */
    @Test
    public void testGoalNeedsLevel() throws Exception {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 1);
        data.setSkillCost("attack", 2, 200);
        data.setGuildLevel("tzarakk", 1);
        data.setSkillMaxInfo("tzarakk", "attack", 12, 12);
        data.setGuildLevel("tarmalen", 12);
        data.setSkillMaxInfo("tarmalen", "attack", 13, 12);
        data.setGuildLevel("barbarian", 1);
        data.setSkillMaxInfo("tarmalen", "attack", 1, 1);
        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic", String.format("Goal attack: needs level%n"));

    }

    /**
     * Goal is not set, should not print anything
     * 
     * @throws Exception
     */
    @Test
    public void testNoGoal() throws Exception {
        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);

        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");
        verify(guiModel, never()).printText(anyString(), anyString());
    }

    @Test
    public void testGoalSkillNotFullNoGuildsOfferMore() throws Exception {

        data.setGoalSkill("attack");
        data.setSkillStatus("attack", 60);
        data.setGuildLevel("tzarakk", 20);
        data.setSkillMaxInfo("tzarakk", "attack", 20, 60);

        ExpCommandOutputProcessor op = new ExpCommandOutputProcessor(guiModel, data);
        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel).printText("generic", String.format("None of your guilds offer more attack%n"));
    }

}
