package com.noxstudio.fruitcas.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.noxstudio.fruitcas.util.Constants;




public class Line {

    private Vector2 positionStart;
    private Vector2 positionEnd;
    private Vector2 postionY;
//    private Texture image;
    ShapeRenderer mRectangle;

    public Line() {
    }

    public void init(Vector2 positionStart, Vector2 positionEnd){
        this.positionStart = positionStart;
        this.positionEnd = positionEnd;
        postionY = new Vector2(0,Constants.height);
        mRectangle = new ShapeRenderer();
//        image = new Texture(Gdx.files.internal("data/11.png"));
    }

    public void render(SpriteBatch batch){
        batch.end();
        mRectangle.begin(ShapeRenderer.ShapeType.Filled);
        mRectangle.setColor(Color.RED);
        mRectangle.rect(positionStart.x,positionStart.y+Constants.slotElementHeight/4, positionEnd.x-positionStart.x,Constants.slotElementHeight/8);
        mRectangle.end();
        batch.begin();
//        batch.draw(image,positionStart.x,positionStart.y, positionEnd.x-positionStart.x,Constants.slotElementHeight/4);
    }

//    public void moveToPosition(float delta,float speed){
//        if(postionY.y > positionStart.y){
//            postionY.y -= delta * speed;
//        }
//    }

    public void dispose(){
//        image.dispose();
        mRectangle.dispose();
    }

//    public boolean onTheEnd(){
//        if(postionY.y <= positionStart.y){
//            return true;
//        }else{
//            return false;
//        }
//    }
}
