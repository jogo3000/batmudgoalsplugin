package batmudgoalsplugin;

import static org.mockito.Mockito.verify;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mythicscape.batclient.interfaces.ParsedResult;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

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

    private void givenPlayersAttackSkillAtOnePercent() {
        batmudPrints("| Attack                      |  1 |  85 | 100 |       22015 |\n");
    }

    private void batmudPrints(String string) {
        goalsModel.trigger(new ParsedResult(string));
    }

    private void givenPlayerListsBarbarianInfo() {
        userTypes("barbarian info");
        batmudPrints("Name: Barbarian Guild\n");
        batmudPrints("Command: barbarian\n");
        batmudPrints("Creators: Duke\n");
        batmudPrints("Your level: 1\n");
        batmudPrints("Maximum level: 35\n");

        batmudPrints("Description:\n");
        batmudPrints("The mighty barbarian guild is a loosely run group of battle hardened warriors.\n");
        batmudPrints(" Through intense training in the wilds, members become both mentally and\n");
        batmudPrints("physically tough.  These ferocious warriors excel at many combat skills.\n");

        batmudPrints("Joining requirements:\n");
        batmudPrints(" Background must be nomad (passed)\n");
        batmudPrints("Abilities gained when joining:\n");
        batmudPrints(" In the name of Groo wear the shrunken skull necklace with pride.\n");
        batmudPrints(" May train skill Attack to 10%\n");
        batmudPrints(" May train skill Push to 40%\n");
        batmudPrints(" May train skill Alcohol tolerance to 3%\n");
        batmudPrints(" May train skill Consider to 10%\n");
        batmudPrints(" May train skill Hunting to 20%\n");
        batmudPrints(" May train skill Fishing to 20%\n");
        batmudPrints(" May train skill Torch creation to 15%\n");
        batmudPrints(" May train skill Looting and burning to 30%\n");
        batmudPrints(" May train skill Vandalism to 10%\n");
        batmudPrints(" May train skill Axes to 10%\n");

        batmudPrints("Abilities and requirements at each level:\n");
        batmudPrints(" Level 2:\n");
        batmudPrints("  Abilities:\n");
        batmudPrints("   May train skill Attack to 57%\n");
        batmudPrints("   May train skill Push to 100%\n");
        batmudPrints("   May train skill Bash to 10%\n");
    }

    private void userTypes(String input) {
        goalsModel.trigger(input);
    }

    private void givenPlayerListsRangerInfo() {
        userTypes("ranger info");
        batmudPrints("Name: Rangers\n");
        batmudPrints("Command: ranger\n");
        batmudPrints("Creators: Duke\n");
        batmudPrints("Your level: 2\n");
        batmudPrints("Maximum level: 35\n");

        batmudPrints("Description:\n");
        batmudPrints("The mighty barbarian guild is a loosely run group of battle hardened warriors.\n");
        batmudPrints(" Through intense training in the wilds, members become both mentally and\n");
        batmudPrints("physically tough.  These ferocious warriors excel at many combat skills.\n");

        batmudPrints("Joining requirements:\n");
        batmudPrints(" Background must be nomad (passed)\n");
        batmudPrints("Abilities gained when joining:\n");
        batmudPrints(" In the name of Groo wear the shrunken skull necklace with pride.\n");
        batmudPrints(" May train skill Attack to 20%\n");
        batmudPrints(" May train skill Push to 40%\n");
        batmudPrints(" May train skill Alcohol tolerance to 3%\n");
        batmudPrints(" May train skill Consider to 10%\n");
        batmudPrints(" May train skill Hunting to 20%\n");
        batmudPrints(" May train skill Fishing to 20%\n");
        batmudPrints(" May train skill Torch creation to 15%\n");
        batmudPrints(" May train skill Looting and burning to 30%\n");
        batmudPrints(" May train skill Vandalism to 10%\n");
        batmudPrints(" May train skill Axes to 10%\n");

        batmudPrints("Abilities and requirements at each level:\n");
        batmudPrints(" Level 2:\n");
        batmudPrints("  Abilities:\n");
        batmudPrints("   May train skill Attack to 57%\n");
        batmudPrints("   May train skill Push to 100%\n");
        batmudPrints("   May train skill Bash to 10%\n");

        batmudPrints("Abilities and requirements at each level:\n");
        batmudPrints(" Level 3:\n");
        batmudPrints("  Abilities:\n");
        batmudPrints("   May train skill Attack to 90%\n");
        batmudPrints("   May train skill Push to 100%\n");
        batmudPrints("   May train skill Bash to 10%\n");
    }

    private void givenPlayerListsCostOfTrainingAttack() {
        batmudPrints(",-------------------------------------------------.\n");
        batmudPrints("| Cost of training Attack                         |\n");
        batmudPrints("|-------------------------------------------------|\n");
        batmudPrints("| Percent     Exp        | Percent     Exp        |\n");
        batmudPrints("|=================================================|\n");
        batmudPrints("|    1% =            80  |   51% =          9046  |\n");
        batmudPrints("|    2% =            82  |   52% =          9700  |\n");
        batmudPrints("|    3% =            86  |   53% =         10395  |\n");
        batmudPrints("|    4% =            91  |   54% =         11135  |\n");
        batmudPrints("|    5% =            99  |   55% =         11921  |\n");
        batmudPrints("|    6% =           109  |   56% =         12756  |\n");
        batmudPrints("|    7% =           121  |   57% =         13642  |\n");
        batmudPrints("|    8% =           137  |   58% =         14584  |\n");
        batmudPrints("|    9% =           155  |   59% =         15583  |\n");
        batmudPrints("|   10% =           177  |   60% =         16643  |\n");
        batmudPrints("|   11% =           203  |   86% =         17768  |\n");
        batmudPrints("|   1% to 86% =         200000000  |\n");
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
        batmudPrints(",-------------------------------------------------.\n");
        batmudPrints("| Cost of training Looting and burning                         |\n");
        batmudPrints("|-------------------------------------------------|\n");
        batmudPrints("| Percent     Exp        | Percent     Exp        |\n");
        batmudPrints("|=================================================|\n");
        batmudPrints("|    1% =            80  |   51% =          9046  |\n");
        batmudPrints("|    2% =            82  |   52% =          9700  |\n");
        batmudPrints("|    3% =            86  |   53% =         10395  |\n");
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
        goalsModel.trigger(new ParsedResult("You now have 'Attack' at 100% without special bonuses.\n"));
    }

    @Test
    public void testGuildinfo() throws Exception {
        userTypes("train");
        batmudPrints("| Skills available at level  1  | Cur | Rac | Max | Exp         |");
        batmudPrints("| Attack                        |   0 |  85 | 10  |       22015 |");

        givenPlayerSetsTheirGoalToAttack();

        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Next goal: attack");
        verifyPrint("Goal attack: 80 You have enough to advance in: barbarian, ranger");
    }

    @Test
    public void testGuildinfoCanAdvanceInOneOfGuilds() throws Exception {
        userTypes("train");
        batmudPrints("| Skills available at level  1  | Cur | Rac | Max | Exp         |");
        // Attack at max in barbarians but this will still allow improvement in
        // Rangers
        batmudPrints("| Attack                        |   10 |  85 | 10  |       22015 |");

        givenPlayerSetsTheirGoalToAttack();
        batmudPrints("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n");

        verifyPrint("Goal attack: 203 You have enough to advance in: ranger");
    }
}
