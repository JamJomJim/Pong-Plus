package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;

public class Wall {
    protected Texture texture;
    protected Body body;

    //constructor for invisible wall of specified size
    //pass angle as degrees
    public Wall(float x, float y, float width, float height, float angle, World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        angle = (float)(angle / 180 * Math.PI);
        body.setTransform(body.getPosition(), angle);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PowerPong.PPM, height / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    //constructor for textured wall of the texture's size
    public Wall(String textureName, float x, float y, float angle, World world) {
        this.texture = new Texture(textureName);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        angle = (float)(angle / 180 * Math.PI);
        body.setTransform(body.getPosition(), angle);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(texture.getWidth() / 2 / PowerPong.PPM, texture.getHeight() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    //DO NOT CALL DRAW ON INVISIBLE WALLS, IT WILL THROW NULLPOINTEREXCEPTION
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
