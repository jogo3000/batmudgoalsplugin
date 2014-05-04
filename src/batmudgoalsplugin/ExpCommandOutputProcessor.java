package batmudgoalsplugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

import com.mythicscape.batclient.interfaces.ClientGUI;

/**
 * Processes output from 'exp' command.
 * 
 * Exp command output is something like this: <br>
 * Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0<br>
 * Takes exp amount, outputs goal skill, needed exp and amount missing from next
 * percent
 */
class ExpCommandOutputProcessor extends AbstractCommandProcessor {
	public ExpCommandOutputProcessor(ClientGUI clientGUI,
			BatMUDGoalsPluginData data) {
		super(
				"Exp: (\\d+) Money: (\\d+)\\.?(\\d*) Bank: (\\d+)\\.?(\\d*) Exp pool: (\\d+)\\.?(\\d*)\\s*",
				clientGUI, data);
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
				// get skillmaxinfo for this skill
				Collection<SkillMaxInfo> skillmaxinfo = new ArrayList<SkillMaxInfo>();
				Collection<String> guilds = new HashSet<String>();
				for (SkillMaxInfo s : data.getSkillMaxes()) {
					if (data.isGoalSkill(s.skill)
							&& s.level <= data.getGuildLevel(s.guild)
							&& s.max >= data.getGoalPercent()) {
						skillmaxinfo.add(s);
						guilds.add(s.guild);
					}
				}

				if (skillmaxinfo.isEmpty()) {
					printMessage("Goal %s: needs level", data.getGoalSkill());
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
								data.getGoalSkill(), neededExp,
								concatGuildNames(guilds));
					}
				}

			}
		}
		return false;
	}
}