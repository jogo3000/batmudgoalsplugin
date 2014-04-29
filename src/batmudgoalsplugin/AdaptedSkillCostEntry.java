package batmudgoalsplugin;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdaptedSkillCostEntry {

	public AdaptedSkillCostEntry(String skill, String percent, String cost) {
		this.skill = skill;
		this.percent = percent;
		this.cost = cost;
	}

	@XmlAttribute
	public String skill;
	@XmlAttribute
	public String percent;
	@XmlAttribute
	public String cost;

	public AdaptedSkillCostEntry() {
		// Needed by JAXB
	}
}