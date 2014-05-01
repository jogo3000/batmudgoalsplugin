package batmudgoalsplugin;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class BatMUDGoalsPluginData {

	@XmlJavaTypeAdapter(SkillCostLibraryMapAdapter.class)
	public Map<String, Map<Integer, Integer>> skills;
	public Map<String, SkillStatus> skillStatuses;
	public String goalSkill;
	public String goalPercent;

	public BatMUDGoalsPluginData(HashMap<String, Map<Integer, Integer>> map,
			Map<String, SkillStatus> skillStatuses) {
		this.skills = map;
		this.skillStatuses = skillStatuses;
	}

	public BatMUDGoalsPluginData() {
		// Needed by JAXB
	}
}