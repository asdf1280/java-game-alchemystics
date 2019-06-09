package alchemystics.menu.screen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class MenuButton extends JButton {
	private static final long serialVersionUID = 3581079109814017451L;

	public MenuButton(String text) {
		setText(text);
		setBorder(null);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		backColor = Color.black;
	}
	
	public Color backColor;
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(backColor);
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		super.paintComponent(g);
	}
}
