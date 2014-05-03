package batmudgoalsplugin.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BatMUDGoalsPluginData {

	@XmlJavaTypeAdapter(SkillCostLibraryMapAdapter.class)
	private Map<String, Map<Integer, Integer>> skillCosts;
	@XmlElement
	private Map<String, Integer> skillStatuses;
	@XmlElement
	private Set<SkillMaxInfo> skillMaxes;
	@XmlElement
	private String goalSkill;
	@XmlElement
	private Map<String, Integer> guildLevels;

	public BatMUDGoalsPluginData() {
		// Needed by JAXB
	}

	private Map<String, Integer> getSkillStatuses() {
		if (skillStatuses == null) {
			skillStatuses = new HashMap<String, Integer>();
		}
		return skillStatuses;
	}

	/**
	 * @return the skills
	 */
	private Map<String, Map<Integer, Integer>> getSkillCosts() {
		if (skillCosts == null) {
			skillCosts = new HashMap<String, Map<Integer, Integer>>();
		}
		return skillCosts;
	}

	/**
	 * @return the guildlevels
	 */
	private Map<String, Integer> getGuildLevels() {
		if (guildLevels == null) {
			guildLevels = new HashMap<String, Integer>();
		}
		return guildLevels;
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

	/**
	 * Returns the current percent of a skill
	 * 
	 * @param skill
	 * @return
	 */
	public int getCurrentSkillStatus(String skill) {
		return getSkillStatuses().get(skill);
	}

	/**
	 * Sets the cost to improve a skill to a given percent value
	 * 
	 * @param skill
	 * @param percent
	 * @param cost
	 */
	public void setSkillCost(String skill, int percent, int cost) {
		Map<String, Map<Integer, Integer>> skillcosts = getSkillCosts();
		if (!skillcosts.containsKey(skill)) {
			skillcosts.put(skill, new HashMap<Integer, Integer>());
		}
		skillcosts.get(skill).put(percent, cost);
	}

	/**
	 * Returns the experience cost to improve a skill to the given percent
	 * 
	 * @param skill
	 * @param percent
	 * @return
	 */
	public int getSkillCost(String skill, int percent) {
		return getSkillCosts().get(skill).get(percent);
	}

	/**
	 * @return next percent of goal skill
	 */
	public int getGoalPercent() {
		return getSkillStatuses().get(goalSkill) + 1;
	}

	/**
	 * @return cost to improve the goal skill to the given percent value
	 */
	public int getImproveGoalSkillCost() {
		return getSkillCosts().get(goalSkill).get(getGoalPercent());
	}

	/**
	 * @return collection of the skills stored
	 */
	public Collection<String> getStoredSkills() {
		return getSkillCosts().keySet();
	}

	/**
	 * Tests if skill has been saved in library
	 * 
	 * @param skill
	 * @return
	 */
	public boolean isSkillInCostLibrary(String skill) {
		return getSkillCosts().containsKey(skill);
	}

	/**
	 * @return current goal skill
	 */
	public String getGoalSkill() {
		return goalSkill;
	}

	public void setGuildLevel(String guild, int level) {
		getGuildLevels().put(guild, level);
	}

	public void setSkillMaxInfo(String guild, String skill, int level,
			int skillMax) {
		getSkillMaxes().add(new SkillMaxInfo(guild, skill, level, skillMax));
	}

	/**
	 * Returns the level in the given guild
	 * 
	 * @param guild
	 * @return
	 */
	public int getGuildLevel(String guild) {
		return getGuildLevels().get(guild);
	}
}