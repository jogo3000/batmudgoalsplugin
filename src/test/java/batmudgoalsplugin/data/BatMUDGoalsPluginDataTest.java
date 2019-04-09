package batmudgoalsplugin.data;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

public class BatMUDGoalsPluginDataTest {

    @Test
    public void testSerialization() throws Exception {
        File file = File.createTempFile("testdata", "data");
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        data.trainPartially("Create Food");
        data.setGoalSkill("Mastery Of Camping");
        data.setSkillMaxInfo("Ranger", "Camping", 1, 90);
        BatMUDGoalsPluginData.persistToFile(data, file);

        BatMUDGoalsPluginData read = BatMUDGoalsPluginData.fromFile(file);
        assertEquals(1, read.getPartialTrains().get("Create Food").intValue());
        assertEquals("Mastery Of Camping", read.getGoalSkill());
        assertEquals(90, read.getSkillMaxes().stream().filter(info -> info.guild.equals("Ranger")).findFirst().get().max);
    }
        
}
