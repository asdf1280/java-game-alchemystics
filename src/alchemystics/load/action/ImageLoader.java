package alchemystics.load.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import alchemystics.game.screen.GamePanel;

public class ImageLoader implements Runnable {
	private static Queue<Integer> loadingImageIdQueue = new LinkedBlockingQueue<>();

	public static void addLoad(int id) {
		if (!loadingImageIdQueue.contains(id) && !GamePanel.gameData.images.containsKey(id) && loading != id)
			loadingImageIdQueue.offer(id);

	}

	public static int loading = -1;

	@Override
	public void run() {
		while (true) {
			if (loadingImageIdQueue.isEmpty()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				continue;
			}
			Integer id = loadingImageIdQueue.poll();
			if (id == null)
				continue;
			loading = id;
			InputStream stream = getClass().getResourceAsStream("/assets/alchemystics/img/" + id + ".png");
			if (stream != null) {
				try {
					GamePanel.gameData.images.put(id, ImageIO.read(stream));
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			} else {
				GamePanel.gameData.images.put(id, new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
			}
		}
	}

}
