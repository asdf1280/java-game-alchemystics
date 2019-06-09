package alchemystics.load.screen;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadLayoutManager implements LayoutManager {

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

	public LoadPanel panel;
	public JLabel title;
	public JProgressBar progress;

	@Override
	public void layoutContainer(Container parent) {
		title.setSize(title.getPreferredSize());
		title.setLocation((panel.getWidth() - title.getWidth()) / 2, panel.getHeight() / 2 - title.getHeight());
		
		progress.setSize(panel.getWidth() / 2, panel.getHeight() / 20);
		progress.setLocation(panel.getWidth() / 4, panel.getHeight() / 2);
	}

}
