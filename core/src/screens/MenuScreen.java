package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

public class MenuScreen implements Screen {
    public enum Mode {
        CLASSIC, AIBATTLE
    }
    public static Mode mode;
	private Stage stage;
	private Table table;
	private Table left, right;
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
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Xcelsion.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 70;
		skin.add("Xcelsion", generator.generateFont(parameter));

		//get the TextButtonStyle defined in the JSON under the name "default" and then modify it
		TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
		textButtonStyle.font = skin.getFont("Xcelsion");
		textButtonStyle.checked = textButtonStyle.down;

		// Create a table that fills the screen. Everything else will go inside this table.
		table = new Table();
		table.setSkin(skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setBackground("background");
		table.setFillParent(true);
		table.align(Align.left);
		stage.addActor(table);


        //stuff for the left VerticalGroup; gamemodes, options, etc
        left = new Table();
        // Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
        final TextButton button1P = new TextButton("One\nPlayer", skin);
        left.add(button1P).fillX();
        left.row();
        final TextButton button2P = new TextButton("Two\nPlayer", skin);
        left.add(button2P).fillX();
        left.row();
        final TextButton buttonAIBattle = new TextButton("AI\nBattle", skin);
        left.add(buttonAIBattle).fillX();
        left.row();
        final TextButton buttonOptions = new TextButton("Options", skin);
        left.add(buttonOptions).fillX().prefHeight(button1P.getPrefHeight());
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button1P.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                right.setVisible(button1P.isChecked());
                if (buttonAIBattle.isChecked())
                    buttonAIBattle.setChecked(false);
                mode = Mode.CLASSIC;
            }
        });
        button2P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                mode = Mode.CLASSIC;
                startPlay(PlayScreen.AI.NONE);
            }
        });
        buttonAIBattle.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                right.setVisible(buttonAIBattle.isChecked());
                if (button1P.isChecked())
                    button1P.setChecked(false);
                mode = Mode.AIBATTLE;
            }
        });

        //right stuff; ai difficulties
        right = new Table();
        right.setVisible(false);
        final TextButton buttonEasy = new TextButton("Easy", skin);
        right.add(buttonEasy).fillX().prefHeight(button1P.getPrefHeight());
        right.row();
        final TextButton buttonMedium = new TextButton("Medium", skin);
        right.add(buttonMedium).fillX().prefHeight(button1P.getPrefHeight());
        right.row();
        final TextButton buttonHard = new TextButton("Hard", skin);
        right.add(buttonHard).fillX().prefHeight(button1P.getPrefHeight());
        right.row();
        final TextButton buttonSkynet = new TextButton("Skynet", skin);
        right.add(buttonSkynet).fillX().prefHeight(button1P.getPrefHeight());

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

        table.add(left).top();
        table.add(right).top();

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
        if (mode == Mode.CLASSIC)
            game.setScreen(new ClassicPlayScreen(game, ai));
        else if (mode == Mode.AIBATTLE)
            game.setScreen(new AIBattle(game, ai));
    }

}
