package alchemystics.load.screen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import alchemystics.load.data.LoadingData;
import alchemystics.load.data.LoadingData.LoadingDataChanged;
import alchemystics.main.Main;

public class LoadPanel extends JPanel {
	private static final long serialVersionUID = 8304382763460556925L;
	public LoadingData data;
	private Color background = new Color(255, 200, 0);
	private JLabel title;
	private JProgressBar progress;

	public LoadPanel(LoadingData ld) {
		data = ld;
		title = new JLabel();
		title.setForeground(Color.white);
		add(title);
		
		progress = new JProgressBar(0, 0, 100);
		progress.setValue(0);
		progress.setBorderPainted(false);
		add(progress);

		LoadLayoutManager layout = new LoadLayoutManager();
		layout.panel = this;
		layout.title = title;
		layout.progress = progress;
		setLayout(layout);

		refreshFonts();
		refreshText();
		data.changed = new LoadingDataChanged() {
			
			@Override
			public void textChanged() {
				refreshText();
			}
		};
	}

	public void refreshFonts() {
		title.setFont(Main.defaultFont.deriveFont(1, 80));
	}
	
	public void setProgress(int n) {
		progress.setValue(n);
	}
	
	public void refreshText() {
		title.setText(data.currentText);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		progress.setForeground(Color.getHSBColor((float)progress.getValue() / (float)progress.getMaximum(), 1, 1));
	}
}
