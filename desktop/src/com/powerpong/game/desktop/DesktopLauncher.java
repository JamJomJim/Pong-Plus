package com.powerpong.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.powerpong.game.PowerPong;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = PowerPong.NATIVE_HEIGHT / 2;
		config.width = PowerPong.NATIVE_WIDTH / 2;
		new LwjglApplication(new PowerPong(), config);
	}
}
