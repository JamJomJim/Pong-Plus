package com.powerpong.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import screens.*;

public class PowerPong extends Game {
	public static final float PPM = 300f; //The number of pixels in one meter
	//basically, libgdx drawing works in pixels, but box2d works in meters. Using 1 pixel = 1 meter results in very bad
	//physics simulation. So whenever you draw, you have to divide both the coordinates and the dimensions by PPM
	//tbh it is pretty confusing

	public static int NATIVE_WIDTH = 1440;
	public static int NATIVE_HEIGHT = 2560;

	public SpriteBatch batch;
	public Skin skin;
	public Options options;

	@Override
	public void create () {
		batch = new SpriteBatch();
		options = new Options();

		//SKIN STUFF***************************************************************************************************
		skin = new Skin();

		//add the menu background image to the skin, under the name background
		skin.add("background", new Texture("MenuBackground.png"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/lilliput steps.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 2; //on desktop this causes weird letters sometimes, but on android it seems to not? idk why
		parameter.borderStraight = true;
		parameter.size = 130;
		skin.add("main menu", generator.generateFont(parameter));
		parameter.size = 75;
        skin.add("options text", generator.generateFont(parameter));
        parameter.size = 120;
        skin.add("options header", generator.generateFont(parameter));
        parameter.size = 80;
        skin.add("back button", generator.generateFont(parameter));


        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));
        parameter.size = 130;
        skin.add("Arial", generator.generateFont(parameter));

        //default TextButtonStyle
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("main menu");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.GRAY;
        textButtonStyle.downFontColor = Color.GRAY;
        skin.add("default", textButtonStyle);

        textButtonStyle = new TextButtonStyle(textButtonStyle);
        textButtonStyle.font = skin.getFont("back button");
        skin.add("back button", textButtonStyle);

		//LabelStyle for the optionsMenu text
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("options text");
		labelStyle.fontColor = Color.WHITE;
		skin.add("options text", labelStyle);

        //LabelStyle for the optionsMenu headers text
        labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("options header");
        labelStyle.fontColor = Color.WHITE;
        skin.add("options header", labelStyle);

		//LabelStyle for the score text
        labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("Arial");
        labelStyle.fontColor = Color.WHITE;
        skin.add("score", labelStyle);

        //LabelStyle for the Paused text
        labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("main menu");
        labelStyle.fontColor = Color.WHITE;
        skin.add("paused", labelStyle);

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
	}
}
