package alchemystics.menu.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import alchemystics.load.action.GameLoader;
import alchemystics.load.action.LoadInitializer;
import alchemystics.load.action.LoadingEventImpl;
import alchemystics.load.screen.LoadPanel;
import alchemystics.main.Main;

public class PlayButtonAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO show load screen
		LoadInitializer li = new LoadInitializer();
		LoadPanel lp = li.getPanel();
		Main.appInstance.frame.setContentPane(lp);
		// TODO load game
		GameLoader gl = new GameLoader(lp.data);
		gl.eventa.add(new LoadingEventImpl(lp));
		new Thread(gl).start();
	}

}
