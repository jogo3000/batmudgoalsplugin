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

    @XmlAttribute(name = "skill")
    public String skill;
    @XmlAttribute(name = "percent")
    public Integer percent;
    @XmlAttribute(name = "cost")
    public Integer cost;

    public AdaptedSkillCostEntry() {
        // Needed by JAXB
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cost == null) ? 0 : cost.hashCode());
        result = prime * result + ((percent == null) ? 0 : percent.hashCode());
        result = prime * result + ((skill == null) ? 0 : skill.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AdaptedSkillCostEntry other = (AdaptedSkillCostEntry) obj;
        if (cost == null) {
            if (other.cost != null)
                return false;
        } else if (!cost.equals(other.cost))
            return false;
        if (percent == null) {
            if (other.percent != null)
                return false;
        } else if (!percent.equals(other.percent))
            return false;
        if (skill == null) {
            if (other.skill != null)
                return false;
        } else if (!skill.equals(other.skill))
            return false;
        return true;
    }

}