package com.powerpong.game;

public class Options {
    public float ballInitialSpeed, ballSpeedIncrease, ballAngleMultiplier;
    public float aiMovespeed, aiOffset;
    public boolean soundOn;

    //no args constructor sets stuff to default values
    public Options() {
        ballInitialSpeed = 3;
        ballSpeedIncrease = 1.5f;
        ballAngleMultiplier = 60;

        aiMovespeed = 7;
        aiOffset = 2; //the width of the paddle is divided by this. So a value of 2 means the offset can be to either end. 4 means halfway to either end, etc

        soundOn = true;
    }
}
