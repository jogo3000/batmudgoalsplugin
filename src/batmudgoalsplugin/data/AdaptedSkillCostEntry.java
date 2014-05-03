package batmudgoalsplugin.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdaptedSkillCostEntry {

	public AdaptedSkillCostEntry(String skill, Integer percent, Integer cost) {
		this.skill = skill;
		this.percent = percent;
		this.cost = cost;
	}

	@XmlAttribute
	public String skill;
	@XmlAttribute
	public Integer percent;
	@XmlAttribute
	public Integer cost;

	public AdaptedSkillCostEntry() {
		// Needed by JAXB
	}
}