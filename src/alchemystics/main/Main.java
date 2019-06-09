package alchemystics.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.IOException;

public class Main {
	public static String[] args;
	public static App appInstance;
	public static Dimension windowSize;
	public static Font defaultFont;
	public static Font boldFont;
	static {
		windowSize = Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static void main(String[] args) {
		Main.args = args;
		try {
			defaultFont = Font.createFont(Font.TRUETYPE_FONT,
					Main.class.getResourceAsStream("/assets/alchemystics/ttf/noto.ttf"));
		} catch (FontFormatException | IOException e) {
			defaultFont = new Font("맑은 고딕", 0, 0);
		}
		try {
			boldFont = Font.createFont(Font.TRUETYPE_FONT,
					Main.class.getResourceAsStream("/assets/alchemystics/ttf/bold.ttf"));
		} catch (FontFormatException | IOException e) {
			boldFont = defaultFont;
		}
		start();
	}

	public static void start() {
		appInstance = new App();
	}
}