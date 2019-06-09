package alchemystics.main;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import alchemystics.menu.action.MenuInitializer;

/**
 * Opens window and call menu screen
 * 
 * @author User
 *
 */
public class App {
	public JFrame frame;

	public App() {
		openFrame();
		openFirstScreen();
		startPaintThread();
	}

	private void openFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
//		frame.setVisible(true);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		DisplayMode cdm = gd.getDisplayMode();
		DisplayMode dm = new DisplayMode(cdm.getWidth(), cdm.getHeight(), cdm.getBitDepth(), cdm.getRefreshRate());
		gd.setFullScreenWindow(frame);
		gd.setDisplayMode(dm);
	}

	private void openFirstScreen() {
		MenuInitializer mi = new MenuInitializer();
		frame.setContentPane(mi.getPanel());
	}

	private void startPaintThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (frame.isDisplayable()) {
						frame.repaint();
						frame.validate();
					}else {
						Utils.sleep(300);
					}
				}
			}
		}).start();
	}
}
