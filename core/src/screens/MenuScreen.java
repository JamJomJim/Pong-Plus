package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class MenuScreen implements Screen {
	private Stage stage;
	private Table table;
	private Table modes, difficulties, optionsMenu;
	private PowerPong game;
	private PlayScreen menuBattle;
	private Options options;

	public MenuScreen(PowerPong game, Options opt) {
		this.game = game;
		this.options = opt;
		stage = new Stage(new StretchViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        stage.setDebugAll(true);
        Gdx.input.setCatchBackKey(false);

		// Create a table that fills the screen
		table = new Table();
		table.setSkin(game.skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		//table.setBackground("background");
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
                difficulties.setVisible(button1P.isChecked());
                modes.setVisible(false);
                options.mode = Mode.ONEPLAYER;
            }
        });
        button2P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.mode = Mode.TWOPLAYER;
                startPlay();
            }
        });
        buttonPractice.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.mode = Mode.PRACTICE;
                startPlay();
            }
        });
        buttonWall.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.mode = Mode.SURVIVAL;
                startPlay();
            }
        });
        buttonAIBattle.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(buttonAIBattle.isChecked());
                modes.setVisible(false);
                options.mode = Mode.AIBATTLE;
            }
        });
        buttonOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(buttonOptions.isChecked());
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


        //difficulties stuff; menuBattle difficulties
        difficulties = new Table();
        difficulties.setVisible(false);
        final TextButton buttonEasy = new TextButton("EASY", game.skin);
        final TextButton buttonMedium = new TextButton("MEDIUM", game.skin);
        final TextButton buttonHard = new TextButton("HARD", game.skin);
        final TextButton buttonSkynet = new TextButton("SKYNET", game.skin);
        final TextButton buttonCustom = new TextButton("CUSTOM", game.skin);
        final TextButton buttonBack = new TextButton("BACK", game.skin, "back button");
        buttonEasy.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ai = AI.EASY;
                startPlay();
            }
        });
        buttonMedium.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ai = AI.MEDIUM;
                startPlay();
            }
        });
        buttonHard.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ai = AI.HARD;
                startPlay();
            }
        });
        buttonSkynet.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ai = AI.SKYNET;
                startPlay();
            }
        });
        buttonCustom.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ai = AI.CUSTOM;
                startPlay();
            }
        });
        buttonBack.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(false);
                modes.setVisible(true);
                button1P.setChecked(false);
                buttonAIBattle.setChecked(false);
                buttonBack.setChecked(false);
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
        final Slider ballAngleSlider = new Slider(1, 100, 1, false, game.skin);
        ballAngleSlider.setValue(options.ballAngleMultiplier);
        final Label ballAngleNumber = new Label(Integer.toString((int)ballAngleSlider.getValue()), game.skin, "options text");

        final Label aiLabel = new Label("CUSTOM AI", game.skin, "options header");
        final Label aiSpeedLabel = new Label("MOVE\nSPEED", game.skin, "options text");
        aiSpeedLabel.setAlignment(Align.center);
        final Slider aiSpeedSlider = new Slider(1, 100, 1, false, game.skin);
        aiSpeedSlider.setValue(options.aiMovespeed);
        final Label aiSpeedNumber = new Label(Integer.toString((int)aiSpeedSlider.getValue()), game.skin, "options text");

        final TextButton buttonResetOptions = new TextButton("DEFAULT", game.skin, "back button");

        final TextButton buttonBackOptions = new TextButton("BACK", game.skin, "back button");

        scoreLimitSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.scoreLimit = scoreLimitSlider.getValue();
                scoreLimitNumber.setText(Integer.toString((int)scoreLimitSlider.getValue()));
            }
        });
        paddleWidthSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.paddleWidth = paddleWidthSlider.getValue();
                paddleWidthNumber.setText(Integer.toString((int)paddleWidthSlider.getValue()));
            }
        });
        ballInitialSpeedSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ballInitialSpeed = ballInitialSpeedSlider.getValue();
                ballInitialSpeedNumber.setText(Integer.toString((int)ballInitialSpeedSlider.getValue()));
            }
        });
        ballSpeedIncreaseSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ballSpeedIncrease = ballSpeedIncreaseSlider.getValue();
                ballSpeedIncreaseNumber.setText(Integer.toString((int)ballSpeedIncreaseSlider.getValue()));
            }
        });
        ballAngleSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ballAngleMultiplier = ballAngleSlider.getValue();
                ballAngleNumber.setText(Integer.toString((int)ballAngleSlider.getValue()));
            }
        });
        aiSpeedSlider.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.aiMovespeed = aiSpeedSlider.getValue();
                aiSpeedNumber.setText(Integer.toString((int)aiSpeedSlider.getValue()));
            }
        });
        buttonResetOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //note that these are hardcoded
                scoreLimitSlider.setValue(5);
                paddleWidthSlider.setValue(300);
                ballInitialSpeedSlider.setValue(3);
                ballSpeedIncreaseSlider.setValue(1);
                ballAngleSlider.setValue(60);
                aiSpeedSlider.setValue(7);
                buttonResetOptions.setChecked(false);
            }
        });
        buttonBackOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(false);
                modes.setVisible(true);
                buttonBackOptions.setChecked(false);
                buttonOptions.setChecked(false);
            }
        });

        int secondColWidth = 500, spacing = 25, thirdColWidth = 50;
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
        optionsMenu.add(ballInitialSpeedLabel).space(0, 0, spacing, 0);
        optionsMenu.add(ballInitialSpeedSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(ballInitialSpeedNumber).width(thirdColWidth).space(0, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballSpeedIncreaseLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballSpeedIncreaseSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(ballSpeedIncreaseNumber).width(thirdColWidth).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballAngleLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballAngleSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(ballAngleNumber).width(thirdColWidth).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(aiLabel).spaceTop(spacing * 2).colspan(3);
        optionsMenu.row();
        optionsMenu.add(aiSpeedLabel).space(0, 0, spacing * 2, 0);
        optionsMenu.add(aiSpeedSlider).width(secondColWidth).space(0, spacing, spacing * 2, spacing).fillX();
        optionsMenu.add(aiSpeedNumber).width(thirdColWidth).space(0, 0, spacing * 2, 0);
        optionsMenu.row();
        optionsMenu.add(buttonResetOptions).colspan(3).fillX().height(110).padBottom(10);
        optionsMenu.row();
        optionsMenu.add(buttonBackOptions).colspan(3).fillX().height(110);



        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        menu.add(optionsMenu);
        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(PowerPong.NATIVE_HEIGHT / 2 - menu.getHeight() / 2);

        //to have changes to the otions affect the menubattle, pass options to this, rather than a new Options
        menuBattle = new PlayScreen(game, new Options(Mode.MENUBATTLE, AI.CUSTOM, 300, 5, 0, 60,
                5, 2, false));
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		menuBattle.render(dt);
		stage.act(dt);
		stage.draw();
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
	public void hide() {

	}

	@Override
	public void dispose() {
	    menuBattle.dispose();
		stage.dispose();
	}

	public void startPlay() {
        dispose();
        game.setScreen(new PlayScreen(game, options));
    }

}
