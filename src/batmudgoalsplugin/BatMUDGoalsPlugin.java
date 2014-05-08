package batmudgoalsplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

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
@SuppressWarnings("serial")
public class BatMUDGoalsPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger, BatClientPluginTrigger,
		BatClientPluginUtil {

	final Logger logger;
	private BatMUDGoalsPluginData data;
	private Collection<AbstractCommandProcessor> commandProcessors;
	private Collection<AbstractCommandProcessor> outputProcessors;

	public BatMUDGoalsPlugin() throws SecurityException, IOException {
		logger = Logger.getLogger(getClass().toString());
		logger.addHandler(new FileHandler("%t/batmudgoalsplugin.log"));
		data = new BatMUDGoalsPluginData();
	}

	void initializeCommandProcessors() {
		final PlayerLevelOutputProcessor playerLevelOutputProcessor = new PlayerLevelOutputProcessor(
				data);
		final InfoCommandSkillMaxOutputProcessor infoCommandSkillMaxOutputProcessor = new InfoCommandSkillMaxOutputProcessor(
				data);
		final PercentCostOutputProcessor percentCostOutputProcessor = new PercentCostOutputProcessor(
				data);

		final BatClientPlugin plugin = this;

		commandProcessors = new ArrayList<AbstractCommandProcessor>() {
			{
				add(new GuildCommandProcessor(playerLevelOutputProcessor,
						infoCommandSkillMaxOutputProcessor));
				add(new GoalCommandWithoutParametersProcessor(plugin, data));
				add(new GoalCommandProcessor(plugin, data));
			}
		};
		outputProcessors = new ArrayList<AbstractCommandProcessor>() {
			{
				add(new TrainCommandOutputProcessor(data));
				add(percentCostOutputProcessor);
				add(new TrainedSkillOutputProcessor(data));
				add(new CostOfTrainingSkillNameOutputProcessor(
						percentCostOutputProcessor, data));
				add(new ExpCommandOutputProcessor(plugin, data));
				add(playerLevelOutputProcessor);
				add(new InfoCommandFirstLevelProcessor(
						infoCommandSkillMaxOutputProcessor, data));
				add(new InfoCommandLevelNumberProcessor(
						infoCommandSkillMaxOutputProcessor, data));
				add(infoCommandSkillMaxOutputProcessor);
				add(new ImproveSkillByUseOutputProcessor(data));
				add(new TrainedPartiallyOutputProcessor(data));
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
		try {
			for (AbstractCommandProcessor cp : commandProcessors) {
				if (cp.receive(input)) {
					return "";
				}
			}
		} catch (Throwable t) {
			logger.log(Level.SEVERE, t.getMessage(), t);
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
		try {
			String originalText = input.getOriginalText();
			for (AbstractCommandProcessor op : outputProcessors) {
				op.receive(originalText);
			}
		} catch (Throwable t) {
			logger.log(Level.SEVERE, t.getMessage(), t);
		}

		return input; // return input to be processed by the client
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
			initializeCommandProcessors();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, t.getMessage(), t);
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
		} catch (Throwable t) {
			logger.log(Level.SEVERE, t.getMessage(), t);
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
		if (!file.exists()) {
			File dir = file.getParentFile();
			dir.mkdirs();
			file.createNewFile();
		}
		return file;
	}

	private JAXBContext generateJAXBContext() throws JAXBException {
		JAXBContext ctx = JAXBContext.newInstance(BatMUDGoalsPluginData.class);
		return ctx;
	}
}
