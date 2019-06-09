package alchemystics.menu.screen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import alchemystics.main.Main;
import alchemystics.menu.action.CloseButtonAction;
import alchemystics.menu.action.PlayButtonAction;
import alchemystics.menu.data.MenuData;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MenuData data;

	private JLabel title;
	private MenuButton closeButton;
	private MenuButton playButton;
	private JTextPane guide;

	private Color compColor;
	private Color backColor;

	public MenuPanel(MenuData menudata) {
		setBackground(Color.red);
		data = menudata;

		title = new JLabel();
		title.setText("Alchemystics");
		add(title);

		closeButton = new MenuButton("종료");
		closeButton.addActionListener(new CloseButtonAction());
		closeButton.setForeground(Color.white);
		closeButton.backColor = Color.RED;
		add(closeButton);

		playButton = new MenuButton("플레이");
		playButton.addActionListener(new PlayButtonAction());
		add(playButton);

		guide = new JTextPane();
		guide.setText("Alchemystics 플레이 방법\n"
				+ "여러가지 아이템을 합쳐서 모든 아이템을 만드세요.\n"
				+ "하단 메뉴 버튼 조작 방법\n"
				+ "- 위로 드래그: 화면 지우기\n"
				+ "- 오래 누르기(붉은색): 힌트 알려주고 아이템 꺼내기\n"
				+ "- 클릭: 힌트 알려주기");
		guide.setForeground(Color.white);
		guide.setEditable(false);
		guide.setOpaque(false);
		add(guide);

		MenuLayoutManager layout = new MenuLayoutManager();
		layout.panel = this;
		layout.guides = guide;
		layout.title = title;
		layout.closeButton = closeButton;
		layout.playButton = playButton;
		guide.setFont(Main.defaultFont.deriveFont(0, 30));
		setLayout(layout);

		refreshFonts();
	}

	public void refreshFonts() {
		title.setFont(Main.defaultFont.deriveFont(0, 100));
		closeButton.setFont(Main.defaultFont.deriveFont(0, 30));
		playButton.setFont(Main.boldFont.deriveFont(0, 60));
	}

	public void playGame() {

	}

	@Override
	protected void paintComponent(Graphics g) {
		// Rainbow Color
		long time = System.currentTimeMillis() % data.animationTime;
		backColor = Color.getHSBColor((float) time / (float) data.animationTime, 1, 1);
		g.setColor(backColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		compColor = new Color(255 - backColor.getRed(), 255 - backColor.getGreen(), 255 - backColor.getBlue());
		title.setForeground(compColor);

		playButton.setForeground(backColor);
		playButton.backColor = compColor;
	}
}
