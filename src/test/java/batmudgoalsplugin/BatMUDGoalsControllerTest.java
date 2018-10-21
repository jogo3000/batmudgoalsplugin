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
        goalsModel.trigger(new ParsedResult("| Attack                      |  1 |  85 | 100 |       22015 |\n"));
    }

    private void givenPlayerListsBarbarianInfo() {
        goalsModel.trigger("barbarian info");
        goalsModel.trigger(new ParsedResult("Name: Barbarian Guild\n"));
        goalsModel.trigger(new ParsedResult("Command: barbarian\n"));
        goalsModel.trigger(new ParsedResult("Creators: Duke\n"));
        goalsModel.trigger(new ParsedResult("Your level: 1\n"));
        goalsModel.trigger(new ParsedResult("Maximum level: 35\n"));

        goalsModel.trigger(new ParsedResult("Description:\n"));
        goalsModel.trigger(
                new ParsedResult("The mighty barbarian guild is a loosely run group of battle hardened warriors.\n"));
        goalsModel.trigger(
                new ParsedResult(" Through intense training in the wilds, members become both mentally and\n"));
        goalsModel.trigger(
                new ParsedResult("physically tough.  These ferocious warriors excel at many combat skills.\n"));

        goalsModel.trigger(new ParsedResult("Joining requirements:\n"));
        goalsModel.trigger(new ParsedResult(" Background must be nomad (passed)\n"));
        goalsModel.trigger(new ParsedResult("Abilities gained when joining:\n"));
        goalsModel.trigger(new ParsedResult(" In the name of Groo wear the shrunken skull necklace with pride.\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Attack to 10%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Push to 40%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Alcohol tolerance to 3%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Consider to 10%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Hunting to 20%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Fishing to 20%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Torch creation to 15%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Looting and burning to 30%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Vandalism to 10%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Axes to 10%\n"));

        goalsModel.trigger(new ParsedResult("Abilities and requirements at each level:\n"));
        goalsModel.trigger(new ParsedResult(" Level 2:\n"));
        goalsModel.trigger(new ParsedResult("  Abilities:\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Attack to 57%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Push to 100%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Bash to 10%\n"));
    }

    private void givenPlayerListsRangerInfo() {
        goalsModel.trigger("ranger info");
        goalsModel.trigger(new ParsedResult("Name: Rangers\n"));
        goalsModel.trigger(new ParsedResult("Command: ranger\n"));
        goalsModel.trigger(new ParsedResult("Creators: Duke\n"));
        goalsModel.trigger(new ParsedResult("Your level: 2\n"));
        goalsModel.trigger(new ParsedResult("Maximum level: 35\n"));

        goalsModel.trigger(new ParsedResult("Description:\n"));
        goalsModel.trigger(
                new ParsedResult("The mighty barbarian guild is a loosely run group of battle hardened warriors.\n"));
        goalsModel.trigger(
                new ParsedResult(" Through intense training in the wilds, members become both mentally and\n"));
        goalsModel.trigger(
                new ParsedResult("physically tough.  These ferocious warriors excel at many combat skills.\n"));

        goalsModel.trigger(new ParsedResult("Joining requirements:\n"));
        goalsModel.trigger(new ParsedResult(" Background must be nomad (passed)\n"));
        goalsModel.trigger(new ParsedResult("Abilities gained when joining:\n"));
        goalsModel.trigger(new ParsedResult(" In the name of Groo wear the shrunken skull necklace with pride.\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Attack to 20%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Push to 40%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Alcohol tolerance to 3%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Consider to 10%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Hunting to 20%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Fishing to 20%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Torch creation to 15%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Looting and burning to 30%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Vandalism to 10%\n"));
        goalsModel.trigger(new ParsedResult(" May train skill Axes to 10%\n"));

        goalsModel.trigger(new ParsedResult("Abilities and requirements at each level:\n"));
        goalsModel.trigger(new ParsedResult(" Level 2:\n"));
        goalsModel.trigger(new ParsedResult("  Abilities:\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Attack to 57%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Push to 100%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Bash to 10%\n"));

        goalsModel.trigger(new ParsedResult("Abilities and requirements at each level:\n"));
        goalsModel.trigger(new ParsedResult(" Level 3:\n"));
        goalsModel.trigger(new ParsedResult("  Abilities:\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Attack to 90%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Push to 100%\n"));
        goalsModel.trigger(new ParsedResult("   May train skill Bash to 10%\n"));
    }

    private void givenPlayerListsCostOfTrainingAttack() {
        goalsModel.trigger(new ParsedResult(",-------------------------------------------------.\n"));
        goalsModel.trigger(new ParsedResult("| Cost of training Attack                         |\n"));
        goalsModel.trigger(new ParsedResult("|-------------------------------------------------|\n"));
        goalsModel.trigger(new ParsedResult("| Percent     Exp        | Percent     Exp        |\n"));
        goalsModel.trigger(new ParsedResult("|=================================================|\n"));
        goalsModel.trigger(new ParsedResult("|    1% =            80  |   51% =          9046  |\n"));
        goalsModel.trigger(new ParsedResult("|    2% =            82  |   52% =          9700  |\n"));
        goalsModel.trigger(new ParsedResult("|    3% =            86  |   53% =         10395  |\n"));
        goalsModel.trigger(new ParsedResult("|    4% =            91  |   54% =         11135  |\n"));
        goalsModel.trigger(new ParsedResult("|    5% =            99  |   55% =         11921  |\n"));
        goalsModel.trigger(new ParsedResult("|    6% =           109  |   56% =         12756  |\n"));
        goalsModel.trigger(new ParsedResult("|    7% =           121  |   57% =         13642  |\n"));
        goalsModel.trigger(new ParsedResult("|    8% =           137  |   58% =         14584  |\n"));
        goalsModel.trigger(new ParsedResult("|    9% =           155  |   59% =         15583  |\n"));
        goalsModel.trigger(new ParsedResult("|   10% =           177  |   60% =         16643  |\n"));
        goalsModel.trigger(new ParsedResult("|   11% =           203  |   86% =         17768  |\n"));
        goalsModel.trigger(new ParsedResult("|   1% to 86% =         200000000  |\n"));
    }

    @Test
    @DisplayName("When player sets goal to a skill which the plugin has no information about, the player gets an error message")
    public void testSkillNotInLibrary() throws Exception {
        goalsModel.trigger("goal looting and burning");
        verifyPrint("looting and burning not in library");
    }

    private void verifyPrint(String expected) {
        verify(guiModel).printText("generic", String.format("%s%n", expected));
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

        goalsModel.trigger("goal");

        verifyPrint("attack (*)");
        verifyPrint("looting and burning");
    }

    private void givenPlayerSetsTheirGoalToAttack() {
        goalsModel.trigger("goal attack");
    }

    private void givenListsCostOfTrainingLootingAndBurning() {
        goalsModel.trigger(new ParsedResult(",-------------------------------------------------.\n"));
        goalsModel.trigger(new ParsedResult("| Cost of training Looting and burning                         |\n"));
        goalsModel.trigger(new ParsedResult("|-------------------------------------------------|\n"));
        goalsModel.trigger(new ParsedResult("| Percent     Exp        | Percent     Exp        |\n"));
        goalsModel.trigger(new ParsedResult("|=================================================|\n"));
        goalsModel.trigger(new ParsedResult("|    1% =            80  |   51% =          9046  |\n"));
        goalsModel.trigger(new ParsedResult("|    2% =            82  |   52% =          9700  |\n"));
        goalsModel.trigger(new ParsedResult("|    3% =            86  |   53% =         10395  |\n"));
    }

    @Test
    @DisplayName("Given the player has not enough exp to train their goal skill, when player uses 'exp' command they are informed how much they need to train")
    public void testParseCostTrain() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        goalsModel.trigger(new ParsedResult("Exp: 2 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));
        verifyPrint("Goal attack: 82 You need: " + Integer.toString(82 - 2));
    }

    @Test
    public void testExpOutputWithZeroValues() throws Exception {
        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 2 Money: 0 Bank: 0 Exp pool: 0\n"));
        verifyPrint("Goal attack: 82 You need: " + Integer.toString(82 - 2));
    }

    @Test
    @DisplayName("Given the player has enough experience to train their goal skill, when player uses 'exp' command they are informed they can train and in which guilds they can do so")
    public void testEnoughExp() throws Exception {
        givenPlayerSetsTheirGoalToAttack();

        goalsModel.trigger(new ParsedResult("Exp: 100000 Money: 0 Bank: 0 Exp pool: 0\n"));

        verifyPrint("Next goal: attack");
        verifyPrint("Goal attack: 82 You have enough to advance in: barbarian, ranger");
    }

    @Test
    @DisplayName("Given player's goal skill is at the max at their level, when player uses 'exp' command they are informed they need another level")
    public void testNeedAnotherLevel() throws Exception {
        goalsModel.trigger(new ParsedResult("| Attack                      |  85 |  85 | 85 |       22015 |\n"));
        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));
        verifyPrint("Goal attack: needs level");
    }

    @Test
    @DisplayName("Given player's goal skill is at max, when player uses 'exp' command they are informed the goal is full")
    public void testSkillIsFull() throws Exception {
        goalsModel.trigger(new ParsedResult("| Attack                      |  100 |  85 | 100 |       (n/a) |\n"));
        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));
        verifyPrint("Goal attack: full");

    }

    @Test
    public void testTrainOutput() throws Exception {
        goalsModel.trigger(new ParsedResult("You now have 'Attack' at 100% without special bonuses.\n"));
        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));
        verifyPrint("Goal attack: full");
    }

    @Test
    public void testGuildinfo() throws Exception {

        goalsModel.trigger("train");
        goalsModel.trigger(new ParsedResult("| Skills available at level  1  | Cur | Rac | Max | Exp         |"));
        goalsModel.trigger(new ParsedResult("| Attack                        |   0 |  85 | 10  |       22015 |"));

        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));

        verifyPrint("Next goal: attack");
        verifyPrint("Goal attack: 80 You have enough to advance in: barbarian, ranger");
    }

    @Test
    public void testGuildinfoCanAdvanceInOneOfGuilds() throws Exception {

        goalsModel.trigger("train");
        goalsModel.trigger(new ParsedResult("| Skills available at level  1  | Cur | Rac | Max | Exp         |"));
        // Attack at max in barbarians but this will still allow improvement in
        // Rangers
        goalsModel.trigger(new ParsedResult("| Attack                        |   10 |  85 | 10  |       22015 |"));

        givenPlayerSetsTheirGoalToAttack();
        goalsModel.trigger(new ParsedResult("Exp: 12920 Money: 211.10 Bank: 64440.00 Exp pool: 100.0\n"));

        verifyPrint("Goal attack: 203 You have enough to advance in: ranger");
    }
}
