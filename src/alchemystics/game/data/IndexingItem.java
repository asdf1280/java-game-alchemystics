package alchemystics.game.data;

public class IndexingItem extends Item {
	public String indexName;
	public String sortName;

	@Override
	public String getName() {
		return sortName;
	}
}
