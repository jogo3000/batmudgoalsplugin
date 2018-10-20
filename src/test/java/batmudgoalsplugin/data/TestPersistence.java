package batmudgoalsplugin.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

public class TestPersistence {

    @Test
    public void test() throws Exception {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();

        data.setGoalSkill("attack");

        data.setSkillCostForLevel("attack", 1, 1001);
        data.setSkillCostForLevel("attack", 2, 1002);

        data.setSkillStatus("looting and burning", 76);
        data.setSkillStatus("attack", 0);
        data.setGuildLevel("ranger", 32);

        data.setSkillMaxInfo("Rangers", "attack", 1, 1);

        JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class);
        Marshaller m = ctx.createMarshaller();
        StringWriter writer = new StringWriter();
        m.marshal(data, writer);

        Unmarshaller u = ctx.createUnmarshaller();
        BatMUDGoalsPluginData o = (BatMUDGoalsPluginData) u.unmarshal(new StringReader(writer.toString()));

        assertEquals(1, o.getGoalPercent());
        assertEquals("attack", o.getGoalSkill());

        assertEquals(1001, o.getSkillCost("attack", 1));
        assertEquals(1002, o.getSkillCost("attack", 2));
        assertEquals(32, o.getGuildLevel("ranger"));
        assertEquals(76, o.getCurrentSkillStatus("looting and burning"));

        assertTrue(o.getSkillMaxes().contains(new SkillMaxInfo("Rangers", "attack", 1, 1)));
    }
}
