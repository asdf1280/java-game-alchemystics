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

public class ImageVisibleItem extends PaintableItem {
	private static final long serialVersionUID = 1L;

	public String name;
	public BufferedImage icon;

	public ImageVisibleItem(int code) {
		LoadedGameData lgd = GamePanel.gameData;
		Item i = lgd.items.get(code);
		System.out.println(i.key);
		name = lgd.languageKeys.get(i.key);
		icon = lgd.images.get(i.id);
		itemcode = code;
		setBorder(null);
	}

	float fontSize = 0f;
	public ItemMouseHandler ml;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int sizebase = Math.min(getWidth(), getHeight());
		int divideInto = 7;
		int xyPos = sizebase / divideInto;
		int size = sizebase * (divideInto - 2) / divideInto;

		if(highlighted) {
			g2.setColor(new Color(200, 200, 200));
		}else {
			g2.setColor(new Color(240, 240, 240));
		}
		g2.fillRoundRect((getWidth() - sizebase) / 2, (getHeight() - sizebase) / 2, sizebase, sizebase, 50, 50);

		if (icon == null) {
			ImageLoader.addLoad(itemcode);
			if (GamePanel.gameData.images.containsKey(itemcode)) {
				icon = GamePanel.gameData.images.get(itemcode);
			}
		}
		g2.drawImage(icon, xyPos, 0, size, size, this);

		String txt = name;
		if (fontSize == 0f) {
			fontSize = sizebase / 5;
			g2.setFont(Main.defaultFont.deriveFont(1, fontSize));
			FontMetrics fm = g2.getFontMetrics();
			while (fm.stringWidth(txt) >= getWidth() - 5) {
				fontSize -= 0.1f;
				g2.setFont(Main.defaultFont.deriveFont(1, fontSize));
				fm = g2.getFontMetrics();
			}
		}
		g2.setFont(Main.defaultFont.deriveFont(1, fontSize));
		g2.setColor(Color.BLACK);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() - 3);

//		if (highlighted) {
//			g.drawImage(BasicResource.duplicated, (getWidth() - sizebase) / 2, (getHeight() - sizebase) / 2, sizebase,
//					sizebase, null);
//		}
//		if (attacking) {
//			g.drawImage(BasicResource.attacking, (getWidth() - sizebase) / 2, (getHeight() - sizebase) / 2, sizebase,
//					sizebase, null);
//		}
	}

	public Point getCenter() {
		return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public void setCenter(int x, int y) {
		setLocation(x - getWidth() / 2, y - getHeight() / 2);
	}

	public void setCenter(Point p) {
		setCenter(p.x, p.y);
	}

	public int getLeft() {
		int xyPos = Math.min(getWidth(), getHeight()) / 7;
		return getX() + xyPos;
	}

	public int getRight() {
		int sizebase = Math.min(getWidth(), getHeight());
		int divideInto = 7;
		int xyPos = sizebase / divideInto;
		int size = sizebase * (divideInto - 2) / divideInto;
		return getX() + xyPos + size;
	}

	public int getTop() {
		return getY();
	}

	public int getBottom() {
		int sizebase = Math.min(getWidth(), getHeight());
		int divideInto = 7;
		int size = sizebase * (divideInto - 2) / divideInto;
		return getY() + size;
	}
//	
//	@Override
//	public void setLocation(int x, int y) {
//		new Exception().printStackTrace();
//		super.setLocation(x, y);
//	}
}
