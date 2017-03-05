package screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pongplus.game.Options;
import com.pongplus.game.Options.AI;
import com.pongplus.game.Options.Mode;
import com.pongplus.game.PongPlus;

import java.util.Random;

public class MenuScreen extends InputAdapter implements Screen {
    private final Texture verticalBarImage, horizontalBarImage, titleTextImage;
    private Stage stage;
	private Table table;
	private Table modes, difficulties, optionsMenu, customAI, practiceMenu;
	private Image titleText, horizontalPlus, verticalPlus;
	private PongPlus game;
	private PlayScreen menuBattle;
	private Options options;

	private Random random;

	public MenuScreen(PongPlus game, final Options opt) {
		this.game = game;
		this.options = opt;
		stage = new Stage(new FitViewport(PongPlus.VIRTUAL_WIDTH, PongPlus.VIRTUAL_HEIGHT), game.batch);
        //stage.setDebugAll(true);
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
        final TextButton buttonOptions = new TextButton("OPTIONS", game.skin);
        final TextButton buttonExit = new TextButton("EXIT", game.skin);
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
                practiceMenu.setVisible(true);
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
                titleText.setVisible(false);
                verticalPlus.setVisible(false);
                horizontalPlus.setVisible(false);
            }
        });
        buttonExit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        modes.add(buttonOptions).fillX().height(button1P.getHeight());
        modes.row();
        modes.add(buttonExit).fillX().height(button1P.getHeight());

        int secondColWidth = 500, thirdColWidth = 160, spacing = 25;

        //difficulties stuff
        difficulties = new Table();
        difficulties.setVisible(false);
        final TextButton buttonEasy = new TextButton("EASY", game.skin);
        final TextButton buttonMedium = new TextButton("MEDIUM", game.skin);
        final TextButton buttonHard = new TextButton("HARD", game.skin);
        final TextButton buttonSkynet = new TextButton("IMPOSSIBLE", game.skin);
        final TextButton buttonCustom = new TextButton("CUSTOM", game.skin);
        final TextButton buttonBack = new TextButton("BACK", game.skin, "LS90");
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

        final Label aiLabel = new Label("CUSTOM AI", game.skin);

        final Label aiSpeedLabel = new Label("MOVE\nSPEED", game.skin, "LS90");
        aiSpeedLabel.setAlignment(Align.center);
        final Slider aiSpeedSlider = new Slider(1, 100, 1, false, game.skin);
        aiSpeedSlider.setValue(options.aiMovespeed);
        final Label aiSpeedNumber = new Label(Integer.toString((int)aiSpeedSlider.getValue()), game.skin, "LS90");
        aiSpeedNumber.setAlignment(Align.center);

        final Label aiOffsetLabel = new Label("AIM", game.skin, "LS90");
        aiOffsetLabel.setAlignment(Align.center);
        final Slider aiOffsetSlider = new Slider(0, 10, 1, false, game.skin);
        aiOffsetSlider.setValue(options.aiOffset);
        final Label aiOffsetNumber = new Label(Integer.toString((int)aiOffsetSlider.getValue()), game.skin, "LS90");

        final TextButton buttonPlayAI = new TextButton("PLAY", game.skin);

        final TextButton buttonBackAI = new TextButton("BACK", game.skin, "LS90");

        aiSpeedSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.aiMovespeed = aiSpeedSlider.getValue();
                aiSpeedNumber.setText(Integer.toString((int)aiSpeedSlider.getValue()));
            }
        });
        aiOffsetSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                options.aiOffset = aiOffsetSlider.getValue();
                aiOffsetNumber.setText(Integer.toString((int)aiOffsetSlider.getValue()));
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

        customAI.add(aiLabel).colspan(3).spaceBottom(spacing * 2);
        customAI.row();
        customAI.add(aiSpeedLabel).space(0, 0, spacing, 0);
        customAI.add(aiSpeedSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        customAI.add(aiSpeedNumber).space(0, 0, spacing, 0).width(thirdColWidth);
        customAI.row();
        customAI.add(aiOffsetLabel).space(spacing, 0, spacing, 0).height(aiSpeedLabel.getPrefHeight());
        customAI.add(aiOffsetSlider).width(secondColWidth).space(spacing).fillX();
        customAI.add(aiOffsetNumber).space(spacing, 0, spacing, 0);
        customAI.row();
        customAI.add(buttonPlayAI).colspan(3).fillX().height(button1P.getHeight());
        customAI.row();
        customAI.add(buttonBackAI).colspan(3).fillX().height(button1P.getHeight());


        practiceMenu = new Table();
        practiceMenu.setVisible(false);

        final Label targetWidthLabel = new Label("TARGET\nWIDTH", game.skin, "LS90");
        targetWidthLabel.setAlignment(Align.center);
        final Slider targetWidthSlider = new Slider(10, 999, 10, false, game.skin);
        targetWidthSlider.setValue(options.targetWidth);
        final Label targetWidthNumber = new Label(Integer.toString((int)targetWidthSlider.getValue()), game.skin, "LS90");
        targetWidthNumber.setAlignment(Align.center);

        final TextButton buttonPlayPractice = new TextButton("TARGETs", game.skin);

        final TextButton buttonWall = new TextButton("SURVIVAL", game.skin);

        final TextButton buttonBackPractice = new TextButton("BACK", game.skin, "LS90");

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
        buttonWall.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.mode = Mode.SURVIVAL;
                startPlay();
            }
        });
        buttonBackPractice.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                practiceMenu.setVisible(false);
                modes.setVisible(true);
            }
        });

        practiceMenu.add(buttonWall).colspan(3).fillX().height(button1P.getHeight()).spaceBottom(spacing);
        practiceMenu.row();
        practiceMenu.add(buttonPlayPractice).colspan(3).fillX().height(button1P.getHeight());
        practiceMenu.row();
        practiceMenu.add(targetWidthLabel).space(spacing, 0, spacing, 0);
        practiceMenu.add(targetWidthSlider).width(secondColWidth).space(spacing).fillX();
        practiceMenu.add(targetWidthNumber).space(spacing, 0, spacing, 0).width(thirdColWidth);
        practiceMenu.row();
        practiceMenu.add(buttonBackPractice).colspan(3).fillX().height(button1P.getHeight());


        //Options menu stuff
        optionsMenu = new Table();
        optionsMenu.setVisible(false);

        final TextButton buttonSound = new TextButton("SOUND", game.skin, "LS90");
        final Label sound = new Label("hi", game.skin, "LS90");
        if (options.soundOn)
            sound.setText("ON");
        else
            sound.setText("OFF");
        sound.setAlignment(Align.center);

        final Label scoreLimitLabel = new Label("SCORE\nLIMIT", game.skin, "LS90");
        scoreLimitLabel.setAlignment(Align.center);
        final Slider scoreLimitSlider = new Slider(1, 10, 1, false, game.skin);
        scoreLimitSlider.setValue(options.scoreLimit);
        final Label scoreLimitNumber = new Label(Integer.toString((int)scoreLimitSlider.getValue()), game.skin, "LS90");

        final Label paddleWidthLabel = new Label("PADDLE\nWIDTH", game.skin, "LS90");
        paddleWidthLabel.setAlignment(Align.center);
        final Slider paddleWidthSlider = new Slider(10, 999, 10, false, game.skin);
        paddleWidthSlider.setValue(options.paddleWidth);
        final Label paddleWidthNumber = new Label(Integer.toString((int)paddleWidthSlider.getValue()), game.skin, "LS90");

        final Label ballLabel = new Label("BALL", game.skin);
        final Label ballSizeLabel = new Label("SIZE", game.skin, "LS90");
        ballSizeLabel.setAlignment(Align.center);
        final Slider ballSizeSlider = new Slider(10, 999, 10, false, game.skin);
        ballSizeSlider.setValue(options.ballSize);
        final Label ballSizeNumber = new Label(Integer.toString((int)ballSizeSlider.getValue()), game.skin, "LS90");
        
        final Label ballInitialSpeedLabel = new Label("START\nSPEED", game.skin, "LS90");
        ballInitialSpeedLabel.setAlignment(Align.center);
        final Slider ballInitialSpeedSlider = new Slider(1, 100, 1, false, game.skin);
        ballInitialSpeedSlider.setValue(options.ballInitialSpeed);
        final Label ballInitialSpeedNumber = new Label(Integer.toString((int)ballInitialSpeedSlider.getValue()), game.skin, "LS90");

        final Label ballSpeedIncreaseLabel = new Label("SPEED\nINCREASE", game.skin, "LS90");
        ballSpeedIncreaseLabel.setAlignment(Align.center);
        final Slider ballSpeedIncreaseSlider = new Slider(0f, 9.9f, 0.5f, false, game.skin);
        ballSpeedIncreaseSlider.setValue(options.ballSpeedIncrease);
        final Label ballSpeedIncreaseNumber = new Label(Integer.toString((int)ballSpeedIncreaseSlider.getValue()), game.skin, "LS90");

        final Label ballAngleLabel = new Label("REBOUND\nANGLE", game.skin, "LS90");
        ballAngleLabel.setAlignment(Align.center);
        final Slider ballAngleSlider = new Slider(0, 100, 1, false, game.skin);
        ballAngleSlider.setValue(options.ballAngleMultiplier);
        final Label ballAngleNumber = new Label(Integer.toString((int)ballAngleSlider.getValue()), game.skin, "LS90");

        final TextButton buttonSmallRandomizeOptions = new TextButton("REASONABLY RANDOM", game.skin, "LS90");

        final TextButton buttonRandomizeOptions = new TextButton("TOTALLY RANDOM", game.skin, "LS90");

        final TextButton buttonResetOptions = new TextButton("DEFAULT", game.skin, "LS90");

        final TextButton buttonBackOptions = new TextButton("BACK", game.skin, "LS90");

        buttonSound.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                options.soundOn = !options.soundOn;
                if (options.soundOn)
                    sound.setText("ON");
                else
                    sound.setText("OFF");
            }
        });
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
                ballSpeedIncreaseNumber.setText(Float.toString(ballSpeedIncreaseSlider.getValue()));
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
                scoreLimitSlider.setValue(3 + random.nextInt(8)); //bound is non-inclusive
                paddleWidthSlider.setValue(100 + random.nextInt(401));
                ballSizeSlider.setValue(50 + random.nextInt(101));
                ballInitialSpeedSlider.setValue(1 + random.nextInt(5));
                ballSpeedIncreaseSlider.setValue(1 + random.nextInt(3));
                ballAngleSlider.setValue(30 + random.nextInt(51));
            }
        });
        buttonRandomizeOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) { //gives random value to each slider, between its min and max values
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
                titleText.setVisible(true);
                verticalPlus.setVisible(true);
                horizontalPlus.setVisible(true);
                options.saveOptions();
            }
        });

        optionsMenu.add(buttonSound).height(scoreLimitLabel.getHeight());
        optionsMenu.add();
        optionsMenu.add(sound).width(thirdColWidth);
        optionsMenu.row();
        optionsMenu.add(scoreLimitLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(scoreLimitSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(scoreLimitNumber).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(paddleWidthLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(paddleWidthSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(paddleWidthNumber).space(0, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballLabel).spaceTop(spacing * 2).colspan(3);
        optionsMenu.row();
        optionsMenu.add(ballSizeLabel).space(0, 0, spacing, 0).height(scoreLimitLabel.getPrefHeight());
        optionsMenu.add(ballSizeSlider).width(secondColWidth).space(0, spacing, spacing, spacing).fillX();
        optionsMenu.add(ballSizeNumber).space(0, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballInitialSpeedLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballInitialSpeedSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(ballInitialSpeedNumber).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballSpeedIncreaseLabel).space(spacing, 0, spacing, 0);
        optionsMenu.add(ballSpeedIncreaseSlider).width(secondColWidth).space(spacing).fillX();
        optionsMenu.add(ballSpeedIncreaseNumber).space(spacing, 0, spacing, 0);
        optionsMenu.row();
        optionsMenu.add(ballAngleLabel).space(spacing, 0, spacing * 2, 0);
        optionsMenu.add(ballAngleSlider).width(secondColWidth).space(0, spacing, spacing * 2, spacing).fillX();
        optionsMenu.add(ballAngleNumber).space(spacing, 0, spacing * 2, 0);
        optionsMenu.row();
        optionsMenu.add(buttonSmallRandomizeOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonRandomizeOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonResetOptions).colspan(3).fillX().height(110);
        optionsMenu.row();
        optionsMenu.add(buttonBackOptions).colspan(3).fillX().height(110);

        stage.addActor(optionsMenu);
        optionsMenu.setX(PongPlus.VIRTUAL_WIDTH / 2);
        optionsMenu.setY(PongPlus.VIRTUAL_HEIGHT / 2);

        Stack menu = new Stack();
        menu.add(modes);
        menu.add(difficulties);
        menu.add(customAI);
        menu.add(practiceMenu);
        stage.addActor(menu);
        menu.setX(PongPlus.VIRTUAL_WIDTH / 2 - menu.getWidth() / 2);
        menu.setY(PongPlus.VIRTUAL_HEIGHT / 2.5f - menu.getHeight() / 2);

        //Stuff for the Title
        //create and position text; origin of the Image is the bottom left corner
        titleTextImage = new Texture("title/titleTextImage.png");
        titleText = new Image(titleTextImage);
        titleText.setWidth(titleTextImage.getWidth());
        titleText.setHeight(titleTextImage.getHeight());
        stage.addActor(titleText);
        //create and position horizontal part of the eventual plus sign
        horizontalBarImage = new Texture("title/horizontalBar.png");
        horizontalPlus = new Image(horizontalBarImage);
        horizontalPlus.setWidth(horizontalBarImage.getWidth());
        horizontalPlus.setHeight(horizontalBarImage.getHeight());
        stage.addActor(horizontalPlus);
        //vertical part of the plus
        verticalBarImage = new Texture("title/verticalBar.png");
        verticalPlus = new Image(verticalBarImage);
        verticalPlus.setWidth(verticalBarImage.getWidth());
        verticalPlus.setHeight(verticalBarImage.getHeight());
        stage.addActor(verticalPlus);

        if (options.startup) {
            titleText.setX(PongPlus.VIRTUAL_WIDTH / 2 - titleText.getPrefWidth() / 2);
            titleText.setY(PongPlus.VIRTUAL_HEIGHT / 4 * 3 - titleText.getPrefHeight() / 2);
            horizontalPlus.setX(PongPlus.VIRTUAL_WIDTH);
            horizontalPlus.setY(PongPlus.VIRTUAL_HEIGHT / 4 * 3 - horizontalPlus.getPrefHeight() / 2);
            verticalPlus.setX(PongPlus.VIRTUAL_WIDTH + (-verticalPlus.getPrefWidth() / 2) + (-horizontalPlus.getPrefWidth() / 16) + (titleText.getX() - horizontalPlus.getX() + titleText.getPrefWidth() + horizontalPlus.getPrefWidth() / 8));
            verticalPlus.setY(PongPlus.VIRTUAL_HEIGHT);
            //add the movement actions
            float initialDelay = 3f, verDelay = 0.5f, horDuration = 0.025f, verDuration = 0.05f, bothDuration = 0.025f;

            verticalPlus.addAction(sequence(
                    delay(initialDelay + verDelay + horDuration + bothDuration),
                    moveBy(0, horizontalPlus.getY() - verticalPlus.getY() - verticalPlus.getPrefHeight() / 2 + horizontalPlus.getPrefHeight() / 2, verDuration)
            ));
            horizontalPlus.addAction(sequence(
                    delay(initialDelay),
                    moveBy(titleText.getX() - horizontalPlus.getX() + titleText.getPrefWidth() + horizontalPlus.getPrefWidth() / 8, 0, horDuration),//move it to the text
                    moveBy(-horizontalPlus.getPrefWidth() / 2 - horizontalPlus.getPrefWidth() / 16, 0, bothDuration)
            ));
            titleText.addAction(sequence(
                    delay(initialDelay + horDuration),
                    moveBy(-horizontalPlus.getPrefWidth() / 2 - horizontalPlus.getPrefWidth() / 16, 0, bothDuration)
            ));
            options.startup = false;
        } else {
            titleText.setX((PongPlus.VIRTUAL_WIDTH / 2 - titleText.getPrefWidth() / 2) - horizontalPlus.getPrefWidth() / 2 - horizontalPlus.getPrefWidth() / 16);
            titleText.setY(PongPlus.VIRTUAL_HEIGHT / 4 * 3 - titleText.getPrefHeight() / 2);
            horizontalPlus.setX(titleText.getX() + titleText.getPrefWidth() + horizontalPlus.getPrefWidth() / 8);
            horizontalPlus.setY(PongPlus.VIRTUAL_HEIGHT / 4 * 3 - horizontalPlus.getPrefHeight() / 2);
            verticalPlus.setX(horizontalPlus.getX() + horizontalPlus.getPrefWidth() / 2 - verticalPlus.getPrefWidth() / 2);
            verticalPlus.setY(horizontalPlus.getY() - verticalPlus.getPrefHeight() / 2 + horizontalPlus.getPrefHeight() / 2);
        }

        //to have changes to the options affect the menubattle, pass options to this, rather than a new Options
        menuBattle = new PlayScreen(game, new Options(Mode.MENUBATTLE, AI.CUSTOM, 300, 5, 0, 60,
                5, 8, false));

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
		stage.draw(); //note that for scene2d, the origin is the bottom left corner, and it's using pixel coordinates
	}

    public void startPlay() {
        dispose();
        game.setScreen(new PlayScreen(game, options));
    }

    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
            options.saveOptions();
            if (customAI.isVisible()) {
                customAI.setVisible(false);
                difficulties.setVisible(true);
            } else if (!modes.isVisible()) {
                difficulties.setVisible(false);
                practiceMenu.setVisible(false);
                optionsMenu.setVisible(false);
                modes.setVisible(true);
                titleText.setVisible(true);
                verticalPlus.setVisible(true);
                horizontalPlus.setVisible(true);
            } else
                //Gdx.app.exit();
            return true;
        }
        return super.keyDown(keyCode);
    }

    @Override
    public void dispose() {
        menuBattle.dispose();
        stage.dispose();
        horizontalBarImage.dispose();
        verticalBarImage.dispose();
        titleTextImage.dispose();
    }

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height,true);
		//menuBattle.resize(width, height);
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
}
