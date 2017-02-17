package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.powerpong.game.ContactListener;
import com.powerpong.game.PowerPong;
import objects.paddles.AIPaddle;
import objects.Ball;
import objects.paddles.PlayerPaddle;
import objects.powerups.Powerup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PowerPongPlayScreen extends PlayScreen {
    private float MAX_POWERUPS = 5;
    private float POWERUP_SPAWN_RATE = 997; //chance out of 1000 to spawn each time render is called. Approx 60 per second

	private ContactListener contactListener;
    private int powerupX, powerupY;
    private int numPowerups;
    private boolean overlap;
    private List<Powerup.Type> typeValues = Arrays.asList(Powerup.Type.values()); //Need to do this otherwise each call for the values of typeValues will create a new list.
    private Random random = new Random();
    private Powerup.Type powerupType;
    private ArrayList<Powerup> powerups;

	public PowerPongPlayScreen(PowerPong game, AI ai) {
		super(game);

		ball = new Ball("ClassicBall.png", 0, 0, BALL_DIRECTION, BALL_SPEED, world, this);
		p1 = new PlayerPaddle("ClassicPaddle.png", 0, -1100 / PowerPong.PPM, world, worldCam);
		if (ai == AI.NONE)
			p2 = new PlayerPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, worldCam);
		else
			p2 = new AIPaddle("ClassicPaddle.png", 0, 1100 / PowerPong.PPM, world, ball, ai);

		contactListener = new ContactListener(p1, p2);
		world.setContactListener(contactListener);
        //create InputMultiplexer, to handle input on multiple paddles and the ui
        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(p1);
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        if (ai == AI.NONE)
            multiplexer.addProcessor(p2);

        powerups = new ArrayList<Powerup>();
	}

	public void render(float dt) {
	    super.render(dt);
        //Random creation of powerups.
        if (random.nextInt(1001) > POWERUP_SPAWN_RATE && numPowerups < MAX_POWERUPS) { // 0 is included, arg is excluded
            powerupType = typeValues.get(random.nextInt(typeValues.size())); //randomly chooses an enum in Powerup.Type
            powerupX = random.nextInt(PowerPong.NATIVE_WIDTH - 100) - (PowerPong.NATIVE_WIDTH - 100)/ 2;
            powerupY = random.nextInt(PowerPong.NATIVE_HEIGHT - 500) - (PowerPong.NATIVE_HEIGHT - 500)/ 2;
            if (powerups.isEmpty()) {//if there aren't any powerups make a powerup and add it to the powerups arraylist
                Powerup tempPowerup = new Powerup(powerupType, powerupX / PowerPong.PPM, powerupY / PowerPong.PPM, world);
                powerups.add(tempPowerup);
                numPowerups++;
            }
            else {
                for (Powerup powerup : powerups) {
                    if ((Math.abs(powerup.getBody().getPosition().x - powerupX / PowerPong.PPM) < 100 / PowerPong.PPM)
                            && (Math.abs(powerup.getBody().getPosition().y - powerupY / PowerPong.PPM) < 100 / PowerPong.PPM)) {
                        overlap = true;
                        break;
                    }
                    else overlap = false;
                }
                if (!overlap) {
                    Powerup tempPowerup = new Powerup(powerupType, powerupX / PowerPong.PPM, powerupY / PowerPong.PPM, world);
                    powerups.add(tempPowerup);
                    numPowerups++;
                }
            }
        }
		//remove powerups that are "dead"
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).isDead()) {
				world.destroyBody(powerups.get(i).getBody());
				powerups.remove(i);
				i--;
				numPowerups--;
			}
		}
		game.batch.begin();
        for (Powerup powerup : powerups) {
            powerup.draw(game.batch);
        }
        game.batch.end();
	}

	public void dispose() {
	    super.dispose();
        for (Powerup powerup : powerups) {
            powerup.dispose();
        }
    }
}