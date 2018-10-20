package batmudgoalsplugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.ParsedResult;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Plugin for BatClient. Player can set a goal of improving a skill in her
 * guild. Experience needed to reach the next percent is then shown upon 'exp'
 * command.
 * 
 * @author Jogo
 */
public class BatMUDGoalsPlugin extends BatClientPlugin
        implements BatClientPluginCommandTrigger, BatClientPluginTrigger, BatClientPluginUtil {

    private final Logger logger;
    private BatMUDGoalsPluginData data;
    private Collection<AbstractCommandProcessor> commandProcessors;
    private Collection<AbstractCommandProcessor> outputProcessors;
    private final ClientGUIModel clientGUIModel;

    public BatMUDGoalsPlugin() throws SecurityException, IOException {
        logger = Logger.getLogger(getClass().toString());
        configureLogFile();
        data = new BatMUDGoalsPluginData();
        clientGUIModel = new ClientGUIModel(this);
    }

    private void configureLogFile() throws IOException {
        FileHandler handler = new FileHandler("%t/batmudgoalsplugin.log");
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
    }

    public BatMUDGoalsPlugin(BatMUDGoalsPluginData data, ClientGUIModel clientGUIModel)
            throws SecurityException, IOException {
        logger = Logger.getLogger(getClass().toString());
        configureLogFile();
        this.data = data;
        this.clientGUIModel = clientGUIModel;
    }

    void initializeCommandProcessors() {
        logger.info("Initalizing command processors");
        final PlayerLevelOutputProcessor playerLevelOutputProcessor = new PlayerLevelOutputProcessor(data);
        final InfoCommandSkillMaxOutputProcessor infoCommandSkillMaxOutputProcessor = new InfoCommandSkillMaxOutputProcessor(
                data);
        final PercentCostOutputProcessor percentCostOutputProcessor = new PercentCostOutputProcessor(data);

        commandProcessors = Arrays.asList(
                new GuildCommandProcessor(playerLevelOutputProcessor, infoCommandSkillMaxOutputProcessor),
                new GoalCommandWithoutParametersProcessor(clientGUIModel, data),
                new GoalCommandProcessor(clientGUIModel, data));

        outputProcessors = Arrays.asList(new TrainCommandOutputProcessor(data), percentCostOutputProcessor,
                new TrainedSkillOutputProcessor(data),
                new CostOfTrainingSkillNameOutputProcessor(percentCostOutputProcessor, data),
                new ExpCommandOutputProcessor(clientGUIModel, data), playerLevelOutputProcessor,
                new InfoCommandFirstLevelProcessor(infoCommandSkillMaxOutputProcessor, data),
                new InfoCommandLevelNumberProcessor(infoCommandSkillMaxOutputProcessor, data),
                infoCommandSkillMaxOutputProcessor, new ImproveSkillByUseOutputProcessor(data),
                new TrainedPartiallyOutputProcessor(data));
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
     * @see com.mythicscape.batclient.interfaces.BatClientPluginTrigger#trigger(com
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
            logger.info("loading plugin");
            try {

                data = (BatMUDGoalsPluginData) generateJAXBContext().createUnmarshaller()
                        .unmarshal(createPersistenceFile());
            } catch (IOException | JAXBException e) {
                logger.log(Level.WARNING, "No configuration loaded, initializing with defaults", e);
                data = new BatMUDGoalsPluginData();
            }
            initializeCommandProcessors();
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getMessage(), t);
        }
    }

    /*
     * Persist current goal and stored skill information (non-Javadoc)
     * 
     * @see com.mythicscape.batclient.interfaces.BatClientPluginUtil#clientExit()
     */
    @Override
    public void clientExit() {
        try {
            generateJAXBContext().createMarshaller().marshal(data, createPersistenceFile());
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
        logger.info("Locating persistence file");
        File file = new File(clientGUIModel.baseDirectory() + "/conf/batmudgoalsplugin/BatMUDGoalsInfo.xml");
        if (!file.exists()) {
            logger.info("Creating new file");
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
