package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;

/**
 * Created by Nick on 2/3/2017.
 */
public class PlayerPaddle extends Paddle {
    private static final float NORM_MS = 5; //the paddle's normal/usual movespeed, in meters/second
    public static final float VERTICAL_INPUT_CAPTURE = 5; //this is half of the y-height (in meters) that input within will be handled by this paddle

    private float movespeed; //movespeed is a separate variable so that paddle speed can be changed by powerups etc.
    private Vector3 touchPos;
    private OrthographicCamera worldCam;

    public PlayerPaddle(String textureName, float x, float y, World world, OrthographicCamera cam) {
        super(textureName, x, y, world);
        touchPos = new Vector3(0, 0, 0);
        this.worldCam = cam;
        movespeed = NORM_MS;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        /*use camera.unproject() to convert touch coordinates to viewport/world coordinates
        NOTE: after being unprojected, the numbers are in box2d coordinates
        so touching the top right of the screen, after being unprojected, will yield x: 18, y: 32 REGARDLESS OF RESOLUTION
        0,0 is the center of the screen, positive y is up, positive x is right
        To convert the returned box2d coords to pixels in the viewport, multiply them by PowerPong.PIXELS_IN_METER
        If you want the pixel coordinates relative to the device's screen, simply use x and y without any unprojecting or modifying etc.
        Reason for always having a touchPos Vector3 is to minimize resource creation/destruction*/
        worldCam.unproject(touchPos.set(Gdx.input.getX(), Gdx.input.getY(),0));
        System.out.println("x: " + touchPos.x + "  y: " + touchPos.y);
        //check that the touchPos is within the vertical confines of the area that touches will affect the paddle within
        if (touchPos.y - body.getPosition().y < VERTICAL_INPUT_CAPTURE && touchPos.y - body.getPosition().y > -VERTICAL_INPUT_CAPTURE) {
            //check whether the touchPos is to the left or right of the paddle's current position
            //Note that touches anywhere along the x-axis will affect the paddle, as long as they are within the vertical confines outlined above
            if (touchPos.x - body.getPosition().x > 0)
                body.setLinearVelocity(movespeed, 0);
            else if (touchPos.x - body.getPosition().x < 0)
                body.setLinearVelocity(-movespeed, 0);
            return true; //return true because this paddle handled the touch input and it does not need to be evaluated by the next InputProcessor
        }
        return false; //the touchPos was not within the bounds that would affect this paddle, so it will be passed on to the next InputProcessor
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }
}
