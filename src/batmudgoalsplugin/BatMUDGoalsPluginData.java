package batmudgoalsplugin;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class BatMUDGoalsPluginData {

	@XmlJavaTypeAdapter(SkillCostLibraryMapAdapter.class)
	public Map<String, Map<String, String>> skills;
	public Map<String, SkillStatus> skillStatuses;
	public String goalSkill;
	public String goalPercent;

	public BatMUDGoalsPluginData(HashMap<String, Map<String, String>> skills,
			Map<String, SkillStatus> skillStatuses) {
		this.skills = skills;
		this.skillStatuses = skillStatuses;
	}

	public BatMUDGoalsPluginData() {
		// Needed by JAXB
	}
}