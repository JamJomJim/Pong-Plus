package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.PowerPong;
import screens.PlayScreen.Mode;
import screens.PlayScreen.AI;

public class MenuScreen implements Screen {
    public static Mode mode;
	private Stage stage;
	private Table table;
	private Table modes, difficulties;
	private PowerPong game;
	private PlayScreen ai;

	public MenuScreen(PowerPong game) {
		this.game = game;
		stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
        game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
		stage.setDebugAll(true);
        Gdx.input.setCatchBackKey(false);

		// Create a table that fills the screen. Everything else will go inside this table.
		table = new Table();
		table.setSkin(game.skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setBackground("background");
		table.setFillParent(true);
		stage.addActor(table);


        //stuff for the modes VerticalGroup; gamemodes, options, etc
        modes = new Table();
        // Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button1P = new TextButton("One Player", game.skin);
        button1P.setHeight(175);
        button1P.setWidth(button1P.getPrefWidth() + 50);
        modes.add(button1P).width(button1P.getWidth()).height(button1P.getHeight());
        modes.row();
        final TextButton button2P = new TextButton("Two Player", game.skin);
        modes.add(button2P).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonAIBattle = new TextButton("AI Battle", game.skin);
        modes.add(buttonAIBattle).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonWall = new TextButton("Survival", game.skin);
        modes.add(buttonWall).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonOptions = new TextButton("Options", game.skin);
        modes.add(buttonOptions).fillX().height(button1P.getHeight());
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button1P.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(button1P.isChecked());
                if (buttonAIBattle.isChecked())
                    buttonAIBattle.setChecked(false);
                modes.setVisible(false);
                mode = Mode.ONEPLAYER;
            }
        });
        button2P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                mode = Mode.TWOPLAYER;
                startPlay(PlayScreen.AI.NONE);
            }
        });
        buttonWall.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                mode = Mode.SURVIVAL;
                startPlay(PlayScreen.AI.NONE);
            }
        });
        buttonAIBattle.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                difficulties.setVisible(buttonAIBattle.isChecked());
                if (button1P.isChecked())
                    button1P.setChecked(false);
                modes.setVisible(false);
                mode = Mode.AIBATTLE;
            }
        });

        //difficulties stuff; ai difficulties
        difficulties = new Table();
        difficulties.setVisible(false);
        final TextButton buttonEasy = new TextButton("Easy", game.skin);
        difficulties.add(buttonEasy).width(button1P.getWidth()).height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonMedium = new TextButton("Medium", game.skin);
        difficulties.add(buttonMedium).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonHard = new TextButton("Hard", game.skin);
        difficulties.add(buttonHard).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonSkynet = new TextButton("Skynet", game.skin);
        difficulties.add(buttonSkynet).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonBack = new TextButton("Back", game.skin);
        difficulties.add(buttonBack).fillX().height(button1P.getHeight());
        buttonEasy.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(AI.EASY);
            }
        });
        buttonMedium.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(AI.MEDIUM);
            }
        });
        buttonHard.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(AI.HARD);
            }
        });
        buttonSkynet.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(AI.SKYNET);
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

        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(700);

        ai = new PlayScreen(game, Mode.MENUBATTLE, AI.SKYNET);
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

	public void startPlay(PlayScreen.AI ai) {
        dispose();
        game.setScreen(new PlayScreen(game, mode, ai));
    }

}
