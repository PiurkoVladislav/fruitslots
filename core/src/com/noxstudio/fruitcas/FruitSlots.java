package com.noxstudio.fruitcas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.noxstudio.fruitcas.entities.Element;
import com.noxstudio.fruitcas.entities.Line;
import com.noxstudio.fruitcas.entities.Slot;
import com.noxstudio.fruitcas.util.Constants;


public class FruitSlots extends ScreenAdapter {

    final FruitSlotsGame mFruitSlotsGame;

	private SpriteBatch batch;
	private Texture backgroundTexture;
	private Sprite bsckgroundSprite;
	private Slot slotOne;
	private Slot slotTwo;
	private Slot slotThree;
	private Slot slotFour;
	private Slot slotFive;
	private GameUI mUI;
	private boolean isSpinBurronClick = false;

	private Stage stage;
	private Skin skin;
	private Texture spinButtonTexture;
	private Texture maxBetTexture;
	private Texture betTexture;
	private TextureRegion spinButtonTextureRegion;
	private TextureRegion maxBetTextureRegion;
	private TextureRegion betTextureRegion;
	private TextureRegionDrawable spinButtonTexRegionDrawable;
	private TextureRegionDrawable maxBetTexRegionDrawable;
	private TextureRegionDrawable betTexRegionDrawable;
	private ImageButton button;
	private ImageButton maxBetButton;
	private ImageButton betButton;
	private int balace = 600000;
	private int win = 0;
	private int bet = 20;
	private float progress = 0;
	private int lvl = 1;
	private boolean isWin = false;
	private boolean isChecked;


	private Line lineOne;
	private Line lineTwo;
	private Line lineTree;

	private boolean firstLine = false;
	private boolean secondLine = false;
	private boolean thirdLine = false;

	private BitmapFont balanceFont;
	private BitmapFont winFont;
	private BitmapFont betFont;
	private BitmapFont betText;
	private BitmapFont lvlFont;

	private ProgressBar bar;
	private ProgressBar.ProgressBarStyle barStyle;

	private boolean isAutoPlayClicked = false;
	private boolean isButtonMaxBet = false;

	private boolean buttonIsPressed;

	private int tick;
	private int slotsStopTick;
	private int tickToch;
	


