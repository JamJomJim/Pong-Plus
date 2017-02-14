package objects.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.powerpong.game.PowerPong;

public class Powerup {
    protected Texture texture;
    protected Body body;

    private String textureName;
    private Type type;
    private boolean isDead;
    public enum Type {
        TEMP
    }

    public Powerup(Type type, float x, float y, World world) {
        this.type = type;
        switch (type) {
            case TEMP:
                textureName = "PowerupTemp.png";
                break;
        }
        this.texture = new Texture(textureName);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the player will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(texture.getWidth() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public boolean getIsDead() { return isDead; }

    public void setIsDead(boolean bool) { isDead = bool; }

    public Body getBody() { return body; }

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

    public float getX(){ return body.getPosition().x; }

    public float getY(){ return body.getPosition().y; }

}