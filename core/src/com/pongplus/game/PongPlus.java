package com.pongplus.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import screens.*;

public class PongPlus extends Game {
	public static final float PPM = 300f; //The number of pixels in one meter
	//basically, libgdx drawing works in pixels, but box2d works in meters. Using 1 pixel = 1 meter results in very bad
	//physics simulation. So whenever you draw, you have to divide both the coordinates and the dimensions by PPM
	//tbh it is pretty confusing

	public static int NATIVE_WIDTH = 1440;
	public static int NATIVE_HEIGHT = 2560;

	public SpriteBatch batch;
	public Skin skin;
	public Options options;
	public BitmapFont ls130, ls75, arial130;

	@Override
	public void create () {
		batch = new SpriteBatch();
		options = new Options();
        options.loadOptions();


		//SKIN STUFF***************************************************************************************************
		skin = new Skin();
		ls130 = new BitmapFont(Gdx.files.internal("fonts/LS130.fnt"));
		ls75 = new BitmapFont(Gdx.files.internal("fonts/LS75.fnt"));
		arial130 = new BitmapFont(Gdx.files.internal("fonts/Arial130.fnt"));
		skin.add("LS130", ls130); //Lilliput Steps size 130
        skin.add("LS75", ls75);
        skin.add("Arial130", arial130);

        //default TextButtonStyle
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("LS130");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.GRAY;
        textButtonStyle.downFontColor = Color.GRAY;
        skin.add("default", textButtonStyle);

        textButtonStyle = new TextButtonStyle(textButtonStyle);
        textButtonStyle.font = skin.getFont("LS75");
        skin.add("LS75", textButtonStyle);


        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("LS130");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("LS75");
        labelStyle.fontColor = Color.WHITE;
        skin.add("LS75", labelStyle);

		//LabelStyle for the score text
        labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("Arial130");
        labelStyle.fontColor = Color.WHITE;
        skin.add("Arial130", labelStyle);

        //Slider style
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.knob = new NinePatchDrawable(new NinePatch(new Texture("skin stuff/slider-knob-square.png")));
        sliderStyle.knobDown = new NinePatchDrawable(new NinePatch(new Texture("skin stuff/slider-knob-square-down.png")));
        sliderStyle.background = new NinePatchDrawable(new NinePatch(new Texture("skin stuff/slider-horizontal-background.png"), 2, 2, 3, 3));
        skin.add("default-horizontal", sliderStyle);


        this.setScreen(new MenuScreen(this, options));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		skin.dispose();
		ls130.dispose();
		ls75.dispose();
		arial130.dispose();
	}
}
