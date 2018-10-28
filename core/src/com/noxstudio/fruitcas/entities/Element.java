package com.noxstudio.fruitcas.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.noxstudio.fruitcas.util.Constants;

public class Element {


    private String name;

    private Vector2 position;
    private Vector2 startPosition;
    private Texture image;


    public Element (String path, Vector2 position, Vector2 startPosition) {
        this.name = path;
        this.position = position;
        this.startPosition = startPosition;
        image = new Texture(Gdx.files.internal("data/"+path+".png"));
    }

    public void render(SpriteBatch batch){
        batch.draw(image,position.x,position.y, Constants.slotElementWidth,Constants.slotElementHeight);
    }

    public void dispose(){
        image.dispose();
    }

    public String getName() {
        return name;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public void update(float delta, float speed) {
        position.y -= delta * speed;
    }

    public void moveToPosition(float delta,float speed){
        if(position.y > startPosition.y){
            position.y -= delta * speed;
        }
    }

}
