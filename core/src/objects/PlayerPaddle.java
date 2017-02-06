package objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Nick on 2/3/2017.
 */
public class PlayerPaddle extends Paddle {

    private static final float INPUT_CAPTURE_HEIGHT = 5; //this is half of the y-height (in meters) that input within (relative to this paddle's y-position) will be handled by this paddle

    private Vector3 touchPos, touchDraggedPos;
    private OrthographicCamera worldCam;

    /*
    Pointers basically keep track of fingers. For example, if no fingers are touching the screen, then you touch it,
    the pointer of that finger will be zero. If you drag that finger around, the pointer will still be zero.
    touching another finger without lifting the first one will give the second finger a pointer of one.
    upon releasing the first finger, the second finger's pointer will still be one, but the first finger's
    point will have been "released", and it will be filled by the next finger that touches. So when a new finger touches,
    it will take the lowest pointer value that is not already in use by another finger.
    */
    private int movePointer; //represents the pointer for the finger that most recently touched within the input capture height,
    //and therefore the one that is controlling the paddle

    public PlayerPaddle(String textureName, float x, float y, World world, OrthographicCamera cam) {
        super(textureName, x, y, world);
        touchPos = new Vector3(0, 0, 0);
        touchDraggedPos = new Vector3(0, 0, 0);
        this.worldCam = cam;
        movePointer = -1; //otherwise default value is 0, and the first touch on the screen, regardless of coords, will potentially affect this paddle
    }

    /*
    use camera.unproject() to convert touch coordinates to viewport/world coordinates
    NOTE: after being unprojected, the numbers are in box2d coordinates; 0,0 is the center of the screen, positive y is up, positive x is right
    so touching the top right of the screen, after being unprojected, will yield
    x: PowerPong.WIDTH / PPM / 2, y: PowerPong.HEIGHT / PPM / 2; REGARDLESS OF RESOLUTION
    To convert the returned box2d coords to pixels in the viewport, multiply them by PowerPong.PPM
    If you want the pixel coordinates relative to the device's screen, simply use x and y without any unprojecting or modifying etc.
    Reason for always having a touchPos Vector3 is to minimize resource creation/destruction.
    */

    /*
    upon a touchDown() event, this first converts the screen coordinates into box2d coordinates. Then it checks if the touched coords are
    within the INPUT_CAPTURE_HEIGHT. If they are, it sets the destination as the touched coords, and sets this finger as the movePointer.
    This means that whichever finger touched within the INPUT_CAPTURE_HEIGHT most recently will be the one to control the paddle.
    */
    public boolean touchDown(int x, int y, int pointer, int button) {
        worldCam.unproject(touchPos.set(x, y, 0));
        //check that the touchPos is within the vertical confines of the area that touches within will affect the paddle
        if (touchPos.y - body.getPosition().y < INPUT_CAPTURE_HEIGHT && touchPos.y - body.getPosition().y > -INPUT_CAPTURE_HEIGHT) {
            destination.set(touchPos.x, touchPos.y);
            movePointer = pointer;
            return true; //return true because this paddle handled the touch input and it does not need to be evaluated by the next InputProcessor
        }
        return false; //the touchPos was not within the bounds that would affect this paddle, so it will be passed on to the next InputProcessor
    }

    /*
    Upon a touchUp() event, check if the finger that was released was the one controlling the paddle. If it was, set the destination to
    the paddle's current position; this will prevent further movement. Also set the movePointer to -1, so that not finger is controlling the paddle
    until another touchDown() event occurs within the input area.
     */
    public boolean touchUp (int x, int y, int pointer, int button) {
        if (pointer == movePointer) {
            destination.set(body.getPosition().x, body.getPosition().y);
            movePointer = -1;
            return true;
        }
        return false;
    }

    /*
    Upon a touchDragged() event, check if the event is from the finger that is currently controlling the paddle. If it is, set the destination
    to the converted coords of the drag.
    Note that when multiple fingers are held on the screen, anytime one of them is dragged, touchDragged events will be
    fired for EVERY finger touching the screen, even ones that didn't move.
     */
    public boolean touchDragged(int x, int y, int pointer) {
        worldCam.unproject(touchDraggedPos.set(x, y, 0));
        if (pointer == movePointer) {
            destination.set(touchDraggedPos.x, touchDraggedPos.y);
            return true;
        }
        return false;
    }
}
