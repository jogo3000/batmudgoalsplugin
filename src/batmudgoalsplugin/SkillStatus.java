package batmudgoalsplugin;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillStatus {
	public SkillStatus(int cur, int max) {
		this.cur = cur;
		this.max = max;
	}

	public SkillStatus(String string, String string2) {
		this(Integer.parseInt(string), Integer.parseInt(string2));
	}

	public SkillStatus() {
		// Needed by JAXB
	}

	public Integer cur;
	public Integer max;
}