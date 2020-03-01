package batmudgoalsplugin;

import static org.mockito.Mockito.verify;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mythicscape.batclient.interfaces.ParsedResult;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Integration test for the plugin. Uses all of the command processors
 * set up in {@link BatMUDGoalsController} in concert.
 */
@ExtendWith(MockitoExtension.class)
public class BatMUDGoalsControllerTest {

    @Mock
    private ClientGUIModel guiModel;

    private BatMUDGoalsController goalsModel;

    @BeforeEach
    public void setup() throws Exception {
        Logger log = Logger.getLogger(getClass().toString());
        log.setLevel(Level.OFF);
        goalsModel = new BatMUDGoalsController(log, new BatMUDGoalsPluginData(), guiModel);

        givenPlayerListsCostOfTrainingAttack();
        givenPlayersAttackSkillAtOnePercent();
        givenPlayerListsRangerInfo();
        givenPlayerListsBarbarianInfo();

    }

    private void batmudPrints(String... strings) {
        Arrays.stream(strings)
            .map(ParsedResult::new)
            .forEach(goalsModel::trigger);
    }

    private void givenPlayersAttackSkillAtOnePercent() {
        batmudPrints("| Attack                      |  1 |  85 | 100 |       22015 |\n");
    }

    private void givenPlayerListsBarbarianInfo() {
        userTypes("barbarian info");

        batmudPrints("Name: Barbarian Guild\n",
                     "Command: barbarian\n",
                     "Creators: Duke\n",
                     "Your level: 1\n",
                     "Maximum level: 35\n",

                     "Description:\n",
                     "The mighty barbarian guild is a loosely run group of battle hardened warriors.\n",
                     " Through intense training in the wilds, members become both mentally and\n",
                     "physically tough.  These ferocious warriors excel at many combat skills.\n",

                     "Joining requirements:\n",
                     " Background must be nomad (passed)\n",
                     "Abilities gained when joining:\n",
                     " In the name of Groo wear the shrunken skull necklace with pride.\n",
                     " May train skill Attack to 10%\n",
                     " May train skill Push to 40%\n",
                     " May train skill Alcohol tolerance to 3%\n",
                     " May train skill Consider to 10%\n",
                     " May train skill Hunting to 20%\n",
                     " May train skill Fishing to 20%\n",
                     " May train skill Torch creation to 15%\n",
                     " May train skill Looting and burning to 30%\n",
                     " May train skill Vandalism to 10%\n",
                     " May train skill Axes to 10%\n",

                     "Abilities and requirements at each level:\n",
                     " Level 2:\n",
                     "  Abilities:\n",
                     "   May train skill Attack to 57%\n",
                     "   May train skill Push to 100%\n",
                     "   May train skill Bash to 10%\n");
    }

    private void userTypes(String input) {
        goalsModel.trigger(input);
    }

    private void givenPlayerListsRangerInfo() {
        userTypes("ranger info");

        batmudPrints("Name: Rangers\n",
                     "Command: ranger\n",
                     "Creators: Duke\n",
                     "Your level: 2\n",
                     "Maximum level: 35\n",

                     "Description:\n",
                     "The mighty barbarian guild is a loosely run group of battle hardened warriors.\n",
                     " Through intense training in the wilds, members become both mentally and\n",
                     "physically tough.  These ferocious warriors excel at many combat skills.\n",

                     "Joining requirements:\n",
                     " Background must be nomad (passed)\n",
                     "Abilities gained when joining:\n",
                     " In the name of Groo wear the shrunken skull necklace with pride.\n",
                     " May train skill Attack to 20%\n",
                     " May train skill Push to 40%\n",
                     " May train skill Alcohol tolerance to 3%\n",
                     " May train skill Consider to 10%\n",
                     " May train skill Hunting to 20%\n",
                     " May train skill Fishing to 20%\n",
                     " May train skill Torch creation to 15%\n",
                     " May train skill Looting and burning to 30%\n",
                     " May train skill Vandalism to 10%\n",
                     " May train skill Axes to 10%\n",

                     "Abilities and requirements at each level:\n",
                     " Level 2:\n",
                     "  Abilities:\n",
                     "   May train skill Attack to 57%\n",
                     "   May train skill Push to 100%\n",
                     "   May train skill Bash to 10%\n",

                     "Abilities and requirements at each level:\n",
                     " Level 3:\n",
                     "  Abilities:\n",
                     "   May train skill Attack to 90%\n",
                     "   May train skill Push to 100%\n",
                     "   May train skill Bash to 10%\n");
    }

