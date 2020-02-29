package batmudgoalsplugin.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BatMUDGoalsPluginData {

    private static final String BEGIN_PARTIAL_TRAINS_MARKER = ">Partial trains";
    private static final String END_PARTIAL_TRAINS_MARKER = "<Partial trains";
    private static final String BEGIN_GUILD_LEVELS_MARKER = ">Guild levels";
    private static final String END_GUILD_LEVELS_MARKER = "<Guild levels";
    private static final String BEGIN_GOAL_SKILL_MARKER = ">Goal skill";
    private static final String END_GOAL_SKILL_MARKER = "<Goal skill";
    private static final String BEGIN_SKILL_MAXES_MARKER = ">Skill maxes";
    private static final String END_SKILL_MAXES_MARKER = "<Skill maxes";
    private static final String BEGIN_SKILL_STATUSES_MARKER = ">Skill statuses";
    private static final String END_SKILL_STATUSES_MARKER = "<Skill statuses";
    private static final String END_SKILL_COSTS_MARKER = "<Skill costs";
    private static final String BEGIN_SKILL_COSTS_MARKER = ">Skill costs";

    private Map<String, Map<Integer, Integer>> skillCosts;
    private Map<String, Integer> skillStatuses;
    private Set<SkillMaxInfo> skillMaxes;
    private String goalSkill;
    private Map<String, Integer> guildLevels;
    private Map<String, Integer> partialTrains;

    public BatMUDGoalsPluginData() {
    }

    private static void skipReaderTo(BufferedReader reader, String marker) throws IOException {
        while(!marker.equals(reader.readLine()));
    }

    public static BatMUDGoalsPluginData fromFile(File file) {
        BatMUDGoalsPluginData data = new BatMUDGoalsPluginData();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            skipReaderTo(reader, BEGIN_SKILL_COSTS_MARKER);
            while (!END_SKILL_COSTS_MARKER.equals((line = reader.readLine()))) {
                String skill = line.substring(1);
                while(!"<".equals((line = reader.readLine()))) {
                    String[] parts = line.split(",");
                    data.setSkillCostForLevel(skill, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
            }

            skipReaderTo(reader, BEGIN_SKILL_STATUSES_MARKER);
            while(!END_SKILL_STATUSES_MARKER.equals((line = reader.readLine()))) {
                String[] parts = line.split(",");
                data.setSkillStatus(parts[0], Integer.parseInt(parts[1]));
            }

            skipReaderTo(reader, BEGIN_SKILL_MAXES_MARKER);
            while(!END_SKILL_MAXES_MARKER.equals((line = reader.readLine()))) {
                String[] parts = line.split(",");
                data.setSkillMaxInfo(parts[0], parts[3], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }
            skipReaderTo(reader, BEGIN_GOAL_SKILL_MARKER);
            while(!END_GOAL_SKILL_MARKER.equals((line = reader.readLine()))) {
                data.setGoalSkill(line);
            }

            skipReaderTo(reader, BEGIN_GUILD_LEVELS_MARKER);
            while(!END_GUILD_LEVELS_MARKER.equals((line = reader.readLine()))) {
                String[] parts = line.split(",");
                data.setGuildLevel(parts[0], Integer.parseInt(parts[1]));
            }

            skipReaderTo(reader, BEGIN_PARTIAL_TRAINS_MARKER);
            while(!END_PARTIAL_TRAINS_MARKER.equals((line = reader.readLine()))) {
                String[] parts = line.split(",");
                for(int i = 0; i < Integer.parseInt(parts[1]); i++) {
                    data.trainPartially(parts[0]);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot deserialize Batmud goals data!");}
        return data;
    }

    public static void persistToFile(BatMUDGoalsPluginData data, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(BEGIN_SKILL_COSTS_MARKER);
            data.getSkillCosts().forEach(
                                         (skill, costs) -> {
                                             writer.println(">" + skill);
                                             costs.forEach((level, cost) -> {
                                                     writer.println(level + "," + cost);
                                                 });
                                             writer.println("<");
                                             });
            writer.println(END_SKILL_COSTS_MARKER);

            writer.println(BEGIN_SKILL_STATUSES_MARKER);
            data.getSkillStatuses().forEach((skill, level) -> {
                    writer.println(skill + "," + level);
                });
            writer.println(END_SKILL_STATUSES_MARKER);

            writer.println(BEGIN_SKILL_MAXES_MARKER);
            data.getSkillMaxes().forEach(skillmax -> {
                    writer.println(skillmax.guild + "," + skillmax.level + "," + skillmax.max + "," + skillmax.skill);
                });
            writer.println(END_SKILL_MAXES_MARKER);

            writer.println(BEGIN_GOAL_SKILL_MARKER);
            writer.println(data.getGoalSkill());
            writer.println(END_GOAL_SKILL_MARKER);

            writer.println(BEGIN_GUILD_LEVELS_MARKER);
            data.getGuildLevels().forEach((guild, level) -> {
                    writer.println(guild + "," + level);
                });
            writer.println(END_GUILD_LEVELS_MARKER);

            writer.println(BEGIN_PARTIAL_TRAINS_MARKER);
            data.getPartialTrains().forEach((skill, partial) -> {
                    writer.println(skill + "," + partial);
                });
            writer.println(END_PARTIAL_TRAINS_MARKER);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot serialize the Batmud goals data!");
        }
    }

    public Map<String, Integer> getPartialTrains() {
        if (partialTrains == null) {
            partialTrains = new HashMap<>();
        }
        return partialTrains;
    }

    private Map<String, Integer> getSkillStatuses() {
        if (skillStatuses == null) {
            skillStatuses = new HashMap<>();
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
