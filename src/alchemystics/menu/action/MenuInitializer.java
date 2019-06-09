package alchemystics.menu.action;

import alchemystics.abstractclasses.GuiInitializer;
import alchemystics.menu.data.MenuData;
import alchemystics.menu.screen.MenuPanel;

public class MenuInitializer extends GuiInitializer<MenuData, MenuPanel> {
	public MenuInitializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MenuData getData() {
		MenuData md = new MenuData();
		return md;
	}

	@Override
	public MenuPanel getPanel() {
		MenuData md = getData();
		MenuPanel mp = new MenuPanel(md);
		return mp;
	}
}
