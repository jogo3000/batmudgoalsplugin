package batmudgoalsplugin;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

/**
 * Processes output from 'exp' command.
 * 
 * Exp command output is something like this: <br>
 * Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0<br>
 * Takes exp amount, outputs goal skill, needed exp and amount missing from next
 * percent
 */
class ExpCommandOutputProcessor extends AbstractCommandProcessor {
    public ExpCommandOutputProcessor(ClientGUIModel model, BatMUDGoalsPluginData data) {
        super("Exp: (\\d+) Money: (\\d+)\\.?(\\d*) Bank: (\\d+)\\.?(\\d*) Exp pool: (\\d+)\\.?(\\d*)\\s*", model, data);
    }

    private String concatGuildNames(Collection<String> guilds) {
        return guilds.stream().sorted().collect(Collectors.joining(", ")).toLowerCase();
    }

    @Override
    protected boolean process(Matcher m) {
        if (data.isGoalSet()) {
            if (data.isGoalSkillMaxed()) {
                printMessage("Goal %s: full", data.getGoalSkill());
            } else {
                Collection<SkillMaxInfo> skillmaxinfo = selectGreaterThanGoalPercent(data.getSkillMaxes(),
                        data.getGoalPercent());

                if (skillmaxinfo.isEmpty()) {
                    printMessage("None of your guilds offer more %s", data.getGoalSkill());
                } else {
                    Collection<SkillMaxInfo> available = selectAvailableOnThisLevel(skillmaxinfo);
                    if (available.isEmpty()) {
                        printMessage("Goal %s: needs level", data.getGoalSkill());
                    } else {
                        int neededExp = data.getImproveGoalSkillCost();
                        int currentExp = Integer.parseInt(m.group(1));
                        if (currentExp < neededExp) {
                            printMessage("Goal %s: %d You need: %d", data.getGoalSkill(), neededExp,
                                    neededExp - currentExp);
                        } else {
                            printMessage("Goal %s: %d You have enough to advance in: %s", data.getGoalSkill(),
                                    neededExp, concatGuildNames(selectGuildNames(available)));
                        }
                    }
                }

            }
        }
        return false;
    }

    private Collection<String> selectGuildNames(Collection<SkillMaxInfo> available) {
        return available.stream()
                .map(s -> s.guild)
                .collect(Collectors.toSet());
    }

    /**
     * Select SkillMaxInfo where level is lower than current level of player in that
     * guild
     * 
     * @param skillmaxinfo
     * @return
     */
    private Collection<SkillMaxInfo> selectAvailableOnThisLevel(Collection<SkillMaxInfo> skillmaxinfo) {
        return skillmaxinfo.stream()
                .filter(s -> s.level <= data.getGuildLevel(s.guild))
                .collect(Collectors.toList());
    }

    /**
     * Select SkillMaxInfo instances from skillMaxes where skill = goal skill and
     * skill max is greater or equal than goalPercent
     * 
     * @param skillMaxes
     * @param goalPercent
     * @return
     */
    private Collection<SkillMaxInfo> selectGreaterThanGoalPercent(Set<SkillMaxInfo> skillMaxes, int goalPercent) {
        return skillMaxes.stream()
                .filter(s -> data.isGoalSkill(s.skill))
                .filter(s -> s.max >= goalPercent)
                .collect(Collectors.toList());
    }
}