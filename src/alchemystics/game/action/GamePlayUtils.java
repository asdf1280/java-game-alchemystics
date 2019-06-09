package alchemystics.game.action;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import alchemystics.game.data.GamePlayData;
import alchemystics.game.data.UserSavefile;

public class GamePlayUtils {
	private GamePlayData gpd;

	public GamePlayUtils(GamePlayData data) {
		data.data = this;
		gpd = data;
	}

	public static boolean saveGame(UserSavefile usf) {
		try {
			Gson g = new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
			File gameSave = new File("savefile.json");
			FileUtils.writeStringToFile(gameSave, g.toJson(usf, usf.getClass()), "UTF8", false);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void addNewItem() {
		gpd.panel.itemsPanel.newItemAdded(null);
		saveGame(gpd.save);
	}
}
