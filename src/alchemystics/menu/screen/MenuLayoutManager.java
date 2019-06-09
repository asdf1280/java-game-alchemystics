package alchemystics.menu.screen;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JTextPane;

public class MenuLayoutManager implements LayoutManager {
	public MenuPanel panel;
	public JLabel title;
	public MenuButton closeButton;
	public MenuButton playButton;
	public JTextPane guides;

	@Override
	public void addLayoutComponent(String name, Component comp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub

	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void layoutContainer(Container parent) {
		title.setSize(title.getPreferredSize());
		title.setLocation((panel.getWidth() - title.getWidth()) / 2, panel.getHeight() / 7);
		
		closeButton.setSize(panel.getWidth() / 9, panel.getHeight() / 10);
		closeButton.setLocation(panel.getWidth() - closeButton.getWidth(), panel.getHeight() - closeButton.getHeight());
		
		playButton.setSize(panel.getWidth() / 5, panel.getHeight() / 5);
		playButton.setLocation((panel.getWidth() - playButton.getWidth()) / 2, (panel.getHeight() - playButton.getHeight()) / 2);
		
		guides.setLocation(0, panel.getHeight() - 450);
		guides.setSize(900, 450);
	}
}
