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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.Options;
import com.powerpong.game.Options.AI;
import com.powerpong.game.Options.Mode;
import com.powerpong.game.PowerPong;

public class MenuScreen implements Screen {
	private Stage stage;
	private Table table;
	private Table modes, difficulties, optionsMenu;
	private PowerPong game;
	private PlayScreen ai;
	private Options options;

	public MenuScreen(PowerPong game) {
		this.game = game;
		options = new Options();
		stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
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
        // Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button1P = new TextButton("ONE PLAYER", game.skin);
        button1P.setHeight(160);
        button1P.setWidth(button1P.getPrefWidth() + 50);
        modes.add(button1P).width(button1P.getWidth()).height(button1P.getHeight());
        modes.row();
        final TextButton button2P = new TextButton("TWO PLAYER", game.skin);
        modes.add(button2P).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonAIBattle = new TextButton("AI BATTLE", game.skin);
        modes.add(buttonAIBattle).fillX().height(button1P.getHeight());
        modes.row();
        //Practice
        final TextButton buttonPractice = new TextButton("PRACTICE", game.skin);
        modes.add(buttonPractice).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonWall = new TextButton("SURVIVAL", game.skin);
        modes.add(buttonWall).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonOptions = new TextButton("OPTIONS", game.skin);
        modes.add(buttonOptions).fillX().height(button1P.getHeight());
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
                mode = Mode.PRACTICE;
                startPlay(PlayScreen.AI.NONE);
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

        //difficulties stuff; ai difficulties
        difficulties = new Table();
        difficulties.setVisible(false);
        final TextButton buttonEasy = new TextButton("EASY", game.skin);
        difficulties.add(buttonEasy).width(button1P.getWidth()).height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonMedium = new TextButton("MEDIUM", game.skin);
        difficulties.add(buttonMedium).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonHard = new TextButton("HARD", game.skin);
        difficulties.add(buttonHard).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonSkynet = new TextButton("SKYNET", game.skin);
        difficulties.add(buttonSkynet).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonCustom = new TextButton("CUSTOM", game.skin);
        difficulties.add(buttonCustom).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonBack = new TextButton("BACK", game.skin);
        difficulties.add(buttonBack).fillX().height(button1P.getHeight());
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

        optionsMenu = new Table();
        optionsMenu.setVisible(false);
        final Slider ballInitialSpeed = new Slider(1, 10, 1, false, game.skin);
        ballInitialSpeed.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                options.ballInitialSpeed = ballInitialSpeed.getValue();
            }
        });
        final TextButton buttonBackOptions = new TextButton("BACK", game.skin);
        buttonBackOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                optionsMenu.setVisible(false);
                modes.setVisible(true);
                buttonBackOptions.setChecked(false);
                buttonOptions.setChecked(false);
            }
        });

        optionsMenu.add(ballInitialSpeed);
        optionsMenu.row();
        optionsMenu.add(buttonBackOptions);



        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        menu.add(optionsMenu);
        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(700);

        ai = new PlayScreen(game, new Options(Mode.MENUBATTLE, AI.CUSTOM, 5, 0, 60, 5, 3, false));
        Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ai.render(dt);
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
	    ai.dispose();
		stage.dispose();
	}

	public void startPlay() {
        dispose();
        game.setScreen(new PlayScreen(game, options));
    }

}
