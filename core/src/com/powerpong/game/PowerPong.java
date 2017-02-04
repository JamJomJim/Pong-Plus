package com.powerpong.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import states.*;

public class PowerPong extends ApplicationAdapter {
	public static final float PIXELS_IN_METER = 30f;
	GameStateManager gsm;
	
	@Override
	public void create () {
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		gsm.render();
	}
	
	@Override
	public void dispose () {
		gsm.dispose();
	}
}
