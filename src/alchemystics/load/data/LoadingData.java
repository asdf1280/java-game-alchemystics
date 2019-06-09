package alchemystics.load.data;

public class LoadingData {
	public String currentText = "불러오는 중...";
	public int progress = 0;
	public LoadingDataChanged changed = null;
	
	public static interface LoadingDataChanged {
		public void textChanged();
	}
}
