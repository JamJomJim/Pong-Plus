package vandykeewens.pongplus.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Options {
    public enum AI {//different AI difficulties
        EASY, MEDIUM, HARD, IMPOSSIBLE, CUSTOM
    }

    public enum Mode {//different modes of play
        ONEPLAYER, TWOPLAYER, SURVIVAL, TARGETS, AIBATTLE, MENUBATTLE
    }

    public boolean startup;
    public AI ai;
    public Mode mode;
    public float paddleWidth;
    public float scoreLimit;
    public float ballSize, ballInitialSpeed, ballSpeedIncrease, ballAngleMultiplier;
    public float aiMovespeed, aiOffset;
    public float targetWidth;
    public boolean soundOn;

    public String paddleTexture;
    public Preferences prefs = Gdx.app.getPreferences("My Preferences");

    public Options() {
        startup = true;
    }

    //this is just for creating a separate Options for the menu ai battle
    public Options(Mode mode) {
        this.mode = mode;
        this.ai = AI.CUSTOM;

        this.paddleWidth = 300;
        this.scoreLimit = 5;
        this.ballSize = 60;
        this.ballInitialSpeed = 5;
        this.ballSpeedIncrease = 0;
        this.ballAngleMultiplier = 50;
        this.aiMovespeed = 3;
        this.aiOffset = 10;
        this.soundOn = false;

        paddleTexture = "ClassicPaddle9.png";
    }

    public void saveOptions() {
        prefs.putFloat("paddleWidth", paddleWidth);
        prefs.putFloat("scoreLimit", scoreLimit);
        prefs.putFloat("ballSize", ballSize);
        prefs.putFloat("ballInitialSpeed", ballInitialSpeed);
        prefs.putFloat("ballSpeedIncrease", ballSpeedIncrease);
        prefs.putFloat("ballAngleMultiplier", ballAngleMultiplier);
        prefs.putFloat("aiMovespeed", aiMovespeed);
        prefs.putFloat("aiOffset", aiOffset);
        prefs.putFloat("targetWidth", targetWidth);
        prefs.putBoolean("soundOn", soundOn);
        prefs.putString("paddleTexture", paddleTexture);
        prefs.flush();
    }

    public void loadOptions() {
        this.paddleWidth = prefs.getFloat("paddleWidth", 300);
        this.scoreLimit = prefs.getFloat("scoreLimit", 5);
        this.ballSize = prefs.getFloat("ballSize", 70);
        this.ballInitialSpeed = prefs.getFloat("ballInitialSpeed", 3);
        this.ballSpeedIncrease = prefs.getFloat("ballSpeedIncrease", 1.0f);
        this.ballAngleMultiplier = prefs.getFloat("ballAngleMultiplier", 50);
        this.aiMovespeed = prefs.getFloat("aiMovespeed", 7);
        this.aiOffset = prefs.getFloat("aiOffset", 5);
        this.targetWidth = prefs.getFloat("targetWidth", 300);
        this.soundOn = prefs.getBoolean("soundOn", true);
        this.paddleTexture = prefs.getString("paddleTexture", "ClassicPaddle9.png");
    }
}
