package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.powerpong.game.PowerPong;
import com.badlogic.gdx.graphics.Color;

public class MenuScreen implements Screen {
	private Stage stage;
	private Table table;
	private VerticalGroup left, mid, right;
	private Skin skin;
	private PowerPong game;
	public String mode;

	public MenuScreen(final PowerPong game) {
		this.game = game;
		stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT), game.batch);
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		// Load skin from JSON file
		skin = new Skin(Gdx.files.internal("skins/neon/neon-ui.json"));

		//add a 1x1 texture to the skin
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

		//add the menu background image to the skin, under the name background
		skin.add("background", new Texture("MenuBackground.png"));

		// Generate a font and add it to the skin under the name "Xcelsion"
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Xcelsion.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 70;
		skin.add("Xcelsion", generator.generateFont(parameter));

		//get the TextButtonStyle defined in the JSON under the name "default" and then modify it
		TextButtonStyle style = skin.get("default", TextButtonStyle.class);
		style.font = skin.getFont("Xcelsion");
		style.checked = style.down;

		// Create a table that fills the screen. Everything else will go inside this table.
		table = new Table();
		table.setSkin(skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setBackground("background");
		table.setFillParent(true);
		table.align(Align.left);
		stage.addActor(table);

		//mid stuff; one or two player options, or online
        mid = new VerticalGroup();
        mid.setVisible(false);
        mid.fill();
        final TextButton button1P = new TextButton("One\nPlayer", skin);
        mid.addActor(button1P);
        final TextButton button2P = new TextButton("Two\nPlayer", skin);
        mid.addActor(button2P);
        final TextButton buttonOnline = new TextButton("Online", skin); //TODO: find out how to make Online button same height as others
        mid.addActor(buttonOnline);
        button1P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                right.setVisible(!right.isVisible());
            }
        });
        button2P.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                startPlay(PlayScreen.AI.NONE);
            }
        });
        buttonOnline.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                //TODO later when we implement online stuff
            }
        });

        //stuff for the left VerticalGroup; gamemodes, options, etc
        left = new VerticalGroup();
        left.fill();
        // Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
        final TextButton classicButton = new TextButton("Classic\nPong", skin);
        left.addActor(classicButton);
        final TextButton powerButton = new TextButton("Power\nPong", skin);
        left.addActor(powerButton);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        classicButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mid.setVisible(classicButton.isChecked());
                if (powerButton.isChecked())
                    powerButton.setChecked(false);
                else if (button1P.isChecked()) {
                    button1P.setChecked(false);
                    right.setVisible(false);
                }
                mode = "classic";
            }
        });
        powerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                mid.setVisible(powerButton.isChecked());
                if (classicButton.isChecked())
                    classicButton.setChecked(false);
                else if (button1P.isChecked()) {
                    button1P.setChecked(false);
                    right.setVisible(false);
                }
                mode = "power";
            }
        });

        //right stuff; ai difficulties
        right = new VerticalGroup();
        right.fill();
        right.setVisible(false);
        final TextButton buttonEasy = new TextButton("Easy", skin);
        right.addActor(buttonEasy);
        final TextButton buttonMedium = new TextButton("Medium", skin);
        right.addActor(buttonMedium);
        final TextButton buttonHard = new TextButton("Hard", skin);
        right.addActor(buttonHard);
        final TextButton buttonSkynet = new TextButton("Skynet", skin);
        right.addActor(buttonSkynet);

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
        table.add(mid).top();
        table.add(right).top();

		table.setDebug(true);
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
		stage.act(dt);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width,height,true);
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
		stage.dispose();
		skin.dispose();
	}

	public void startPlay(PlayScreen.AI ai) {
        dispose();
        if (mode.equals("classic"))
            game.setScreen(new ClassicPlayScreen(game, ai));
        else if (mode.equals("power"))
            game.setScreen(new PowerPongPlayScreen(game, ai));
    }

}
