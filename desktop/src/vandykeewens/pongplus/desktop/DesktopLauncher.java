package vandykeewens.pongplus.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import vandykeewens.pongplus.main.PongPlus;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = PongPlus.VIRTUAL_HEIGHT / 2;
		config.width = PongPlus.VIRTUAL_WIDTH / 2;
		new LwjglApplication(new PongPlus(), config);
	}
}
