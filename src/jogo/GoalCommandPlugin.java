package jogo;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginCommandTrigger;

public class GoalCommandPlugin extends BatClientPlugin implements
		BatClientPluginCommandTrigger {

	@Override
	public String trigger(String arg0) {
		getClientGUI().printText("generic", arg0);
		return arg0;
	}

	@Override
	public String getName() {
		return "jogo.BatMUDGoalsPlugin";
	}

	@Override
	public void loadPlugin() {
		// TODO Auto-generated method stub

	}

}
