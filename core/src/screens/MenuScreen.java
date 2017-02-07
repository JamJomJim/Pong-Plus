package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.powerpong.game.PowerPong;

public class MenuScreen implements Screen {
	private Stage stage;
	private Table table;
	private Skin skin;
	private PowerPong game;

	public MenuScreen(final PowerPong game) {
		this.game = game;
		stage = new Stage(new FitViewport(PowerPong.NATIVE_WIDTH, PowerPong.NATIVE_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		// Load skin from JSON file
		skin = new Skin(Gdx.files.internal("skins/neon/skin/neon-ui.json"));

		//add the menu background image to the skin, under the name background
		skin.add("background", new Texture("MenuBackground.png"));

		// Generate a font and add it to the skin under the name "Xcelsion"
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Xcelsion.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		skin.add("Xcelsion", generator.generateFont(parameter));

		//get the TextButtonStyle defined in the JSON under the name "default" and then modify it by changing the font
		TextButtonStyle style = skin.get("default", TextButtonStyle.class);
		style.font = skin.getFont("Xcelsion");

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setSkin(skin); //set the table's skin. This means that all widgets within this table will use the skin's definitions by default
		table.setBackground("background");
		table.setFillParent(true);
		stage.addActor(table);

		// Create a button with the "default" TextButtonStyle of skin. A 3rd parameter can be used to specify a name other than "default".
		final TextButton button1P = new TextButton("One Player", skin);
		table.add(button1P).width(1000).height(150);
		final TextButton button2P = new TextButton("Two Player", skin);
		table.row(); //advance to the next row of the table
		table.add(button2P).width(1000).height(150);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		button1P.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new PlayScreen(game, "1P"));
			}
		});
		button2P.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(new PlayScreen(game, "2P"));
			}
		});

		table.setDebug(true);
	}

	@Override
	public void show() {
		game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
	}

	@Override
	public void render(float dt) {
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

}
