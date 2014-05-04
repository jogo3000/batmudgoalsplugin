package batmudgoalsplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

import com.mythicscape.batclient.interfaces.BatClientPlugin;

/**
 * Processes output from 'exp' command.
 * 
 * Exp command output is something like this: <br>
 * Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0<br>
 * Takes exp amount, outputs goal skill, needed exp and amount missing from next
 * percent
 */
class ExpCommandOutputProcessor extends AbstractCommandProcessor {
	public ExpCommandOutputProcessor(BatClientPlugin plugin,
			BatMUDGoalsPluginData data) {
		super(
				"Exp: (\\d+) Money: (\\d+)\\.?(\\d*) Bank: (\\d+)\\.?(\\d*) Exp pool: (\\d+)\\.?(\\d*)\\s*",
				plugin, data);
	}

	private String concatGuildNames(Collection<String> guilds) {
		StringBuilder sb = new StringBuilder();
		for (String s : guilds) {
			sb.append(s);
			sb.append(", ");
		}
		return sb.toString().substring(0, sb.length() - 2).toLowerCase();
	}

	@Override
	protected boolean process(Matcher m) {
		if (data.isGoalSet()) {
			if (data.isGoalSkillMaxed()) {
				printMessage("Goal %s: full", data.getGoalSkill());
			} else {
				Collection<SkillMaxInfo> skillmaxinfo = selectGreaterThanGoalPercent(
						data.getSkillMaxes(), data.getGoalPercent());

				if (skillmaxinfo.isEmpty()) {
					printMessage("None of your guilds offer more %s",
							data.getGoalSkill());
				} else {
					Collection<SkillMaxInfo> available = selectAvailableOnThisLevel(skillmaxinfo);
					if (available.isEmpty()) {
						printMessage("Goal %s: needs level",
								data.getGoalSkill());
					} else {
						int neededExp = data.getImproveGoalSkillCost();
						int currentExp = Integer.parseInt(m.group(1));
						if (currentExp < neededExp) {
							printMessage("Goal %s: %d You need: %d",
									data.getGoalSkill(), neededExp, neededExp
											- currentExp);
						} else {
							printMessage(
									"Goal %s: %d You have enough to advance in: %s",
									data.getGoalSkill(),
									neededExp,
									concatGuildNames(selectGuildNames(available)));
						}
					}
				}

			}
		}
		return false;
	}

	private Collection<String> selectGuildNames(
			Collection<SkillMaxInfo> available) {
		Collection<String> guilds = new HashSet<String>();
		for (SkillMaxInfo s : available) {
			guilds.add(s.guild);
		}
		return guilds;
	}

	/**
	 * Select SkillMaxInfo where level is lower than current level of player in
	 * that guild
	 * 
	 * @param skillmaxinfo
	 * @return
	 */
	private Collection<SkillMaxInfo> selectAvailableOnThisLevel(
			Collection<SkillMaxInfo> skillmaxinfo) {
		Collection<SkillMaxInfo> available = new ArrayList<SkillMaxInfo>();
		for (SkillMaxInfo s : skillmaxinfo) {
			if (s.level <= data.getGuildLevel(s.guild)) {
				available.add(s);
			}
		}
		return available;
	}

	/**
	 * Select SkillMaxInfo instances from skillMaxes where skill = goal skill
	 * and skill max is greater or equal than goalPercent
	 * 
	 * @param skillMaxes
	 * @param goalPercent
	 * @return
	 */
	private Collection<SkillMaxInfo> selectGreaterThanGoalPercent(
			Set<SkillMaxInfo> skillMaxes, int goalPercent) {
		Collection<SkillMaxInfo> skillmaxinfo = new ArrayList<SkillMaxInfo>();
		for (SkillMaxInfo s : skillMaxes) {
			if (data.isGoalSkill(s.skill) && s.max >= goalPercent) {
				skillmaxinfo.add(s);
			}
		}
		return skillmaxinfo;
	}
}