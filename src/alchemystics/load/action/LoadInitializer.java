package alchemystics.load.action;

import alchemystics.abstractclasses.GuiInitializer;
import alchemystics.load.data.LoadingData;
import alchemystics.load.screen.LoadPanel;

public class LoadInitializer extends GuiInitializer<LoadingData, LoadPanel>{

	@Override
	public LoadingData getData() {
		LoadingData ld = new LoadingData();
		
		return ld;
	}

	@Override
	public LoadPanel getPanel() {
		LoadingData ld = getData();
		LoadPanel lp = new LoadPanel(ld);
		
		return lp;
	}
	
}
