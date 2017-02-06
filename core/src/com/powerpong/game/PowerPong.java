package com.powerpong.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerPong extends Game {
	public static final float PPM = 300f; //The number of pixels in one meter
	//basically, libgdx drawing works in pixels, but box2d works in meters. Using 1 pixel = 1 meter results in very bad
	//physics simulation. So whenever you draw, you have to divide both the coordinates and the dimensions by PPM
	//tbh it is pretty confusing

	public static int NATIVE_WIDTH = 1440;
	public static int NATIVE_HEIGHT = 2560;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new screens.MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
