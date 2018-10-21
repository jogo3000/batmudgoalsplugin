package batmudgoalsplugin;

import java.util.regex.Matcher;

import batmudgoalsplugin.data.BatMUDGoalsPluginData;

/**
 * Processes output from 'train' and 'study' commands. Stores the skill percents
 * shown in the train skill table. Example output from 'study' command: <code>
 * ,-------------------------------+-----------------+-------------|
 * | Spells available at level 35  | Cur | Rac | Max | Exp         |
 * |===============================|=====|=====|=====|=============|
 * | Cure light wounds             |  85 | 101 |  95 |       16940 |
 * | Detect alignment              |  50 | 101 | 100 |        2444 |
 * | Paranoia                      |   1 | 101 | 100 |          25 |</code>
 */
class TrainCommandOutputProcessor extends AbstractCommandProcessor {
    public TrainCommandOutputProcessor(BatMUDGoalsPluginData data) {
        super(
                "\\|\\s+([^\\|]+)\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+)\\s+\\|\\s+(\\d+|\\(n/a\\))\\s+\\|\\s*(?:\\(partially (?:trained|studied)\\))?\\s*",
                null, data);
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
        return normalizeSkillName(m.group(1));
    }
}