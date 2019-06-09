package alchemystics.load.action;

import alchemystics.game.action.GamePlayUtils;
import alchemystics.game.data.GamePlayData;
import alchemystics.game.data.LoadedGameData;
import alchemystics.game.screen.GamePanel;
import alchemystics.load.screen.LoadPanel;
import alchemystics.main.Main;

public class LoadingEventImpl implements LoadingEventListener {

	private LoadPanel panel;

	public LoadingEventImpl(LoadPanel lp) {
		panel = lp;
	}

	@Override
	public void loadingProgressChanged(int progress) {
		panel.setProgress(progress);
	}
	
	private GamePlayData gpd = new GamePlayData();

	@Override
	public void loadingFinished(LoadedGameData data) {
		// TODO loading finished
		GamePanel.gameData = data;

		// TODO show play screen

		GamePanel gp = new GamePanel(gpd);
		gpd.data = new GamePlayUtils(gpd);
		Main.appInstance.frame.setContentPane(gp);
		Main.appInstance.frame.validate();
		gp.initializeComponents();
	}

	@Override
	public void loadingFailed() {
		// TODO Auto-generated method stub
	}

	@Override
	public GamePlayData getGamePlayData() {
		// TODO Auto-generated method stub
		return gpd;
	}

}
