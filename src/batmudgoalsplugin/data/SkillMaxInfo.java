package batmudgoalsplugin.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillMaxInfo {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SkillMaxInfo [guild=" + guild + ", skill=" + skill + ", level="
				+ level + ", max=" + max + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guild == null) ? 0 : guild.hashCode());
		result = prime * result + level;
		result = prime * result + max;
		result = prime * result + ((skill == null) ? 0 : skill.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillMaxInfo other = (SkillMaxInfo) obj;
		if (guild == null) {
			if (other.guild != null)
				return false;
		} else if (!guild.equals(other.guild))
			return false;
		if (level != other.level)
			return false;
		if (max != other.max)
			return false;
		if (skill == null) {
			if (other.skill != null)
				return false;
		} else if (!skill.equals(other.skill))
			return false;
		return true;
	}

	public String guild;
	public String skill;
	public int level;
	public int max;

	public SkillMaxInfo(String guild, String skill, int level, int max) {
		this.guild = guild;
		this.skill = skill;
		this.level = level;
		this.max = max;
	}

	public SkillMaxInfo() {
		// Needed by JAXB
	}

}
