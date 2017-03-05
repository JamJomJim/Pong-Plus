package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.pongplus.game.Options;
import com.pongplus.game.PongPlus;

import java.util.Random;

public class Wall {
    protected NinePatchDrawable ninePatch;
    protected Body body;

    protected Options options;

    private boolean needsNewLocation =  false;

    //pass angle as degrees
    public Wall(boolean textured, float x, float y, float width, float height, float angle, World world, Options options) {
        if (textured)
            ninePatch = new NinePatchDrawable(new NinePatch(new Texture("ClassicPaddle9.png")));
        this.options = options;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        angle = (float)(angle / 180 * Math.PI);
        body.setTransform(body.getPosition(), angle);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PongPlus.PPM, height / 2 / PongPlus.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void randomizeLocation(){
        Random rand = new Random();
        float max, min;
        max = (PongPlus.NATIVE_WIDTH - options.targetWidth - 20);
        min = -(PongPlus.NATIVE_WIDTH - options.targetWidth - 20);
        float x = rand.nextInt((int)(max - min) + 1) + min;
        body.setTransform(x / PongPlus.PPM / 2, 1100 / PongPlus.PPM, 0);
        this.needsNewLocation(false);
    }

    public void draw(SpriteBatch sb) {
        ninePatch.draw(sb,
                body.getPosition().x - options.targetWidth / 2 / PongPlus.PPM,
                body.getPosition().y - ninePatch.getMinHeight() / 2 / PongPlus.PPM,
                options.targetWidth / PongPlus.PPM,
                ninePatch.getMinHeight() / PongPlus.PPM);
    }

    public boolean needsNewLocation() {
        return needsNewLocation;
    }

    public void needsNewLocation(boolean bool) {
        needsNewLocation = bool;
    }

    public NinePatchDrawable getNinePatch() {
        return ninePatch;
    }

    public void dispose() {

    }
}
