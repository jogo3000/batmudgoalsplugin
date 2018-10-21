package batmudgoalsplugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.ParsedResult;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Plugin for BatClient. Player can set a goal of improving a skill in her
 * guild. Experience needed to reach the next percent is then shown upon 'exp'
 * command.
 * 
 * @author Jogo
 */
public class BatMUDGoalsController
        implements BatClientPluginCommandTrigger, BatClientPluginTrigger {

    private final Logger logger;
    private BatMUDGoalsPluginData data;
    private Collection<AbstractCommandProcessor> commandProcessors;
    private Collection<AbstractOutputProcessor> outputProcessors;
    private final ClientGUIModel clientGUIModel;

    public BatMUDGoalsController(Logger logger, BatMUDGoalsPluginData data, ClientGUIModel clientGUIModel)
            throws SecurityException, IOException {
        this.logger = logger;
        this.data = data;
        this.clientGUIModel = clientGUIModel;

        initializeCommandProcessors();
    }

    private void initializeCommandProcessors() {
        logger.info("Initalizing command processors");
        final PlayerLevelOutputProcessor playerLevelOutputProcessor = new PlayerLevelOutputProcessor(data);
        final InfoCommandSkillMaxOutputProcessor infoCommandSkillMaxOutputProcessor = new InfoCommandSkillMaxOutputProcessor(
                data);
        final PercentCostOutputProcessor percentCostOutputProcessor = new PercentCostOutputProcessor(data);

        commandProcessors = Arrays.asList(
                new GuildCommandProcessor(playerLevelOutputProcessor, infoCommandSkillMaxOutputProcessor),
                new GoalCommandWithoutParametersProcessor(clientGUIModel, data),
                new GoalCommandProcessor(clientGUIModel, data));

        outputProcessors = Arrays.asList(new TrainCommandOutputProcessor(data),
                percentCostOutputProcessor,
                new TrainedSkillOutputProcessor(data),
                new CostOfTrainingSkillNameOutputProcessor(percentCostOutputProcessor),
                new ExpCommandOutputProcessor(clientGUIModel, data),
                playerLevelOutputProcessor,
                new InfoCommandFirstLevelProcessor(infoCommandSkillMaxOutputProcessor),
                new InfoCommandLevelNumberProcessor(infoCommandSkillMaxOutputProcessor),
                infoCommandSkillMaxOutputProcessor,
                new ImproveSkillByUseOutputProcessor(data),
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
            for (AbstractOutputProcessor op : outputProcessors) {
                op.receive(originalText);
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, t.getMessage(), t);
        }

        return input; // return input to be processed by the client
    }
}
