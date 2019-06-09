package alchemystics.load.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import alchemystics.game.data.BasicResource;
import alchemystics.game.data.Item;
import alchemystics.game.data.LoadedGameData;
import alchemystics.game.data.MergeNode;
import alchemystics.game.data.UserSavefile;
import alchemystics.load.data.LoadingData;

public class GameLoader implements Runnable {

	public ArrayList<LoadingEventListener> eventa = new ArrayList<>();

	LoadingData data;

	public GameLoader(LoadingData ld) {
		data = ld;
	}

	@Override
	public void run() {
		// TODO load
		Gson g = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
		LoadedGameData lgd = new LoadedGameData();
		lgd.images = new HashMap<>();
		lgd.items = new HashMap<>();
		lgd.languageKeys = new HashMap<>();
		lgd.mergeNodes = new ArrayList<>();
		data.currentText = "준비 중...";
		data.changed.textChanged();
		try {
			BasicResource.duplicated = ImageIO
					.read(getClass().getResourceAsStream("/assets/alchemystics/texture/duplicated.png"));
		} catch (Exception e) {
			e.printStackTrace();
			for (LoadingEventListener ev : eventa) {
				ev.loadingFailed();
			}
		}

		data.currentText = "저장 파일 불러오는 중...";
		data.changed.textChanged();
		try {
			File gameSave = new File("savefile.json");
			if (!gameSave.exists()) {
				FileUtils.writeStringToFile(gameSave, g.toJson(new UserSavefile()), "UTF8");
			}
			UserSavefile usf = g.fromJson(FileUtils.readFileToString(gameSave, "UTF8"), UserSavefile.class);
			for (LoadingEventListener ev : eventa) {
				ev.getGamePlayData().save = usf;
			}
			Collections.sort(usf.unlocked);
			FileUtils.writeStringToFile(gameSave, g.toJson(usf), "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
			for (LoadingEventListener ev : eventa) {
				ev.loadingFailed();
			}
		}

		// TODO later check for mods
		try {

		} catch (Exception e) {
		}

		try {
			loadDefaultMod(g, lgd);
		} catch (Exception e) {
			e.printStackTrace();
			for (LoadingEventListener ev : eventa) {
				ev.loadingFailed();
			}
		}
	}

	private int loadable = 1;
	private int loaded = 0;
	private int totalWorks = 3 * loadable;
	private int progress = 0;

	private void loadDefaultMod(Gson g, LoadedGameData lgd) throws Exception {
		data.currentText = "Default 불러오는 중...";
		data.changed.textChanged();

		refProgress(loadable, loaded, totalWorks, progress++);
		{
			HashMap<Integer, Item> tmp = g.fromJson(
					IOUtils.toString(getClass().getResourceAsStream("/assets/alchemystics/def/items.json"), "UTF8"),
					new TypeToken<HashMap<Integer, Item>>() {
					}.getType());
			for (Integer it : tmp.keySet()) {
				if (!lgd.items.containsKey(it)) {
					lgd.items.put(it, tmp.get(it));
				}
			}
			totalWorks += tmp.size();
		}
		refProgress(loadable, loaded, totalWorks, progress++);
		{
			ArrayList<MergeNode> tmp = g.fromJson(
					IOUtils.toString(getClass().getResourceAsStream("/assets/alchemystics/def/merges.json"), "UTF8"),
					new TypeToken<ArrayList<MergeNode>>() {
					}.getType());
			for (MergeNode it : tmp) {
				if (!lgd.mergeNodes.contains(it)) {
					lgd.mergeNodes.add(it);
				}
			}
		}
		refProgress(loadable, loaded, totalWorks, progress++);
		{
			HashMap<String, String> tmp = g.fromJson(
					IOUtils.toString(getClass().getResourceAsStream("/assets/alchemystics/lang/ko_kr.json"), "UTF8"),
					new TypeToken<HashMap<String, String>>() {
					}.getType());
			lgd.languageKeys.putAll(tmp);
		}

		refProgress(loadable, loaded, totalWorks, progress++);
//		{
//			for (int in : lgd.items.keySet()) {
//				Item i = lgd.items.get(in);
//				System.out.println("/assets/alchemystics/img/" + i.id + ".png");
//				InputStream stream = getClass().getResourceAsStream("/assets/alchemystics/img/" + i.id + ".png");
//				if (stream != null) {
//					lgd.images.put(i.id,
//							ImageIO.read(getClass().getResourceAsStream("/assets/alchemystics/img/" + i.id + ".png")));
//				} else {
//					lgd.images.put(i.id, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
//				}
//				refProgress(loadable, loaded, totalWorks, progress++);
//			}
//		}
//		refProgress(loadable, loaded, totalWorks, progress++);
		for (int i = 0; i < 1; i++) {
			Thread thread = new Thread(new ImageLoader());
			thread.setName("Image loader " + i);
			thread.start();
		}
		for (LoadingEventListener ev : eventa) {
			ev.loadingFinished(lgd);
		}
	}

	private void refProgress(int loadable, int loaded, int totalWorks, int progress) {
		for (LoadingEventListener ev : eventa) {
			ev.loadingProgressChanged(getProgress(loadable, loaded, progress, totalWorks));
		}
	}

	public int getProgress(int loadable, int loaded, int progress, int mprogress) {
		System.out.println(loadable + ", " + loaded + ", " + progress + ", " + mprogress);
		int d = Math.round(((float) progress / (float) mprogress / (float) loadable * 100f)
				+ (100f / (float) loadable * (float) loaded));
		System.out.println(d);
		return d;
	}
}
