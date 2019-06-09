package alchemystics.load.action;

import alchemystics.game.data.GamePlayData;
import alchemystics.game.data.LoadedGameData;

public interface LoadingEventListener {
	public void loadingProgressChanged(int progress);
	public void loadingFinished(LoadedGameData data);
	public void loadingFailed();
	public GamePlayData getGamePlayData();
}
