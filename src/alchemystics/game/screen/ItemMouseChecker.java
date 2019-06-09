package alchemystics.game.screen;

import java.awt.event.MouseEvent;

public abstract class ItemMouseChecker {
	public abstract void mousePressed(MouseEvent e, PaintableItem item);
	public abstract void mouseReleased(MouseEvent e, PaintableItem item);
	public abstract void mouseDragged(MouseEvent e, PaintableItem item);
}
