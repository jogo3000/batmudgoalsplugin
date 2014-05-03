package batmudgoalsplugin.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
public class BatMUDGoalsPluginData {

	@XmlJavaTypeAdapter(SkillCostLibraryMapAdapter.class)
	public Map<String, Map<Integer, Integer>> skills;
	public Map<String, Integer> skillStatuses;
	public Set<SkillMaxInfo> skillMaxes;
	public String goalSkill;
	public Integer goalPercent;
	public Map<String, Integer> guildlevels;

	public BatMUDGoalsPluginData(Map<String, Map<Integer, Integer>> map,
			Map<String, Integer> skillStatuses, Set<SkillMaxInfo> skillMaxInfo,
			Map<String, Integer> guildlevels) {
		this.skills = map;
		this.skillStatuses = skillStatuses;
		this.skillMaxes = skillMaxInfo;
		this.guildlevels = guildlevels;
	}

	public BatMUDGoalsPluginData() {
		// Needed by JAXB
	}

	public Map<String, Integer> getSkillStatuses() {
		if (skillStatuses == null) {
			skillStatuses = new HashMap<String, Integer>();
		}
		return skillStatuses;
	}

	/**
	 * @return the skills
	 */
	public Map<String, Map<Integer, Integer>> getSkills() {
		if (skills == null) {
			skills = new HashMap<String, Map<Integer, Integer>>();
		}
		return skills;
	}

	/**
	 * @return the guildlevels
	 */
	public Map<String, Integer> getGuildlevels() {
		if (guildlevels == null) {
			guildlevels = new HashMap<String, Integer>();
		}
		return guildlevels;
	}

	public Set<SkillMaxInfo> getSkillMaxes() {
		if (skillMaxes == null) {
			skillMaxes = new HashSet<SkillMaxInfo>();
		}
		return skillMaxes;
	}

	/**
	 * Sets the goal skill
	 * 
	 * @param skill
	 */
	public void setGoalSkill(String skill) {
		goalSkill = skill;
	}

	/**
	 * @return true if goal has been set, false if not
	 */
	public boolean isGoalSet() {
		return goalSkill != null;
	}

	/**
	 * Tests if skill is goal skill
	 * 
	 * @param skill
	 * @return true if skill is goal skill, false otherwise
	 */
	public boolean isGoalSkill(String skill) {
		return skill.equals(goalSkill);
	}

	/**
	 * @return true if goal skill is maxed, false otherwise
	 */
	public boolean isGoalSkillMaxed() {
		return getSkillStatuses().get(goalSkill) == 100;
	}

	/**
	 * Sets the current percent of a skill
	 * 
	 * @param skill
	 * @param percent
	 */
	public void setSkillStatus(String skill, int percent) {
		getSkillStatuses().put(skill, percent);
	}

}