    private void givenPlayerListsCostOfTrainingAttack() {
        batmudPrints(",-------------------------------------------------.\n",
                     "| Cost of training Attack                         |\n",
                     "|-------------------------------------------------|\n",
                     "| Percent     Exp        | Percent     Exp        |\n",
                     "|=================================================|\n",
                     "|    1% =            80  |   51% =          9046  |\n",
                     "|    2% =            82  |   52% =          9700  |\n",
                     "|    3% =            86  |   53% =         10395  |\n",
                     "|    4% =            91  |   54% =         11135  |\n",
                     "|    5% =            99  |   55% =         11921  |\n",
                     "|    6% =           109  |   56% =         12756  |\n",
                     "|    7% =           121  |   57% =         13642  |\n",
                     "|    8% =           137  |   58% =         14584  |\n",
                     "|    9% =           155  |   59% =         15583  |\n",
                     "|   10% =           177  |   60% =         16643  |\n",
                     "|   11% =           203  |   86% =         17768  |\n",
                     "|   1% to 86% =         200000000  |\n");
    }

    @Test
    @DisplayName("When player sets goal to a skill which the plugin has no information about, the player gets an error message")
    public void testSkillNotInLibrary() throws Exception {
        userTypes("goal looting and burning");

        verifyPrint("looting and burning not in library");
    }

    private void verifyPrint(String expected) {
        verify(guiModel).printMessage(expected);
    }

    @Test
    @DisplayName("When player sets goal to a skill in the library, the player gets a verification message")
    public void testSetGoalSuccesfully() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        verifyPrint("Next goal: attack");
    }

    @Test
    @DisplayName("When given player has set their goal to attack and uses goal command without parameters, available goals are listed with attack marked as the active goal")
    public void testGoalSetListGoals() throws Exception {
        givenListsCostOfTrainingLootingAndBurning();
        givenPlayerSetsTheirGoalToAttack();

        userTypes("goal");

        verifyPrint("attack (*)");
        verifyPrint("looting and burning");
    }

    private void givenPlayerSetsTheirGoalToAttack() {
        userTypes("goal attack");
    }

    private void givenListsCostOfTrainingLootingAndBurning() {
        batmudPrints(",-------------------------------------------------.\n",
                     "| Cost of training Looting and burning                         |\n",
                     "|-------------------------------------------------|\n",
                     "| Percent     Exp        | Percent     Exp        |\n",
                     "|=================================================|\n",
                     "|    1% =            80  |   51% =          9046  |\n",
                     "|    2% =            82  |   52% =          9700  |\n",
                     "|    3% =            86  |   53% =         10395  |\n");
    }

    @Test
    @DisplayName("Given the player has not enough exp to train their goal skill, when player uses 'exp' command they are informed how much they need to train")
    public void testParseCostTrain() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 2 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: 82 You need: " + Integer.toString(82 - 2));
    }

    @Test
    public void testExpOutputWithZeroValues() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 2 Money: 0 Bank: 0 Exp pool: 0\n");

        verifyPrint("Goal attack: 82 You need: " + Integer.toString(82 - 2));
    }

    @Test
    @DisplayName("Given the player has enough experience to train their goal skill, when player uses 'exp' command they are informed they can train and in which guilds they can do so")
    public void testEnoughExp() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 100000 Money: 0 Bank: 0 Exp pool: 0\n");

        verifyPrint("Next goal: attack");
        verifyPrint("Goal attack: 82 You have enough to advance in: barbarian, ranger");
    }

    @Test
    @DisplayName("Given player's goal skill is at the max at their level, when player uses 'exp' command they are informed they need another level")
    public void testNeedAnotherLevel() throws Exception {
        batmudPrints("| Attack                      |  85 |  85 | 85 |       22015 |\n");
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: needs level");
    }

    @Test
    @DisplayName("Given player's goal skill is at max, when player uses 'exp' command they are informed the goal is full")
    public void testSkillIsFull() throws Exception {
        batmudPrints("| Attack                      |  100 |  85 | 100 |       (n/a) |\n");
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: full");
    }

    @Test
    @DisplayName("Given player trains the skill to max, when player uses 'exp' command they are informed the goal is full")
    public void testTrainOutput() throws Exception {
        givenPlayerTrainsAttackToMax();
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: full");
    }

    private void givenPlayerTrainsAttackToMax() {
        batmudPrints("You now have 'Attack' at 100% without special bonuses.\n");
    }

    @Test
    public void testGuildinfo() throws Exception {
        userTypes("train");
        batmudPrints("| Skills available at level  1  | Cur | Rac | Max | Exp         |",
                     "| Attack                        |   0 |  85 | 10  |       22015 |");

        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Next goal: attack");
        verifyPrint("Goal attack: 80 You have enough to advance in: barbarian, ranger");
    }

    @Test
    public void testGuildinfoCanAdvanceInOneOfGuilds() throws Exception {
        userTypes("train");
        // Attack at max in barbarians but this will still allow improvement in
        // Rangers
        batmudPrints("| Skills available at level  1  | Cur | Rac | Max | Exp         |",
                     "| Attack                        |   10 |  85 | 10  |       22015 |");
        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: 203 You have enough to advance in: ranger");
    }
}
