package batmudgoalsplugin;

import com.mythicscape.batclient.interfaces.ClientGUI;

public class ClientGUIModel {

    private ClientGUI gui;
    private final BatMUDGoalsPlugin plugin;

    public ClientGUIModel(BatMUDGoalsPlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin is a mandatory argument");
        }
        this.plugin = plugin;
    }

    private ClientGUI getGui() {
        if (gui == null) {
            gui = plugin.getClientGUI();
        }
        return gui;
    }

    public String baseDirectory() {
        return getGui().getBaseDirectory();
    }

    public void printText(String string, String format) {
        getGui().printText(string, format);
    }
}
