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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.PowerPong;

import java.awt.*;

public class MenuScreen implements Screen {
    public enum Mode {
        CLASSIC, AIBATTLE, WALL
    }
    public static Mode mode;
	private Stage stage;
	private Table table;
	private Table modes, difficulties;
	private Skin skin;
	private PowerPong game;
	private AIBattle ai;

	public MenuScreen(final PowerPong game) {
		this.game = game;
		stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		// Load skin from JSON file
		skin = new Skin(Gdx.files.internal("skins/neon/neon-ui.json"));

		//add the menu background image to the skin, under the name background
		skin.add("background", new Texture("MenuBackground.png"));

		// Generate a font and add it to the skin under the name "Xcelsion"
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ARCADECLASSIC.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 175;
		skin.add("font", generator.generateFont(parameter));

		//get the TextButtonStyle defined in the JSON under the name "default" and then modify it
		TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
		textButtonStyle.font = skin.getFont("font");
        textButtonStyle.checked = null;
		textButtonStyle.up = null;
		textButtonStyle.down = null;
		textButtonStyle.over = null;
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.overFontColor = Color.GRAY;

		// Create a table that fills the screen. Everything else will go inside this table.
		table = new Table();
		table.setSkin(skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setBackground("background");
		table.setFillParent(true);
		stage.addActor(table);


        //stuff for the modes VerticalGroup; gamemodes, options, etc
        modes = new Table();
        // Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button1P = new TextButton("One Player", skin);
        button1P.setHeight(175);
        button1P.setWidth(button1P.getPrefWidth() + 50);
        modes.add(button1P).width(button1P.getWidth()).height(button1P.getHeight());
        modes.row();
        final TextButton button2P = new TextButton("Two Player", skin);
        modes.add(button2P).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonWall = new TextButton("Wall Mode", skin);
        modes.add(buttonWall).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonAIBattle = new TextButton("AI Battle", skin);
        modes.add(buttonAIBattle).fillX().height(button1P.getHeight());
        modes.row();
        final TextButton buttonOptions = new TextButton("Options", skin);
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
                mode = Mode.CLASSIC;
            }
        });
        button2P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                mode = Mode.CLASSIC;
                startPlay(PlayScreen.AI.NONE);
            }
        });
        buttonWall.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                mode = Mode.WALL;
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
        final TextButton buttonEasy = new TextButton("Easy", skin);
        difficulties.add(buttonEasy).width(button1P.getWidth()).height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonMedium = new TextButton("Medium", skin);
        difficulties.add(buttonMedium).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonHard = new TextButton("Hard", skin);
        difficulties.add(buttonHard).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonSkynet = new TextButton("Skynet", skin);
        difficulties.add(buttonSkynet).fillX().height(button1P.getHeight());
        difficulties.row();
        final TextButton buttonBack = new TextButton("Back", skin);
        difficulties.add(buttonBack).fillX().height(button1P.getHeight());
        buttonEasy.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(PlayScreen.AI.EASY);
            }
        });
        buttonMedium.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(PlayScreen.AI.MEDIUM);
            }
        });
        buttonHard.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(PlayScreen.AI.HARD);
            }
        });
        buttonSkynet.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(PlayScreen.AI.SKYNET);
            }
        });
        buttonBack.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                difficulties.setVisible(false);
                modes.setVisible(true);
                button1P.setChecked(false);
                buttonAIBattle.setChecked(false);
            }
        });

        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        stage.addActor(menu);
        menu.setX(PowerPong.NATIVE_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(800);
        //table.add(menu);

        ai = new AIBattle(game, PlayScreen.AI.SKYNET);
	}

	@Override
	public void show() {
		game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(false);
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
		skin.dispose();
	}

	public void startPlay(PlayScreen.AI ai) {
        dispose();
        if (mode == Mode.WALL)
            game.setScreen(new WallPlayScreen(game));
        else if (mode == Mode.CLASSIC)
            game.setScreen(new ClassicPlayScreen(game, ai));
        else if (mode == Mode.AIBATTLE)
            game.setScreen(new AIBattle(game, ai));
    }

}
