package objects;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

/**
 * Created by Nick on 2/3/2017.
 */
public class PlayerPaddle extends Paddle {

    public PlayerPaddle(String textureName, float x, float y, World world) {
        super(textureName, x, y, world);
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        //convert x and y to game coordinates (they are passed as coordinates in which 0,0 is the top left corner)
        x -= PowerPong.WIDTH / 2;
        y -= PowerPong.HEIGHT / 2;
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }
}
