package com.noxstudio.fruitcas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.noxstudio.fruitcas.util.Constants;

public class GameUI {

    private Texture bottomToolBar;
    private Texture topToolBar;
    private Texture spinButton;
    private Texture bankField;
    private Texture money;
//    private Texture level;
    private Texture win;
    private Texture buttonBet;
    private Texture buttonBet1;


    public GameUI() {
        init();
    }

    public void init(){
        bottomToolBar = new Texture(Gdx.files.internal("data/11.png"));
        topToolBar = new Texture(Gdx.files.internal("data/13.png"));
        bankField = new Texture(Gdx.files.internal("data/12.png"));
        money = new Texture(Gdx.files.internal("data/15.png"));
//        level = new Texture(Gdx.files.internal("data/9.png"));
        win = new Texture(Gdx.files.internal("data/9.png"));
        buttonBet = new Texture(Gdx.files.internal("data/buttons/10.png"));
        buttonBet1 = new Texture(Gdx.files.internal("data/buttons/10.png"));


    }

    public void render(SpriteBatch batch){
        batch.draw(bottomToolBar,- Constants.quarterScreenHeight, - Constants.quarterScreenHeight/2,Constants.width+(2*Constants.quarterScreenHeight),Constants.quarterScreenHeight+Constants.quarterScreenHeight/6);
        batch.draw(topToolBar,-Constants.quarterScreenHeight,Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/10,Constants.width+(2*Constants.quarterScreenHeight),Constants.slotElementHeight);
        batch.draw(bankField,(Constants.width/2)-(Constants.quarterScreenHeight),Constants.height/2 + Constants.height/3,2*Constants.quarterScreenHeight,Constants.height/5);
        batch.draw(money,(Constants.width/2)-(3*Constants.quarterScreenHeight-Constants.quarterScreenHeight/3),Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/4,Constants.quarterScreenHeight/3,Constants.quarterScreenHeight/3);
//        batch.draw(level,(Constants.width/2)+(Constants.quarterScreenHeight),Constants.height/2 + Constants.height/3+Constants.quarterScreenHeight/4,Constants.quarterScreenHeight+Constants.quarterScreenHeight/2,Constants.quarterScreenHeight/3 );
        batch.draw(win,(Constants.width/2)-(Constants.quarterScreenHeight),Constants.height/2 - (2*Constants.quarterScreenHeight),2*Constants.quarterScreenHeight,Constants.quarterScreenHeight/2+Constants.quarterScreenHeight/8);
//        batch.draw(buttonBet,(Constants.width/2)-(3*Constants.quarterScreenHeight)+Constants.quarterScreenHeight/5,Constants.height/2 - (2*Constants.quarterScreenHeight),2*Constants.quarterScreenHeight,Constants.quarterScreenHeight/2+Constants.quarterScreenHeight/8);
        batch.draw(buttonBet1,(Constants.width/2)-(3*Constants.quarterScreenHeight)+Constants.quarterScreenHeight/5,Constants.height/2 - (2*Constants.quarterScreenHeight),2*Constants.quarterScreenHeight,Constants.quarterScreenHeight/2+Constants.quarterScreenHeight/8);

    }

    public void dispose(){
        bottomToolBar.dispose();
        topToolBar.dispose();
        bankField.dispose();
        money.dispose();
//        level.dispose();
        win.dispose();
        buttonBet.dispose();
        buttonBet1.dispose();
    }

    public Texture getButtonBet() {
        return buttonBet;
    }
}
