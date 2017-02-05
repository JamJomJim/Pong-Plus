package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;

public class Wall {
    protected Texture texture;
    protected Body body;

    public Wall() {
    }

    public Wall(String textureName, World world) {
        this.texture = new Texture(textureName);

        BodyDef wallDefLeft = new BodyDef();
        wallDefLeft.type = BodyDef.BodyType.StaticBody;
        wallDefLeft.fixedRotation = true;
        wallDefLeft.position.set(PowerPong.NATIVE_WIDTH / PowerPong.PPM / 2 + 1 , 0);

        body = world.createBody(wallDefLeft);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, PowerPong.NATIVE_HEIGHT / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        BodyDef wallDefRight = new BodyDef();
        wallDefRight.type = BodyDef.BodyType.StaticBody;
        wallDefRight.fixedRotation = true;
        wallDefRight.position.set(-PowerPong.NATIVE_WIDTH / PowerPong.PPM / 2 - 1, 0);

        body = world.createBody(wallDefRight);
        body.setUserData(this);

        shape.setAsBox(1, PowerPong.NATIVE_HEIGHT / 2);

        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);

        BodyDef tempWallTop = new BodyDef();
        tempWallTop.type = BodyDef.BodyType.StaticBody;
        tempWallTop.fixedRotation = true;
        tempWallTop.position.set(0, PowerPong.NATIVE_HEIGHT / PowerPong.PPM / 2 + 1);

        body = world.createBody(tempWallTop);
        body.setUserData(this);

        shape.setAsBox(PowerPong.NATIVE_WIDTH / 2, 1);

        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    /*
    Draw the paddle, centered at body.x and body.y
     */
    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                body.getPosition().x - texture.getWidth() / 2 / PowerPong.PPM,
                body.getPosition().y - texture.getHeight() / 2 / PowerPong.PPM,
                texture.getWidth() / PowerPong.PPM,
                texture.getHeight() / PowerPong.PPM);
    }

    public void dispose() {
        texture.dispose();
    }
}
