package alchemystics.game.screen;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import alchemystics.game.data.Item;
import alchemystics.game.data.LoadedGameData;
import alchemystics.load.action.ImageLoader;
import alchemystics.main.Main;

public class CraftableItem extends PaintableItem {
	private static final long serialVersionUID = -6839292471344695424L;

	public String name;
	public BufferedImage icon;

	public CraftableItem(int code) {
		LoadedGameData lgd = GamePanel.gameData;
		Item i = lgd.items.get(code);
		System.out.println(i.key);
		name = lgd.languageKeys.get(i.key);
		this.itemcode = i.id;
		icon = lgd.images.get(i.id);
	}

	float fontSize = 0;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2.setColor(new Color(240, 240, 240));
//		g2.fillRect(0, 0, getWidth(), getHeight());

		int sizebase = Math.min(getWidth(), getHeight());
		int divideInto = 7;
		int xyPos = sizebase / divideInto;
		int size = sizebase * (divideInto - 2) / divideInto;
		if (icon == null) {
			ImageLoader.addLoad(itemcode);
			if (GamePanel.gameData.images.containsKey(itemcode)) {
				icon = GamePanel.gameData.images.get(itemcode);
			}
		}
		if (!highlighted)
			g2.drawImage(icon, xyPos, 0, size, size, this);

		String txt = name;
		if (fontSize == 0f) {
			fontSize = sizebase / 5;
		}
		g2.setFont(Main.boldFont.deriveFont(0, fontSize));
		FontMetrics fm = g2.getFontMetrics();
		while (fm.stringWidth(txt) >= getWidth() - 5) {
			fontSize -= 0.1f;
			g2.setFont(Main.boldFont.deriveFont(0, fontSize));
			fm = g2.getFontMetrics();
		}

		g2.setFont(Main.boldFont.deriveFont(0, fontSize));
		g2.setColor(Color.black);
		fm = g2.getFontMetrics();
		if (!highlighted)
			g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() - 3);
	}

	public Point getCenter() {
		return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}
}
