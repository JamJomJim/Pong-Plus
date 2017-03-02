package com.powerpong.game;

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

    //no args constructor sets stuff to default values
    public Options() {
        ai = AI.CUSTOM;
        mode = Mode.MENUBATTLE;
        paddleWidth = 300;
        scoreLimit = 5;
        ballSize = 80;
        ballInitialSpeed = 3;
        ballSpeedIncrease = 1f;
        ballAngleMultiplier = 60;

        aiMovespeed = 7;
        aiOffset = 2; //the width of the paddle is divided by this. So a value of 2 means the offset can be to either end. 4 means halfway to either end, etc

        targetWidth = 300;

        soundOn = false;


        paddleTexture = "ClassicPaddle9.png";
    }

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
        this.soundOn = soundOn;

        paddleTexture = "ClassicPaddle9.png";
    }
}
