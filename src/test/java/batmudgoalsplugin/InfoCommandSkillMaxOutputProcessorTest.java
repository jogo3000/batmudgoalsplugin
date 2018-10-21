package batmudgoalsplugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

public class InfoCommandSkillMaxOutputProcessorTest {

    @Test
    public void testSkillMaxOutput() {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        InfoCommandSkillMaxOutputProcessor op = new InfoCommandSkillMaxOutputProcessor(data);
        op.setGuild("tzarakk");
        op.setLevel(12);

        op.receive(" May train skill Attack to 20%");
        op.receive(" May train skill Looting and burning to 100%");

        assertFalse(data.getSkillMaxes().isEmpty(), "Processor didn't catch skillmax info");
        Set<SkillMaxInfo> expected = new HashSet<>();
        expected.add(new SkillMaxInfo("tzarakk", "attack", 12, 20));
        expected.add(new SkillMaxInfo("tzarakk", "looting and burning", 12, 100));
        assertEquals(expected, data.getSkillMaxes());
    }

    @Test
    public void testSpellMaxOutput() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        InfoCommandSkillMaxOutputProcessor op = new InfoCommandSkillMaxOutputProcessor(data);
        op.setGuild("spider");
        op.setLevel(10);

        op.receive("Requirements:");
        op.receive("Has trained Discipline to 15%");
        op.receive("Has trained Stab to 26%");
        op.receive("Has trained Short blades to 37%");
        op.receive("Has trained Combat sense to 17%");
        op.receive("Has trained Stunned maneuvers to 5%");
        op.receive("Has trained Parry to 18%");
        op.receive("Available:");

        op.receive("May train skill Throw weight to 40%");
        op.receive("May train skill Leadership to 17%");
        op.receive("May study spell Infravision to 100%");
        op.receive("May study spell Cleanse weapon to 100%");

        Set<SkillMaxInfo> expected = new HashSet<>();
        expected.add(new SkillMaxInfo("spider", "throw weight", 10, 40));
        expected.add(new SkillMaxInfo("spider", "leadership", 10, 17));
        expected.add(new SkillMaxInfo("spider", "infravision", 10, 100));
        expected.add(new SkillMaxInfo("spider", "cleanse weapon", 10, 100));

        assertEquals(expected, data.getSkillMaxes());
    }
}
