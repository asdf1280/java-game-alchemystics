package alchemystics.game.screen;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import alchemystics.game.data.IndexingItem;
import alchemystics.main.Main;

public class IndexableItem extends PaintableItem {

	private static final long serialVersionUID = -2561730062491204348L;

	private IndexingItem ii;

	public IndexableItem(IndexingItem item) {
		ii = item;
	}

	float fontSize = 0;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int sizebase = Math.min(getWidth(), getHeight());
		g2.setPaint(Color.white);
		g2.fillRoundRect((getWidth() - sizebase) / 2, (getHeight() - sizebase) / 2, sizebase, sizebase, 70, 70);

		String txt = ii.indexName;
		if (fontSize == 0f) {
			fontSize = sizebase;
		}
		g2.setFont(Main.boldFont.deriveFont(0, fontSize));
		FontMetrics fm = g2.getFontMetrics();
		while (fm.stringWidth(txt) >= sizebase - 20) {
			fontSize -= 0.1f;
			g2.setFont(Main.boldFont.deriveFont(0, fontSize));
			fm = g2.getFontMetrics();
		}

		g2.setFont(Main.boldFont.deriveFont(0, fontSize));
		g2.setColor(new Color(0, 120, 250));
		fm = g2.getFontMetrics();
		g2.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2,
				(getHeight() - fm.getHeight()) / 2 + fm.getAscent());
	}

}
