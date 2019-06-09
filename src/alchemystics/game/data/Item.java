package alchemystics.game.data;

import java.util.Comparator;

import alchemystics.game.screen.GamePanel;

public class Item implements Comparable<Item>, Comparator<Item> {
	public int id;
	public String key;
	public boolean prime;

	public String getName() {
		return GamePanel.gameData.languageKeys.get(key);
	}

	@Override
	public int compare(Item o1, Item o2) {
		return o1.getName().compareTo(o2.getName());
	}

	@Override
	public int compareTo(Item o) {
		return compare(this, o);
	}
}
