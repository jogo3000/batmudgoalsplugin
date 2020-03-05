package batmudgoalsplugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
 */
public class BatMUDGoalsPlugin extends BatClientPlugin
        implements BatClientPluginCommandTrigger, BatClientPluginTrigger, BatClientPluginUtil {

    private final Logger logger;
    private BatMUDGoalsPluginData data;
    private final ClientGUIModel clientGUIModel;

    private BatMUDGoalsController model;

    /**
     * Needed by the plugin framework.
     *
     * @throws SecurityException
     * @throws IOException
     */
    public BatMUDGoalsPlugin() throws SecurityException, IOException {
        logger = Logger.getLogger(getClass().toString());
        FileHandler handler = new FileHandler("%t/batmudgoalsplugin.log");
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        clientGUIModel = new ClientGUIModel(this);
    }

    /*
     * @see
     * com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger#trigger
     * (java.lang.String)
     */
    @Override
    public String trigger(String input) {
        return model.trigger(input);
    }

    /*
     * Catch output from 'cost train skill' and 'train' commands (non-Javadoc)
     *
     * @see com.mythicscape.batclient.interfaces.BatClientPluginTrigger#trigger(com
     * .mythicscape.batclient.interfaces.ParsedResult)
     */
    @Override
    public ParsedResult trigger(ParsedResult input) {
        return model.trigger(input);
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
            data = BatMUDGoalsPluginData.fromFile(createPersistenceFile());
            model = new BatMUDGoalsController(logger, data, clientGUIModel);
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
            BatMUDGoalsPluginData.persistToFile(data, createPersistenceFile());
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
        File file = new File(clientGUIModel.baseDirectory() + "/conf/batmudgoalsplugin/BatMUDGoalsInfo.data");
        if (!file.exists()) {
            logger.info("Creating new file");
            File dir = file.getParentFile();
            dir.mkdirs();
            file.createNewFile();
        }
        return file;
    }
}
