package batmudgoalsplugin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

public class InfoCommandSkillMaxOutputProcessorTest {

	@Test
	public void testSkillMaxOutput() {
		BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
		InfoCommandSkillMaxOutputProcessor op = new InfoCommandSkillMaxOutputProcessor(
				data);
		op.setGuild("tzarakk");
		op.setLevel(12);

		op.receive(" May train skill Attack to 20%");
		op.receive(" May train skill Looting and burning to 100%");

		assertFalse("Processor didn't catch skillmax info", data
				.getSkillMaxes().isEmpty());
		Set<SkillMaxInfo> expected = new HashSet<SkillMaxInfo>();
		expected.add(new SkillMaxInfo("tzarakk", "attack", 12, 20));
		expected.add(new SkillMaxInfo("tzarakk", "looting and burning", 12, 100));
		assertEquals(expected, data.getSkillMaxes());
	}
}
