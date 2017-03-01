package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.powerpong.game.Options;
import com.powerpong.game.Options.AI;
import com.powerpong.game.Options.Mode;
import com.powerpong.game.PowerPong;

import java.util.Random;

public class MenuScreen extends InputAdapter implements Screen {
	private Stage stage;
	private Table table;
	private Table modes, difficulties, optionsMenu, customAI, practiceSettings;
	private PowerPong game;
	private PlayScreen menuBattle;
	private Options options;

	private Random random;

	public MenuScreen(PowerPong game, Options opt) {
		this.game = game;
		this.options = opt;
		stage = new Stage(new StretchViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        stage.setDebugAll(true);
        random = new Random();

		// Create a table that fills the screen
		table = new Table();
		table.setSkin(game.skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setFillParent(true);
		stage.addActor(table);


        //stuff for the different modes
        modes = new Table();
        final TextButton button1P = new TextButton("ONE PLAYER", game.skin);
        button1P.setHeight(160);
        button1P.setWidth(button1P.getPrefWidth() + 50);
        final TextButton button2P = new TextButton("TWO PLAYER", game.skin);
        final TextButton buttonAIBattle = new TextButton("AI BATTLE", game.skin);
        final TextButton buttonPractice = new TextButton("PRACTICE", game.skin);
        final TextButton buttonWall = new TextButton("SURVIVAL", game.skin);
        final TextButton buttonOptions = new TextButton("OPTIONS", game.skin);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button1P.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(true);
                modes.setVisible(false);
                options.mode = Mode.ONEPLAYER;
            }
        });
        button2P.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.mode = Mode.TWOPLAYER;
                startPlay();
            }
        });
        buttonPractice.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.mode = Mode.PRACTICE;
                modes.setVisible(false);
                practiceSettings.setVisible(true);
            }
        });
        buttonWall.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.mode = Mode.SURVIVAL;
                startPlay();
            }
        });
        buttonAIBattle.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(true);
                modes.setVisible(false);
                options.mode = Mode.AIBATTLE;
            }
        });
        buttonOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(true);
                modes.setVisible(false);
            }
        });

        modes.add(button1P).width(button1P.getWidth()).height(button1P.getHeight());
        modes.row();
        modes.add(button2P).fillX().height(button1P.getHeight());
        modes.row();
        modes.add(buttonAIBattle).fillX().height(button1P.getHeight());
        modes.row();
        modes.add(buttonPractice).fillX().height(button1P.getHeight());
        modes.row();
        modes.add(buttonWall).fillX().height(button1P.getHeight());
        modes.row();
        modes.add(buttonOptions).fillX().height(button1P.getHeight());


        //difficulties stuff
        difficulties = new Table();
        difficulties.setVisible(false);
        final TextButton buttonEasy = new TextButton("EASY", game.skin);
        final TextButton buttonMedium = new TextButton("MEDIUM", game.skin);
        final TextButton buttonHard = new TextButton("HARD", game.skin);
        final TextButton buttonSkynet = new TextButton("SKYNET", game.skin);
        final TextButton buttonCustom = new TextButton("CUSTOM", game.skin);
        final TextButton buttonBack = new TextButton("BACK", game.skin, "back button");
        buttonEasy.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.ai = AI.EASY;
                startPlay();
            }
        });
        buttonMedium.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.ai = AI.MEDIUM;
                startPlay();
            }
        });
        buttonHard.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.ai = AI.HARD;
                startPlay();
            }
        });
        buttonSkynet.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.ai = AI.SKYNET;
                startPlay();
            }
        });
        buttonCustom.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.ai = AI.CUSTOM;
                difficulties.setVisible(false);
                customAI.setVisible(true);
            }
        });
        buttonBack.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(false);
                modes.setVisible(true);
            }
        });

        difficulties.add(buttonEasy).width(button1P.getWidth()).height(button1P.getHeight());
        difficulties.row();
        difficulties.add(buttonMedium).fillX().height(button1P.getHeight());
        difficulties.row();
        difficulties.add(buttonHard).fillX().height(button1P.getHeight());
        difficulties.row();
        difficulties.add(buttonSkynet).fillX().height(button1P.getHeight());
        difficulties.row();
        difficulties.add(buttonCustom).fillX().height(button1P.getHeight());
        difficulties.row();
        difficulties.add(buttonBack).fillX().height(button1P.getHeight());


        customAI = new Table();
        customAI.setVisible(false);
        final Label aiLabel = new Label("CUSTOM AI", game.skin, "options header");
        final Label aiSpeedLabel = new Label("MOVE\nSPEED", game.skin, "options text");
        aiSpeedLabel.setAlignment(Align.center);
        final Slider aiSpeedSlider = new Slider(1, 100, 1, false, game.skin);
        aiSpeedSlider.setValue(options.aiMovespeed);
        final Label aiSpeedNumber = new Label(Integer.toString((int)aiSpeedSlider.getValue()), game.skin, "options text");

        final TextButton buttonPlayAI = new TextButton("PLAY", game.skin);

        final TextButton buttonBackAI = new TextButton("BACK", game.skin, "back button");

        aiSpeedSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.aiMovespeed = aiSpeedSlider.getValue();
                aiSpeedNumber.setText(Integer.toString((int)aiSpeedSlider.getValue()));
            }
        });
        buttonPlayAI.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                startPlay();
            }
        });
        buttonBackAI.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                customAI.setVisible(false);
                difficulties.setVisible(true);
            }
        });

        int secondColWidth = 500, spacing = 25, thirdColWidth = 160;
        customAI.add(aiLabel).colspan(3);
        customAI.row();
        customAI.add(aiSpeedLabel).space(0, 0, spacing, 0);
        customAI.add(aiSpeedSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        customAI.add(aiSpeedNumber).width(thirdColWidth).space(0, 0, spacing, 0);
        customAI.row();
        customAI.add(buttonPlayAI).colspan(3).fillX();
        customAI.row();
        customAI.add(buttonBackAI).colspan(3).fillX().height(160);


        practiceSettings = new Table();
        practiceSettings.setVisible(false);
        final Label practiceLabel = new Label("PRACTICE", game.skin, "options header");
        final Label targetWidthLabel = new Label("TARGET\nWIDTH", game.skin, "options text");
        targetWidthLabel.setAlignment(Align.center);
        final Slider targetWidthSlider = new Slider(10, 1000, 10, false, game.skin);
        targetWidthSlider.setValue(options.targetWidth);
        final Label targetWidthNumber = new Label(Integer.toString((int)targetWidthSlider.getValue()), game.skin, "options text");

        final TextButton buttonPlayPractice = new TextButton("PLAY", game.skin);

        final TextButton buttonBackPractice = new TextButton("BACK", game.skin, "back button");

        targetWidthSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.targetWidth = targetWidthSlider.getValue();
                targetWidthNumber.setText(Integer.toString((int)targetWidthSlider.getValue()));
            }
        });
        buttonPlayPractice.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                startPlay();
            }
        });
        buttonBackPractice.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                practiceSettings.setVisible(false);
                modes.setVisible(true);
            }
        });

        practiceSettings.add(practiceLabel).colspan(3);
        practiceSettings.row();
        practiceSettings.add(targetWidthLabel).space(0, 0, spacing, 0);
        practiceSettings.add(targetWidthSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        practiceSettings.add(targetWidthNumber).width(thirdColWidth).space(0, 0, spacing, 0);
        practiceSettings.row();
        practiceSettings.add(buttonPlayPractice).colspan(3).fillX();
        practiceSettings.row();
        practiceSettings.add(buttonBackPractice).colspan(3).fillX().height(160);


        //Options menu stuff
        optionsMenu = new Table();
        optionsMenu.setVisible(false);
        
        final Label scoreLimitLabel = new Label("SCORE\nLIMIT", game.skin, "options text");
        scoreLimitLabel.setAlignment(Align.center);
        final Slider scoreLimitSlider = new Slider(1, 10, 1, false, game.skin);
        scoreLimitSlider.setValue(options.scoreLimit);
        final Label scoreLimitNumber = new Label(Integer.toString((int)scoreLimitSlider.getValue()), game.skin, "options text");

        final Label paddleWidthLabel = new Label("PADDLE\nWIDTH", game.skin, "options text");
        paddleWidthLabel.setAlignment(Align.center);
        final Slider paddleWidthSlider = new Slider(100, 1000, 10, false, game.skin);
        paddleWidthSlider.setValue(options.paddleWidth);
        final Label paddleWidthNumber = new Label(Integer.toString((int)paddleWidthSlider.getValue()), game.skin, "options text");

        final Label ballLabel = new Label("BALL", game.skin, "options header");
        final Label ballSizeLabel = new Label("SIZE", game.skin, "options text");
        ballSizeLabel.setAlignment(Align.center);
        final Slider ballSizeSlider = new Slider(10, 1000, 10, false, game.skin);
        ballSizeSlider.setValue(options.ballSize);
        final Label ballSizeNumber = new Label(Integer.toString((int)ballSizeSlider.getValue()), game.skin, "options text");
        
        final Label ballInitialSpeedLabel = new Label("START\nSPEED", game.skin, "options text");
        ballInitialSpeedLabel.setAlignment(Align.center);
        final Slider ballInitialSpeedSlider = new Slider(1, 100, 1, false, game.skin);
        ballInitialSpeedSlider.setValue(options.ballInitialSpeed);
        final Label ballInitialSpeedNumber = new Label(Integer.toString((int)ballInitialSpeedSlider.getValue()), game.skin, "options text");

        final Label ballSpeedIncreaseLabel = new Label("SPEED\nINCREASE", game.skin, "options text");
        ballSpeedIncreaseLabel.setAlignment(Align.center);
        final Slider ballSpeedIncreaseSlider = new Slider(0, 10, 1, false, game.skin);
        ballSpeedIncreaseSlider.setValue(options.ballSpeedIncrease);
        final Label ballSpeedIncreaseNumber = new Label(Integer.toString((int)ballSpeedIncreaseSlider.getValue()), game.skin, "options text");

        final Label ballAngleLabel = new Label("REBOUND\nANGLE", game.skin, "options text");
        ballAngleLabel.setAlignment(Align.center);
        final Slider ballAngleSlider = new Slider(0, 100, 1, false, game.skin);
        ballAngleSlider.setValue(options.ballAngleMultiplier);
        final Label ballAngleNumber = new Label(Integer.toString((int)ballAngleSlider.getValue()), game.skin, "options text");

        final TextButton buttonSmallRandomizeOptions = new TextButton("REASONABLY RANDOMIZE", game.skin, "back button");

        final TextButton buttonRandomizeOptions = new TextButton("TOTALLY RANDOMIZE", game.skin, "back button");

        final TextButton buttonResetOptions = new TextButton("DEFAULT", game.skin, "back button");

        final TextButton buttonBackOptions = new TextButton("BACK", game.skin, "back button");

        scoreLimitSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.scoreLimit = scoreLimitSlider.getValue();
                scoreLimitNumber.setText(Integer.toString((int)scoreLimitSlider.getValue()));
            }
        });
        paddleWidthSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.paddleWidth = paddleWidthSlider.getValue();
                paddleWidthNumber.setText(Integer.toString((int)paddleWidthSlider.getValue()));
            }
        });
        ballSizeSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.ballSize = ballSizeSlider.getValue();
                ballSizeNumber.setText(Integer.toString((int)ballSizeSlider.getValue()));
            }
        });
        ballInitialSpeedSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.ballInitialSpeed = ballInitialSpeedSlider.getValue();
                ballInitialSpeedNumber.setText(Integer.toString((int)ballInitialSpeedSlider.getValue()));
            }
        });
        ballSpeedIncreaseSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.ballSpeedIncrease = ballSpeedIncreaseSlider.getValue();
                ballSpeedIncreaseNumber.setText(Integer.toString((int)ballSpeedIncreaseSlider.getValue()));
            }
        });
        ballAngleSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.ballAngleMultiplier = ballAngleSlider.getValue();
                ballAngleNumber.setText(Integer.toString((int)ballAngleSlider.getValue()));
            }
        });
        buttonSmallRandomizeOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                scoreLimitSlider.setValue(3 + random.nextInt(8)); //bound in non-inclusive
                paddleWidthSlider.setValue(100 + random.nextInt(401));
                ballSizeSlider.setValue(50 + random.nextInt(101));
                ballInitialSpeedSlider.setValue(1 + random.nextInt(10));
                ballSpeedIncreaseSlider.setValue(1 + random.nextInt(10));
                ballAngleSlider.setValue(30 + random.nextInt(51));
            }
        });
        buttonRandomizeOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                scoreLimitSlider.setValue(scoreLimitSlider.getMinValue() + random.nextInt((int)(scoreLimitSlider.getMaxValue() + 1 - scoreLimitSlider.getMinValue())));
                paddleWidthSlider.setValue(paddleWidthSlider.getMinValue() + random.nextInt((int)(paddleWidthSlider.getMaxValue() + 1 - paddleWidthSlider.getMinValue())));
                ballSizeSlider.setValue(ballSizeSlider.getMinValue() + random.nextInt((int)(ballSizeSlider.getMaxValue() + 1 - ballSizeSlider.getMinValue())));
                ballInitialSpeedSlider.setValue(ballInitialSpeedSlider.getMinValue() + random.nextInt((int)(ballInitialSpeedSlider.getMaxValue() + 1 - ballInitialSpeedSlider.getMinValue())));
                ballSpeedIncreaseSlider.setValue(ballSpeedIncreaseSlider.getMinValue() + random.nextInt((int)(ballSpeedIncreaseSlider.getMaxValue() + 1 - ballSpeedIncreaseSlider.getMinValue())));
                ballAngleSlider.setValue(ballAngleSlider.getMinValue() + random.nextInt((int)(ballAngleSlider.getMaxValue() + 1 - ballAngleSlider.getMinValue())));
            }
        });
        buttonResetOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //note that these are hardcoded
                scoreLimitSlider.setValue(5);
                paddleWidthSlider.setValue(300);
                ballSizeSlider.setValue(75);
                ballInitialSpeedSlider.setValue(3);
                ballSpeedIncreaseSlider.setValue(1);
                ballAngleSlider.setValue(60);
            }
        });
        buttonBackOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(false);
                modes.setVisible(true);
            }
        });

        optionsMenu.add(scoreLimitLabel).space(0, 0, spacing, 0);
        optionsMenu.add(scoreLimitSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(scoreLimitNumber).width(thirdColWidth).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(paddleWidthLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(paddleWidthSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(paddleWidthNumber).width(thirdColWidth).space(0, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballLabel).spaceTop(spacing * 2).colspan(3);
        optionsMenu.row();
        optionsMenu.add(ballSizeLabel).space(0, 0, spacing, 0).height(scoreLimitLabel.getPrefHeight());
        optionsMenu.add(ballSizeSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(ballSizeNumber).width(thirdColWidth).space(0, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballInitialSpeedLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballInitialSpeedSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(ballInitialSpeedNumber).width(thirdColWidth).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballSpeedIncreaseLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballSpeedIncreaseSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(ballSpeedIncreaseNumber).width(thirdColWidth).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballAngleLabel).space(spacing, 0, spacing * 2, 0);
        optionsMenu.add(ballAngleSlider).width(secondColWidth).space(0, spacing, spacing * 2, spacing).fillX();
        optionsMenu.add(ballAngleNumber).width(thirdColWidth).space(spacing, 0, spacing * 2, 0);
        optionsMenu.row();
        optionsMenu.add(buttonSmallRandomizeOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonRandomizeOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonResetOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonBackOptions).colspan(3).fillX().height(140);



        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        menu.add(customAI);
        menu.add(practiceSettings);
        menu.add(optionsMenu);
        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(PowerPong.NATIVE_HEIGHT / 2 - menu.getHeight() / 2);

        //to have changes to the options affect the menubattle, pass options to this, rather than a new Options
        menuBattle = new PlayScreen(game, new Options(Mode.MENUBATTLE, AI.CUSTOM, 300, 5, 0, 60,
                5, 2, false));

        Gdx.input.setCatchBackKey(true);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		menuBattle.render(dt);
		stage.act(dt);
		stage.draw();
	}

    public void startPlay() {
        dispose();
        game.setScreen(new PlayScreen(game, options));
    }

    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            if (customAI.isVisible()) {
                customAI.setVisible(false);
                difficulties.setVisible(true);
            } else if (!modes.isVisible()) {
                difficulties.setVisible(false);
                practiceSettings.setVisible(false);
                optionsMenu.setVisible(false);
                modes.setVisible(true);
            } else
                Gdx.app.exit();
            return true;
        }
        return super.keyDown(keyCode);
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

    @Override
    public void show() {

    }

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
	    menuBattle.dispose();
		stage.dispose();
	}

}
