package com.pongplus.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Options {
    public enum AI {//different AI difficulties
        EASY, MEDIUM, HARD, SKYNET, CUSTOM
    }

    public enum Mode {//different modes of play
        ONEPLAYER, TWOPLAYER, SURVIVAL, PRACTICE, AIBATTLE, MENUBATTLE
    }

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
    }

    //this is just for creating a separate Options for the menu ai battle
    public Options(Mode mode, AI ai, float paddleWidth, float ballInitialSpeed, float ballSpeedIncrease, float ballAngleMultiplier,
                   float aiMovespeed, float aiOffset, boolean soundOn) {
        this.mode = mode;
        this.ai = ai;

        this.paddleWidth = paddleWidth;
        this.scoreLimit = 5;
        this.ballSize = 80;
        this.ballInitialSpeed = ballInitialSpeed;
        this.ballSpeedIncrease = ballSpeedIncrease;
        this.ballAngleMultiplier = ballAngleMultiplier;
        this.aiMovespeed = aiMovespeed;
        this.aiOffset = aiOffset;
        this.soundOn = true;

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
        this.ballSize = prefs.getFloat("ballSize", 80);
        this.ballInitialSpeed = prefs.getFloat("ballInitialSpeed", 3);
        this.ballSpeedIncrease = prefs.getFloat("ballSpeedIncrease", 1);
        this.ballAngleMultiplier = prefs.getFloat("ballAngleMultiplier", 60);
        this.aiMovespeed = prefs.getFloat("aiMovespeed", 7);
        this.aiOffset = prefs.getFloat("aiOffset", 5);
        this.targetWidth = prefs.getFloat("targetWidth", 300);
        this.soundOn = prefs.getBoolean("soundOn", true);
        this.paddleTexture = prefs.getString("paddleTexture", "ClassicPaddle9.png");
    }
}
