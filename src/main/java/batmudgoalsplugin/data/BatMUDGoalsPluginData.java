package batmudgoalsplugin.data;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "batMUDGoalsPluginData")
@XmlType(name = "batMUDGoalsPluginData")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatMUDGoalsPluginData {

    @XmlJavaTypeAdapter(SkillCostLibraryMapAdapter.class)
    @XmlElement(name = "skillCosts")
    private Map<String, Map<Integer, Integer>> skillCosts;
    @XmlElement(name = "skillStatuses")
    private Map<String, Integer> skillStatuses;
    @XmlElement(name = "skillMaxes")
    private Set<SkillMaxInfo> skillMaxes;
    @XmlElement(name = "goalSkill")
    private String goalSkill;
    @XmlElement(name = "guildLevels")
    private Map<String, Integer> guildLevels;
    @XmlElement(name = "partialTrains")
    private Map<String, Integer> partialTrains;

    public BatMUDGoalsPluginData() {
        // Needed by JAXB
    }

    public static BatMUDGoalsPluginData fromXMLFile(File file) {

        BatMUDGoalsPluginData data;
        try {
            data = (BatMUDGoalsPluginData) generateJAXBContext().createUnmarshaller()
                    .unmarshal(file);
        } catch (JAXBException e) {
            data = new BatMUDGoalsPluginData();
        }
        return data;
    }

    public static void persistToXmlFile(BatMUDGoalsPluginData data, File file) throws JAXBException {
        generateJAXBContext().createMarshaller().marshal(data, file);
    }

    private static JAXBContext generateJAXBContext() throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class);
        return ctx;
    }

    public Map<String, Integer> getPartialTrains() {
        if (partialTrains == null) {
            partialTrains = new HashMap<String, Integer>();
        }
        return partialTrains;
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
            skillCosts = new HashMap<>();
        }
        return skillCosts;
    }

    /**
     * @return the guildlevels
     */
    private Map<String, Integer> getGuildLevels() {
        if (guildLevels == null) {
            guildLevels = new HashMap<>();
        }
        return guildLevels;
    }

    public Set<SkillMaxInfo> getSkillMaxes() {
        if (skillMaxes == null) {
            skillMaxes = new HashSet<>();
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
    public void setSkillCostForLevel(String skill, int percent, int cost) {
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
        int partials = 0;
        Map<String, Integer> map = getPartialTrains();
        if (map.containsKey(goalSkill)) {
            partials = map.get(goalSkill);
        }
        return getSkillCosts().get(goalSkill).get(getGoalPercent()) - partials * 250000;
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

    public void setSkillMaxInfo(String guild, String skill, int level, int skillMax) {
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

    /**
     * Marks that a skill has been partially trained.
     * 
     * @param skill
     */
    public void trainPartially(String skill) {
        Map<String, Integer> map = getPartialTrains();
        if (!map.containsKey(skill)) {
            map.put(skill, 0);
        }
        map.put(skill, map.get(skill) + 1);
    }

    /**
     * Clears partial trains for a skill
     * 
     * @param skillname
     */
    public void clearPartialTrains(String skillname) {
        getPartialTrains().remove(skillname);
    }
}