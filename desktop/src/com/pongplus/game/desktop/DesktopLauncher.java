package com.pongplus.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pongplus.game.PongPlus;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = PongPlus.NATIVE_HEIGHT / 2;
		config.width = PongPlus.NATIVE_WIDTH / 2;
		new LwjglApplication(new PongPlus(), config);
	}
}
