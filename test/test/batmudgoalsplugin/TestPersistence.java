package test.batmudgoalsplugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import batmudgoalsplugin.BatMUDGoalsPluginData;
import batmudgoalsplugin.SkillStatus;

public class TestPersistence {

	@Test
	public void test() throws Exception {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData(
				new HashMap<String, Map<Integer, Integer>>(),
				new HashMap<String, SkillStatus>());

		data.goalPercent = "1";
		data.goalSkill = "attack";

		Map<Integer, Integer> skillCostMap = new HashMap<Integer, Integer>();
		skillCostMap.put(1, 1001);
		skillCostMap.put(2, 1002);
		data.skills.put("attack", skillCostMap);

		data.skillStatuses.put("looting and burning", new SkillStatus(76, 100));

		JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class,
				SkillStatus.class);
		Marshaller m = ctx.createMarshaller();
		StringWriter writer = new StringWriter();
		m.marshal(data, writer);

		Unmarshaller u = ctx.createUnmarshaller();
		BatMUDGoalsPluginData o = (BatMUDGoalsPluginData) u
				.unmarshal(new StringReader(writer.toString()));

		assertEquals("1", o.goalPercent);
		assertEquals("attack", o.goalSkill);
		assertTrue(o.skills.containsKey("attack"));

		Map<Integer, Integer> ummap = o.skills.get("attack");
		assertEquals(1001, ummap.get(1).intValue());
		assertEquals(1002, ummap.get(2).intValue());

		assertEquals(76,
				o.skillStatuses.get("looting and burning").cur.intValue());
		assertEquals(100,
				o.skillStatuses.get("looting and burning").max.intValue());
	}
}
