package alchemystics.game.screen;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ItemMouseHandler extends MouseAdapter {

	public static ItemMouseHandler getNewOne(PaintableItem pi, ItemMouseChecker imc) {
		ItemMouseHandler imh = new ItemMouseHandler() {

			@Override
			public void mouseReleased(MouseEvent e, PaintableItem item) {
				imc.mouseReleased(e, item);
			}

			@Override
			public void mousePressed(MouseEvent e, PaintableItem item) {
				imc.mousePressed(e, item);
			}

			@Override
			public void mouseDragged(MouseEvent e, PaintableItem item) {
				imc.mouseDragged(e, item);
			}
		};
		imh.item = pi;
		return imh;
	}

	public PaintableItem item;

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed(e, item);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseReleased(e, item);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDragged(e, item);
	}

	public abstract void mousePressed(MouseEvent e, PaintableItem item);

	public abstract void mouseReleased(MouseEvent e, PaintableItem item);

	public abstract void mouseDragged(MouseEvent e, PaintableItem item);
}
