package batmudgoalsplugin.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdaptedSkillCostList {
    @XmlElement(name = "list")
    public List<AdaptedSkillCostEntry> list;

    public AdaptedSkillCostList() {
    }

    public AdaptedSkillCostList(List<AdaptedSkillCostEntry> list) {
        this.list = list;
    }

}