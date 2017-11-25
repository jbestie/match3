package org.jbestie.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.jbestie.game.Match3;
import org.jbestie.game.utils.GameConstants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConstants.WORLD_WIDTH;
		config.height = GameConstants.WORLD_HEIGHT;
		new LwjglApplication(new Match3(), config);
	}
}