    public FruitSlots(final FruitSlotsGame fruitSlotsGame) {

    	this.mFruitSlotsGame = fruitSlotsGame;

    	batch = new SpriteBatch();

    	mUI = new GameUI();

        init();

		ExtendViewport extendViewport=new ExtendViewport(Constants.width,Constants.height);
		stage=new Stage(extendViewport);

        betButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            	if(isSlotsStop()) {
					if (x >= betButton.getPadX() && x <= betButton.getWidth() / 2) {
						if (bet > 10 && balace - 10 > 0) {
							bet -= 10;
						}
					} else if (x >= betButton.getWidth() / 2 && x <= betButton.getWidth()) {
						if (balace > bet + 10) {
							bet += 10;
						}
					}
				}
                return super.touchDown(event,x,y,pointer,button);
            }
        });

		button.addListener(new InputListener(){
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(tickToch>50) {
					isAutoPlayClicked = !isAutoPlayClicked;
					spinSlots();
				}else if (balace>=bet&&!slotOne.isCircle() && !slotTwo.isCircle() && !slotThree.isCircle() && !slotFour.isCircle() && !slotFive.isCircle()) {
					slotOne.setCount(0);
					slotTwo.setCount(0);
					slotThree.setCount(0);
					slotFour.setCount(0);
					slotFive.setCount(0);
					tick = 0;
					balace-=bet;
                    if(firstLine){
                        lineOne.dispose();
                        firstLine=false;
                    }
                    if(secondLine){
                        lineTwo.dispose();
                        secondLine = false;
                    }
                    if(thirdLine){
                        lineTree.dispose();
                        thirdLine = false;
                    }
					isSpinBurronClick = true;
					isChecked = false;
				}
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                tickToch = 0;
				return true;
			}
		});

		maxBetButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            	bet = balace;
            }
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {

				return true;
			}
		});


        stage = new Stage(extendViewport); //Set up a stage for the ui
		stage.addActor(maxBetButton);
		stage.addActor(button); //Add the button to the stage to perform rendering and take input.

		stage.addActor(betButton);
		stage.addActor(bar);

        Gdx.input.setInputProcessor(stage);



    }

    public void spinSlots(){
		if (isSlotsStop()){
			if (balace >= bet && !slotOne.isCircle() && !slotTwo.isCircle() && !slotThree.isCircle() && !slotFour.isCircle() && !slotFive.isCircle()) {
				slotOne.setCount(0);
				slotTwo.setCount(0);
				slotThree.setCount(0);
				slotFour.setCount(0);
				slotFive.setCount(0);
				tick = 0;
				balace -= bet;
				if (firstLine) {
					lineOne.dispose();
					firstLine = false;
				}
				if (secondLine) {
					lineTwo.dispose();
					secondLine = false;
				}
				if (thirdLine) {
					lineTree.dispose();
					thirdLine = false;
				}
				isChecked = false;
				isButtonMaxBet = true;
			}
		}
	}

    @Override
	public void show () {


	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}

	private void init(){
		backgroundTexture = new Texture(Gdx.files.internal("bg.jpg"));
		bsckgroundSprite = new Sprite(backgroundTexture);
		bsckgroundSprite.setSize(Constants.width,Constants.height);
		bsckgroundSprite.setPosition(0,0f);

		slotThree = new Slot(new Vector2(Constants.width/2 - Constants.slotElementWidth/2,0),20);
		slotTwo = new Slot(new Vector2(slotThree.getPosition().x - Constants.slotElementWidth,0),15);
		slotOne = new Slot(new Vector2(slotTwo.getPosition().x - Constants.slotElementWidth,0),10);
		slotFour = new Slot(new Vector2(slotThree.getPosition().x+ Constants.slotElementWidth,0),25);
		slotFive = new Slot(new Vector2(slotFour.getPosition().x+ Constants.slotElementWidth,0),30);

		spinButtonTexture = new Texture(Gdx.files.internal("data/buttons/7.png"));
		maxBetTexture = new Texture(Gdx.files.internal("data/buttons/8.png"));
		betTexture = new Texture("data/buttons/10.png");

		spinButtonTextureRegion = new TextureRegion(spinButtonTexture);
		maxBetTextureRegion = new TextureRegion(maxBetTexture);
		betTextureRegion = new TextureRegion(betTexture);

		spinButtonTexRegionDrawable = new TextureRegionDrawable(spinButtonTextureRegion);
		maxBetTexRegionDrawable = new TextureRegionDrawable(maxBetTextureRegion);
		betTexRegionDrawable = new TextureRegionDrawable(betTextureRegion);

		button = new ImageButton(spinButtonTexRegionDrawable); //Set the button up
		maxBetButton = new ImageButton(maxBetTexRegionDrawable);
		betButton = new ImageButton(betTexRegionDrawable);

		button.setPosition(Constants.width/2+(2*Constants.quarterScreenHeight)-Constants.quarterScreenHeight/2+10,
				Constants.height/2 - (11*(Constants.quarterScreenHeight/5)));
		maxBetButton.setPosition((Constants.width/2)+(Constants.quarterScreenHeight/2)+Constants.quarterScreenHeight/3,
				Constants.height/2 - (11*(Constants.quarterScreenHeight/5)));
		betButton.setPosition((Constants.width/2)-(3*Constants.quarterScreenHeight)+Constants.quarterScreenHeight/5,
				Constants.height/2 - (2*Constants.quarterScreenHeight));

		button.setSize(Constants.slotElementWidth+Constants.quarterScreenHeight/3,
				Constants.slotElementHeight);
		maxBetButton.setSize(Constants.quarterScreenHeight-Constants.quarterScreenHeight/8,
				Constants.quarterScreenHeight);
		betButton.setSize(2*Constants.quarterScreenHeight,
				Constants.quarterScreenHeight/2+Constants.quarterScreenHeight/8);

		slotOne.init();
		slotTwo.init();
		slotThree.init();
		slotFour.init();
		slotFive.init();

		balanceFont = new BitmapFont(Gdx.files.internal("data/fonts/myFont.fnt"));
		balanceFont.setColor(Color.YELLOW);
		balanceFont.getData().setScale(Constants.quarterScreenHeight*0.0075f);

		winFont = new BitmapFont(Gdx.files.internal("data/fonts/myFont.fnt"));
		winFont.setColor(Color.WHITE);
		winFont.getData().setScale(Constants.quarterScreenHeight*0.0075f);

		betFont = new BitmapFont(Gdx.files.internal("data/fonts/myFont.fnt"));
		betFont.setColor(Color.WHITE);
		betFont.getData().setScale(Constants.quarterScreenHeight*0.0075f);

		betText = new BitmapFont(Gdx.files.internal("data/fonts/myFont.fnt"));
		betText.setColor(Color.WHITE);
		betText.getData().setScale(Constants.quarterScreenHeight*0.0075f);

		lvlFont = new BitmapFont(Gdx.files.internal("data/fonts/myFont.fnt"));
		lvlFont.setColor(Color.WHITE);
		lvlFont.getData().setScale(Constants.quarterScreenHeight*0.0075f);

		lineOne = new Line();
		lineTwo = new Line();
		lineTree = new Line();

		skin = new Skin();
		Pixmap pixmapYellow = new Pixmap(Constants.quarterScreenHeight/10,
				Constants.quarterScreenHeight/4, Pixmap.Format.RGBA8888);
		pixmapYellow.setColor(Color.YELLOW);
		pixmapYellow.fill();
		skin.add("yellow", new Texture(pixmapYellow));

		Pixmap pixmapWhite = new Pixmap(Constants.quarterScreenHeight/4,
				Constants.quarterScreenHeight/4, Pixmap.Format.RGBA8888);
		pixmapWhite.setColor(Color.BLACK);
		pixmapWhite.fill();
		skin.add("white", new Texture(pixmapWhite));

		barStyle = new ProgressBar.ProgressBarStyle(skin.getDrawable("white"),skin.getDrawable("yellow"));

		bar = new ProgressBar(0, 10, 0.1f, false, barStyle);
		barStyle.knobBefore = barStyle.knob;
		bar.setPosition((Constants.width/2)+(Constants.quarterScreenHeight),
				Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/3  );
		bar.setSize(Constants.quarterScreenHeight+Constants.quarterScreenHeight/2,
				Constants.quarterScreenHeight/4);

	}

	@Override
	public void render (float detla) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		bsckgroundSprite.draw(batch);
		slotOne.render(batch);
		slotTwo.render(batch);
		slotThree.render(batch);
		slotFour.render(batch);
		slotFive.render(batch);
        mUI.render(batch);
        move(detla);
		balanceFont.draw(batch,""+balace,(Constants.width/2)-(2*Constants.quarterScreenHeight+Constants.quarterScreenHeight/3),
				Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/2);
		betText.draw(batch,"BET",betButton.getX()- Constants.quarterScreenHeight/2,
				Constants.height/2 - (2*Constants.quarterScreenHeight));
        if (isSlotsStop()&&(isSpinBurronClick||isButtonMaxBet)&&!isChecked){
            onCheckWin(batch);
			isChecked = true;
        }
        if(firstLine){
				lineOne.render(batch);
			}
		if (secondLine){
				lineTwo.render(batch);
			}

		if(thirdLine){
				lineTree.render(batch);
			}

		winFont.draw(batch,"WIN: "+win,(Constants.width/2) - winFont.getSpaceWidth()*7,
//                        -(Constants.quarterScreenHeight/2),
				Constants.height/2 - (Constants.quarterScreenHeight) - Constants.quarterScreenHeight/1.7f);
		if(tickToch >= 30) {
				if (isAutoPlayClicked && isSlotsStop() && isChecked && tick >= slotsStopTick) {
					spinSlots();
				}
		}

		batch.end();
