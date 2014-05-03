package batmudgoalsplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;
import batmudgoalsplugin.data.SkillMaxInfo;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

/**
 * Plugin for BatClient. Player can set a goal of improving a skill in her
 * guild. Experience needed to reach the next percent is then shown upon 'exp'
 * command.
 * 
 * @author Jogo
 */
public class BatMUDGoalsPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger, BatClientPluginTrigger,
		BatClientPluginUtil {

	private BatMUDGoalsPluginData data;
	private Collection<AbstractCommandProcessor> commandProcessors;
	private Collection<AbstractCommandProcessor> outputProcessors;

	/**
	 * Catches guild info commands, e.g. 'barbarian info' and stores the guild
	 * name from that command.
	 */
	private class GuildCommandProcessor extends AbstractCommandProcessor {

		private Collection<IGuildNameListener> listeners;

		public GuildCommandProcessor(IGuildNameListener... listeners) {
			super("\\s*(.+)\\sinfo\\s*");
			this.listeners = new ArrayList<IGuildNameListener>(
					Arrays.asList(listeners));
		}

		@Override
		protected boolean process(Matcher m) {
			for (IGuildNameListener l : listeners) {
				l.setGuild(m.group(1));
			}
			return false; // Always forward the command to client
		}

	}

	/**
	 * Catches goal command without parameters and prints list of possible goal
	 * skills
	 */
	private class GoalCommandWithoutParametersProcessor extends
			AbstractCommandProcessor {
		public GoalCommandWithoutParametersProcessor() {
			super("\\s*goal\\s*");
		}

		@Override
		protected boolean process(Matcher m) {
			for (String skillName : data.getSkills().keySet())
				printMessage("%s%s", skillName,
						data.isGoalSkill(skillName) ? " (*)" : "");
			return true;
		}
	}

	/**
	 * Catches parameterised goal command, e.g. 'goal attack' and sets the goal
	 * if possible
	 */
	private class GoalCommandProcessor extends AbstractCommandProcessor {
		public GoalCommandProcessor() {
			super("goal\\s*(.+)\\s*");
		}

		@Override
		protected boolean process(Matcher m) {
			String goalParameter = m.group(1);
			// If a skill is given as goal parameter, normalize skill name and
			// set goal
			data.setGoalSkill(normalizeSkillName(goalParameter));
			if (!data.getSkills().containsKey(data.goalSkill)) {
				printMessage("%s not in library", data.goalSkill);
			} else {
				printMessage("Next goal is %s", data.goalSkill);
				data.goalPercent = data.skillStatuses.get(data.goalSkill);
			}
			return true; // Stop command from being processed by client
		}

		/**
		 * Removes extra whitespaces and puts to lowercase
		 * 
		 * @param originalSkillName
		 * @return normalized skill name
		 */
		private String normalizeSkillName(String originalSkillName) {
			StringBuilder sb = new StringBuilder();
			for (String s : originalSkillName.split("\\s")) {
				sb.append(s);
				sb.append(" ");
			}
			return sb.toString().trim().toLowerCase();
		}
	}

	@SuppressWarnings("serial")
	public BatMUDGoalsPlugin() {
		data = new BatMUDGoalsPluginData();
		final PlayerLevelOutputProcessor playerLevelOutputProcessor = new PlayerLevelOutputProcessor();
		final InfoCommandSkillMaxOutputProcessor infoCommandSkillMaxOutputProcessor = new InfoCommandSkillMaxOutputProcessor();
		final PercentCostOutputProcessor percentCostOutputProcessor = new PercentCostOutputProcessor();

		commandProcessors = new ArrayList<AbstractCommandProcessor>() {
			{
				add(new GuildCommandProcessor(playerLevelOutputProcessor,
						infoCommandSkillMaxOutputProcessor));
				add(new GoalCommandWithoutParametersProcessor());
				add(new GoalCommandProcessor());
			}
		};
		outputProcessors = new ArrayList<AbstractCommandProcessor>() {
			{
				add(new TrainCommandOutputProcessor());
				add(percentCostOutputProcessor);
				add(new TrainedSkillOutputProcessor());
				add(new CostOfTrainingSkillNameOutputProcessor(
						percentCostOutputProcessor));
				add(new ExpCommandOutputProcessor());
				add(playerLevelOutputProcessor);
				add(new InfoCommandFirstLevelProcessor(
						infoCommandSkillMaxOutputProcessor));
				add(new InfoCommandLevelNumberProcessor(
						infoCommandSkillMaxOutputProcessor));
				add(infoCommandSkillMaxOutputProcessor);
			}
		};
	}

	/*
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger#trigger
	 * (java.lang.String)
	 */
	@Override
	public String trigger(String input) {
		for (AbstractCommandProcessor cp : commandProcessors) {
			if (cp.receive(input)) {
				return "";
			}
		}
		return null;
	}

	/*
	 * Catch output from 'cost train skill' and 'train' commands (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginTrigger#trigger(com
	 * .mythicscape.batclient.interfaces.ParsedResult)
	 */
	@Override
	public ParsedResult trigger(ParsedResult input) {
		String originalText = input.getOriginalText();
		for (AbstractCommandProcessor op : outputProcessors) {
			op.receive(originalText);
		}

		return input; // return input to be processed by the client
	}

	/**
	 * Processes output from guildcommand info command - e.g. 'ranger info'.
	 * Stores the player level in that guild.
	 */
	private class PlayerLevelOutputProcessor extends AbstractCommandProcessor
			implements IGuildNameListener {

		private String guild;

		public PlayerLevelOutputProcessor() {
			super("Your level:\\s+(\\d+)\\s+");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see batmudgoalsplugin.GuildNameListener#setGuild(java.lang.String)
		 */
		@Override
		public void setGuild(String guild) {
			this.guild = guild;
		}

		@Override
		protected boolean process(Matcher m) {
			data.getGuildlevels().put(guild, Integer.parseInt(m.group(1)));
			return false;
		}
	}

	/**
	 * Processes output from guildname info command - e.g. 'barbarian info'.
	 * First level skills are given after a message 'Abilities gained when
	 * joining:'
	 */
	private class InfoCommandFirstLevelProcessor extends
			AbstractCommandProcessor {

		private InfoCommandSkillMaxOutputProcessor op;

		public InfoCommandFirstLevelProcessor(
				InfoCommandSkillMaxOutputProcessor op) {
			super("Abilities gained when joining:\\s+");
			this.op = op;
		}

		@Override
		protected boolean process(Matcher m) {
			op.setLevel(1);
			return false;
		}
	}

	/**
	 * Processes output from guildname info command - e.g. 'ranger info'. Skill
	 * maxes for each level are reported after a row containing the level
	 * number.
	 */
	private class InfoCommandLevelNumberProcessor extends
			AbstractCommandProcessor {

		private InfoCommandSkillMaxOutputProcessor op;

		public InfoCommandLevelNumberProcessor(
				InfoCommandSkillMaxOutputProcessor op) {
			super("\\s*Level\\s+(\\d+):\\s*");
			this.op = op;
		}

		@Override
		protected boolean process(Matcher m) {
			op.setLevel(Integer.parseInt(m.group(1)));
			return false;
		}
	}

	/**
	 * Processes output from guildname info command, e.g. 'ranger info'. Stores
	 * skill max from the output. This depends on the earlier rows printed by
	 * the client telling the guild name and guild level.
	 */
	private class InfoCommandSkillMaxOutputProcessor extends
			AbstractCommandProcessor implements IGuildNameListener {

		private int level;
		private String guild;

		public InfoCommandSkillMaxOutputProcessor() {
			super("\\s+May\\s+train\\s+skill\\s+(.+)\\s+to\\s+(\\d+)%\\s+");
		}

		public void setLevel(int level) {
			this.level = level;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see batmudgoalsplugin.IGuildNameListener#setGuild(java.lang.String)
		 */
		@Override
		public void setGuild(String guild) {
			this.guild = guild;
		}

		@Override
		protected boolean process(Matcher m) {
			data.getSkillMaxes().add(
					new SkillMaxInfo(guild, m.group(1).toLowerCase(), level,
							Integer.parseInt(m.group(2))));
			return false;
		}
	}

	/**
	 * Processes output from 'exp' command.
	 * 
	 * Exp command output is something like this: <br>
	 * Exp: 135670 Money: 0.00 Bank: 644404.00 Exp pool: 0<br>
	 * Takes exp amount, outputs goal skill, needed exp and amount missing from
	 * next percent
	 */
	private class ExpCommandOutputProcessor extends AbstractCommandProcessor {
		public ExpCommandOutputProcessor() {
			super(
					"Exp: (\\d+) Money: (\\d+)\\.?(\\d*) Bank: (\\d+)\\.?(\\d*) Exp pool: (\\d+)\\.?(\\d*)\\s+");
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
			if (data.isGoalSkillMaxed()) {
				printMessage("Goal %s: full", data.goalSkill);
			} else {
				// get skillmaxinfo for this skill
				Collection<SkillMaxInfo> skillmaxinfo = new ArrayList<SkillMaxInfo>();
				Collection<String> guilds = new HashSet<String>();
				for (SkillMaxInfo s : data.getSkillMaxes()) {
					if (data.isGoalSkill(s.skill)
							&& s.level <= data.getGuildlevels().get(s.guild)
							&& s.max >= data.getGoalPercent()) {
						skillmaxinfo.add(s);
						guilds.add(s.guild);
					}
				}

				if (skillmaxinfo.isEmpty()) {
					printMessage("Goal %s: needs level", data.goalSkill);
				} else {
					int neededExp = data.getImproveGoalSkillCost();
					int currentExp = Integer.parseInt(m.group(1));
					if (currentExp < neededExp) {
						printMessage("Goal %s: %d You need: %d",
								data.goalSkill, neededExp, neededExp
										- currentExp);
					} else {
						printMessage(
								"Goal %s: %d You have enough to advance in: %s",
								data.goalSkill, neededExp,
								concatGuildNames(guilds));
					}
				}

			}
			return false;
		}
	}

	private class TrainedSkillOutputProcessor extends AbstractCommandProcessor {
		public TrainedSkillOutputProcessor() {
			super(
					"You now have '([^']+)' at (\\d+)% without special bonuses.\\s+");
		}

		@Override
		protected boolean process(Matcher m) {
			data.skillStatuses.put(m.group(1).trim().toLowerCase(),
					Integer.parseInt(m.group(2).trim()));
			return false;
		}

	}

	private void printMessage(String message) {
		getClientGUI().printText("generic", String.format("%s\n", message));
	}

	private void printMessage(String format, Object... args) {
		printMessage(String.format(format, args));
	}

	/**
	 * Processes output from 'train' command. Stores the skill percents shown in
	 * the train skill table.
	 */
	private class TrainCommandOutputProcessor extends AbstractCommandProcessor {
		public TrainCommandOutputProcessor() {
			super(
					"\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+|\\(n/a\\))\\s+\\|\\s+");
		}

		@Override
		protected boolean process(Matcher m) {
			data.setSkillStatus(readSkillName(m), readSkillStatus(m));
			return false;
		}

		private int readSkillStatus(Matcher m) {
			return Integer.parseInt(m.group(2));
		}

		private String readSkillName(Matcher m) {
			return m.group(1).trim().toLowerCase();
		}
	}

	/**
	 * Processes output from 'cost train <skill>' command. Stores the experience
	 * costs of skill percents.
	 */
	private class PercentCostOutputProcessor extends AbstractCommandProcessor {

		private String skill;

		public PercentCostOutputProcessor() {
			super("\\|\\s+(\\d+)%\\s+=\\s+(\\d+)");
		}

		public void setSkill(String skill) {
			this.skill = skill;
		}

		@Override
		protected boolean decideReturn(Matcher m) {
			while (m.find()) {
				process(m);
			}
			return false;
		}

		@Override
		protected boolean process(Matcher m) {
			Map<Integer, Integer> skilltable = data.getSkills().get(skill);
			skilltable.put(Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)));
			return false;
		}
	}

	/**
	 * Processes output from 'cost train <skill>' command. Stores the skill name
	 * from the outputted table.
	 */
	private class CostOfTrainingSkillNameOutputProcessor extends
			AbstractCommandProcessor {

		private PercentCostOutputProcessor op;

		public CostOfTrainingSkillNameOutputProcessor(
				PercentCostOutputProcessor op) {
			super("\\|\\s+Cost\\s+of\\s+training\\s+([^\\|]+)\\s+\\|\\s+");
			this.op = op;
		}

		@Override
		protected boolean process(Matcher m) {
			String skill = m.group(1).toLowerCase().trim();
			op.setSkill(skill);
			if (!data.getSkills().containsKey(skill)) {
				data.getSkills().put(skill, new HashMap<Integer, Integer>());
			}
			return false;
		}
	}

	@Override
	public String getName() {
		return "BatMUDGoalsPlugin";
	}

	/*
	 * Load state from persistent storage (non-Javadoc)
	 * 
	 * @see com.mythicscape.batclient.interfaces.BatClientPlugin#loadPlugin()
	 */
	@Override
	public void loadPlugin() {
		try {
			data = (BatMUDGoalsPluginData) generateJAXBContext()
					.createUnmarshaller().unmarshal(createPersistenceFile());
		} catch (Exception e) {
			printMessage(e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * Persist current goal and stored skill information (non-Javadoc)
	 * 
	 * @see
	 * com.mythicscape.batclient.interfaces.BatClientPluginUtil#clientExit()
	 */
	@Override
	public void clientExit() {
		try {
			generateJAXBContext().createMarshaller().marshal(data,
					createPersistenceFile());
		} catch (JAXBException | IOException e) {
			printMessage(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * If file exists opens the file, otherwise a new one is created
	 * 
	 * @return {@link File} used to store plugin's state
	 * @throws IOException
	 */
	private File createPersistenceFile() throws IOException {
		File file = new File(getClientGUI().getBaseDirectory()
				+ "/conf/batmudgoalsplugin/BatMUDGoalsInfo.xml");
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	private JAXBContext generateJAXBContext() throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class);
		return ctx;
	}
}
