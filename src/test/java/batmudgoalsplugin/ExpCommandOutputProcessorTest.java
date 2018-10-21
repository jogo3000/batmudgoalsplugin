package batmudgoalsplugin;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    private ExpCommandOutputProcessor op;

    @BeforeEach
    public void initOutputProcessor() {
        op = new ExpCommandOutputProcessor(guiModel, data);
    }

    @Test
    @DisplayName("Given player has no goal, when player uses 'exp' then nothing is printed")
    public void testNoGoal() throws Exception {
        op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

        verify(guiModel, never()).printMessage(anyString());
    }

    @Nested
    @DisplayName("Given player's goal is 'attack'")
    class GivenPlayersGoalIsAttack {

        @BeforeEach
        public void playersGoalIsAttack() {
            data.setGoalSkill("attack");
        }

        @Test
        @DisplayName("and goal skill is at 100%, when player uses 'exp' command then they are informed that their goal skill is full")
        public void testGoalIsFull() {
            data.setSkillStatus("attack", 100);

            op.receive("Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0");

            assertPrints("Goal attack: full");
        }

        private void assertPrints(String expected) {
            verify(guiModel).printMessage(expected);
        }

        @Test
        @DisplayName("given none of player's guilds offer more of their goal skill they are informed that none of their guilds offer more")
        public void testGoalSkillNotFullNoGuildsOfferMore() throws Exception {
            data.setSkillStatus("attack", 60);
            data.setGuildLevel("tzarakk", 20);
            data.setSkillMaxInfo("tzarakk", "attack", 20, 60);

            op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

            assertPrints("None of your guilds offer more attack");
        }

        @Nested
        @DisplayName("And goal skill is at 1%")
        class AndGoalAt1Percent {

            @BeforeEach
            public void goalSkillAt1Percent() {
                data.setSkillStatus("attack", 1);
            }

            @Test
            @DisplayName("and player hasn't got enough exp to train it, they are informed how much they still need to train")
            public void testNotEnoughExp() throws Exception {
                data.setSkillCostForLevel("attack", 2, 200);
                data.setGuildLevel("tzarakk", 1);
                data.setSkillMaxInfo("tzarakk", "attack", 1, 12);

                op.receive("Exp: 13 Money: 0.00 Bank: 644404.00 Exp pool: 0");

                assertPrints(String.format("Goal attack: 200 You need: %d", 200 - 13));
            }

            @Test
            @DisplayName("and player has enough exp to train it in 'tzrakk' guild, they are informed they have enough to advance in 'tzarakk'")
            public void testEnoughExp() throws Exception {
                data.setSkillCostForLevel("attack", 2, 200);
                data.setGuildLevel("tzarakk", 1);
                data.setSkillMaxInfo("tzarakk", "attack", 1, 12);

                op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

                assertPrints("Goal attack: 200 You have enough to advance in: tzarakk");
            }

            @Test
            @DisplayName("and player has enough exp to train it in 'tarmalen' and 'tzarakk' guilds, they are informed they have enough to advance in 'tarmalen' and 'tzarakk'")
            public void testEnoughExpToAdvanceInMultipleGuilds() throws Exception {
                data.setSkillCostForLevel("attack", 2, 200);
                data.setGuildLevel("tzarakk", 1);
                data.setSkillMaxInfo("tzarakk", "attack", 1, 12);
                data.setGuildLevel("tarmalen", 12);
                data.setSkillMaxInfo("tarmalen", "attack", 1, 12);
                data.setGuildLevel("barbarian", 1);
                data.setSkillMaxInfo("tarmalen", "attack", 1, 1);

                op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

                assertPrints("Goal attack: 200 You have enough to advance in: tarmalen, tzarakk");
            }

            @Test
            @DisplayName("player has enough exp and not one of their guilds offer more 'attack' at their guild level they are informed they  need to level up in their guild")
            public void testGoalNeedsLevel() throws Exception {
                data.setSkillCostForLevel("attack", 2, 200);
                data.setGuildLevel("tzarakk", 1);
                data.setSkillMaxInfo("tzarakk", "attack", 12, 12);
                data.setGuildLevel("tarmalen", 12);
                data.setSkillMaxInfo("tarmalen", "attack", 13, 12);
                data.setGuildLevel("barbarian", 1);
                data.setSkillMaxInfo("tarmalen", "attack", 1, 1);

                op.receive("Exp: 1300 Money: 0.00 Bank: 644404.00 Exp pool: 0");

                assertPrints("Goal attack: needs level");
            }
        }

    }

}