//        moneyFont.draw(batch,""+balace,(Constants.width/2)-(3*Constants.quarterScreenHeight-Constants.quarterScreenHeight/4),Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/4);
		stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
		stage.draw();
		batch.begin();
		lvlFont.draw(batch,"lvl "+lvl,(Constants.width/2)+(Constants.quarterScreenHeight+Constants.quarterScreenHeight/3),
				Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/3+Constants.sixthOfHeight/3 );
		betFont.draw(batch,""+bet,(Constants.width/2)-(2*Constants.quarterScreenHeight+Constants.quarterScreenHeight/5),
				Constants.height/2 - (Constants.quarterScreenHeight) - Constants.quarterScreenHeight/1.7f);
		batch.end();
		tick++;
		if(button.isPressed()) {
			tickToch++;
		}

	}


	@Override
	public void dispose() {
		backgroundTexture.dispose();
		slotOne.dispose();
		slotTwo.dispose();
		slotThree.dispose();
		slotFour.dispose();
		slotFive.dispose();
		mUI.dispose();
		lvlFont.dispose();
		winFont.dispose();
		betFont.dispose();
		balanceFont.dispose();
		lineOne.dispose();
		lineTwo.dispose();
		lineTree.dispose();
		stage.dispose();

	}

	public void move(float detla){
    	if (isSpinBurronClick||isButtonMaxBet) {
			slotOne.move(detla, Constants.height*2.4f);
			slotTwo.move(detla, Constants.height*2.5f);
			slotThree.move(detla, Constants.height*2.6f);
			slotFour.move(detla, Constants.height*2.7f);
			slotFive.move(detla, Constants.height*2.8f);
		}

    }

    private void onCheckWin(SpriteBatch batch){
    	win = 0;
    	progress = 0;
    		checkWinLine(slotOne.getFirstElement(),slotTwo.getFirstElement(),slotThree.getFirstElement(),
					slotFour.getFirstElement(),slotFive.getFirstElement(),batch, 1);
    		checkWinLine(slotOne.getSecondElement(),slotTwo.getSecondElement(),slotThree.getSecondElement(),
                            slotFour.getSecondElement(),slotFive.getSecondElement(),batch,2);
    		checkWinLine(slotOne.getThirdElement(),slotTwo.getThirdElement(),slotThree.getThirdElement(),
                            slotFour.getThirdElement(),slotFive.getThirdElement(),batch,3);
		balace += win;
		if(bar.getValue()+progress>=10){
            float val = (bar.getValue()+progress)-10;
            bar.setValue(val);
            lvl++;
        }
		bar.setValue(bar.getValue()+progress);
	}

	private void checkWinLine(Element one,Element two, Element three, Element four, Element five,SpriteBatch batch, int lineNumber){
		int youWin = 0;
		float yourProgress = 0;
		slotsStopTick = tick+50;
    	if (one.getName().equals(two.getName())&&
				one.getName().equals(three.getName())&&
				one.getName().equals(four.getName())&&
				one.getName().equals(five.getName())){
			youWin += bet*200;
			isWin =  true;
			yourProgress += 0.5f/lvl;
			if(lineNumber==1){
				firstLine = true;
				lineOne.init(one.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else if(lineNumber==2){
				secondLine = true;
				lineTwo.init(one.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else {
				thirdLine = true;
				lineTree.init(one.getStartPosition(), new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}

		}else if (one.getName().equals(two.getName())&&
				one.getName().equals(three.getName())&&
				one.getName().equals(four.getName())){
			youWin += bet*50;
			isWin = true;
            yourProgress += 0.4f/lvl;

			if(lineNumber==1){
				firstLine = true;
				lineOne.init(one.getStartPosition(),new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}else if(lineNumber==2){
				secondLine = true;
				lineTwo.init(one.getStartPosition(),new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}else {
				thirdLine = true;
				lineTree.init(one.getStartPosition(),new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}

		}else if (one.getName().equals(two.getName())&&
				one.getName().equals(three.getName())) {
			youWin += bet * 10;
			isWin = true;
            yourProgress += 0.3f/lvl;

			if (lineNumber == 1) {
				firstLine = true;
				lineOne.init(one.getStartPosition(), new Vector2(three.getStartPosition().x+Constants.quarterScreenHeight,three.getStartPosition().y));
			} else if (lineNumber == 2) {
				secondLine = true;
				lineTwo.init(one.getStartPosition(), new Vector2(three.getStartPosition().x+Constants.quarterScreenHeight,three.getStartPosition().y));
			} else  {
				thirdLine = true;
				lineTree.init(one.getStartPosition(), new Vector2(three.getStartPosition().x+Constants.quarterScreenHeight,three.getStartPosition().y));
			}
		}

		else if (two.getName().equals(three.getName())&&
				two.getName().equals(four.getName())&&
				two.getName().equals(five.getName())){
			youWin += bet*50;
			isWin = true;
            yourProgress += 0.4f/lvl;

			if(lineNumber==1){
				firstLine = true;
				lineOne.init(two.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else if(lineNumber==2){
				secondLine = true;
				lineTwo.init(two.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else {
				thirdLine = true;
				lineTree.init(two.getStartPosition(), new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}

		}
		else if (two.getName().equals(three.getName())&&
				two.getName().equals(four.getName())) {
			youWin += bet * 10;
			isWin = true;
            yourProgress += 0.3f/lvl;

			if(lineNumber==1){
				firstLine = true;
				lineOne.init(two.getStartPosition(),new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}else if(lineNumber==2){
				secondLine = true;
				lineTwo.init(two.getStartPosition(),new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}else {
				thirdLine = true;
				lineTree.init(two.getStartPosition(), new Vector2(four.getStartPosition().x+Constants.quarterScreenHeight,four.getStartPosition().y));
			}

		}else if (three.getName().equals(four.getName())&&
				three.getName().equals(five.getName())) {
            youWin += bet * 10;
            isWin = true;
            yourProgress += 0.3f/lvl;
			if(lineNumber==1){
				firstLine= true;
				lineOne.init(three.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else if(lineNumber==2){
				secondLine = true;
				lineTwo.init(three.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}else {
				thirdLine = true;
				lineTree.init(three.getStartPosition(),new Vector2(five.getStartPosition().x+Constants.quarterScreenHeight,five.getStartPosition().y));
			}
		}
		win += youWin;
    	progress +=yourProgress;
	}

	private boolean isSlotsStop(){
        if(!slotOne.isCircle()&&!slotTwo.isCircle()&&!slotThree.isCircle()&&!slotFour.isCircle()&&!slotFive.isCircle()){
            return true;

        }else{
            return false;
        }
    }


}
