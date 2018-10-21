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

    /**
     * Prints message to the batclient text input area
     * 
     * @param message
     */
    protected void printMessage(String message) {
        getGui().printText("generic", String.format("%s%n", message));
    }

}
