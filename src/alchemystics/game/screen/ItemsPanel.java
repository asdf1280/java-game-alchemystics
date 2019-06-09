package alchemystics.game.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JPanel;

import alchemystics.game.action.GamePlayUtils;
import alchemystics.game.data.GamePlayData;
import alchemystics.game.data.IndexingItem;
import alchemystics.game.data.Item;

public class ItemsPanel extends JPanel {
	private static final long serialVersionUID = -125271801944986585L;

	GamePlayData gpd;

	public ItemsPanel(GamePlayData gpd) {
		this.gpd = gpd;
	}

	long whiteTime = 0;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (whiteTime > System.currentTimeMillis()) {
			int d = (int) (whiteTime - System.currentTimeMillis());
			d = Math.min(200, d);
			g.setColor(new Color(255, 255, 255, d * 255 / 200));
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	public void newItemAdded(ItemMouseChecker imc) {
		whiteTime = System.currentTimeMillis() + 10000;
		layout(null, imc);
		whiteTime = System.currentTimeMillis() + 100;
	}

	public void saveGame() {
		GamePlayUtils.saveGame(gpd.save);
	}

	private Dimension lastLayoutSize = null;
	static int items_per_line = 5;
	private ItemMouseChecker latestimc;

	public void layout(Dimension size, ItemMouseChecker imc) {
		if (size == null) {
			size = lastLayoutSize;
		} else if (lastLayoutSize == null) {
			lastLayoutSize = size;
		}
		if (imc != null) {
			latestimc = imc;
		} else {
			imc = latestimc;
		}
		removeAll();
//		LoadedGameData loadedGameData = GamePanel.gameData;
		int itemSquareSize = size.width / items_per_line;
		int verticalItemLines = size.height / itemSquareSize;

		if (gpd.save.unlocked.isEmpty()) {
			for (Integer i : GamePanel.gameData.items.keySet()) {
				Item item = GamePanel.gameData.items.get(i);
				if (item.prime)
					gpd.save.unlocked.add(i);
			}
		}

		ArrayList<Integer> al = gpd.save.unlocked;
		HashMap<Integer, Item> shownItems = new HashMap<>();
		for (int a : al) {
			shownItems.put(a, GamePanel.gameData.items.get(a));
		}
		ArrayList<Item> itemList = new ArrayList<>();
		for (int index : shownItems.keySet()) {
			itemList.add(shownItems.get(index));
		}
		String[] shangul = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파",
				"하" };
		String[] hangul = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z", "ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ",
				"ㅎ" };
		for (int i = 0; i < shangul.length; i++) {
			IndexingItem item = new IndexingItem();
			item.indexName = hangul[i];
			item.sortName = shangul[i];
			itemList.add(item);
		}
		Collections.sort(itemList);
		//@formatter:off
		/*
		 * gpd.save.unlocked
		 * loadedGameData.items
		 * shownItems.keySet()
		 */
		//@formatter:on
		int items = itemList.size();
		verticalItemLines = Math.max(verticalItemLines, items / items_per_line + 1);

		GridLayout layout = new GridLayout(verticalItemLines, items_per_line + 1);
		setLayout(layout);
		int totalSize = verticalItemLines * items_per_line;
//		for (int index : gpd.save.unlocked) {
		for (Item item : itemList) {
			whiteTime = System.currentTimeMillis() + 10000;
			PaintableItem ci = null;
			if (item instanceof IndexingItem) {
				ci = new IndexableItem((IndexingItem) item);
			} else {
				ci = new CraftableItem(item.id);
				ItemMouseHandler imh = ItemMouseHandler.getNewOne(ci, imc);
				ci.addMouseListener(imh);
				ci.addMouseMotionListener(imh);
			}
			ci.setPreferredSize(new Dimension(itemSquareSize, itemSquareSize));
			add(ci);
			totalSize--;
		}
		whiteTime = System.currentTimeMillis() + 400;
		while (totalSize-- > 0) {
			InvisibleComponent d = new InvisibleComponent();
			d.setPreferredSize(new Dimension(itemSquareSize, itemSquareSize));
			add(d);
		}
		setPreferredSize(new Dimension(0, verticalItemLines * itemSquareSize));
		validate();
	}
}
