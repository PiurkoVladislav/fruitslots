package com.noxstudio.fruitcas.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.noxstudio.fruitcas.util.Constants;

import java.util.Random;

public class Slot {

    private Element firstElement;
    private Element secondElement;
    private Element thirdElement;
    private Vector2 firstPosition;
    private Vector2 secondPosition;
    private Vector2 thirdPosition;
    private Vector2 position;
    private int circle;
    private int count = 0;
    private int numb;
    private boolean isCircle = false;
    final Random random = new Random();
    private int tick ;


    public Slot(Vector2 position, int circle) {
        this.position = position;
        this.circle = circle;
        tick = 0;
    }

    public void init(){
        secondPosition = new Vector2(position.x,Constants.height/2 - Constants.slotElementHeight/2 );
        firstPosition = new Vector2(position.x,secondPosition.y + Constants.slotElementHeight);
        thirdPosition = new Vector2(position.x,secondPosition.y - Constants.slotElementHeight);
        secondElement = new Element(getRamdomPath(),secondPosition,new Vector2(position.x,Constants.height/2 - Constants.slotElementHeight/2));
//        firstElement = new Element(getRamdomPath(),firstPosition,new Vector2(position.x,(Constants.height/2 - Constants.slotElementHeight/2)+Constants.slotElementHeight));
        firstElement = new Element(getRamdomPath(),firstPosition,new Vector2(position.x,(secondElement.getPosition().y + Constants.slotElementHeight)));
//        thirdElement = new Element(getRamdomPath(),thirdPosition,new Vector2(position.x,(Constants.height/2 - Constants.slotElementHeight/2)-Constants.slotElementHeight));
        thirdElement = new Element(getRamdomPath(),thirdPosition,new Vector2(position.x,(secondElement.getPosition().y - Constants.slotElementHeight)));
        numb = rnd(circle-5,circle+5);
    }


    public void render(SpriteBatch batch){
        firstElement.render(batch);
        secondElement.render(batch);
        thirdElement.render(batch);
        tick++;
    }

    private String getRamdomPath(){
        return String.valueOf(random.nextInt(6) + 1);
    }

    public void dispose(){
        firstElement.dispose();
        secondElement.dispose();
        thirdElement.dispose();
    }

    private Element updateElement(Element element){
        if (element.getPosition().y < 0 ){
            element.dispose();
            count++;
            return new Element(getRamdomPath(),new Vector2(position.x,Constants.height - Constants.slotElementHeight),element.getStartPosition());
        }else
            return element;
    }

    public void move(float delta, float speed){
        if(count/3!=numb) {
            isCircle = true;
            firstElement = updateElement(firstElement);
            secondElement = updateElement(secondElement);
            thirdElement = updateElement(thirdElement);
            firstElement.update(delta, speed);
            secondElement.update(delta, speed);
            thirdElement.update(delta, speed);
        }else {
            firstElement.moveToPosition(delta,speed);
            secondElement.moveToPosition(delta,speed);
            thirdElement.moveToPosition(delta,speed);
            isCircle = false;
        }
    }

    public static int rnd(int min, int max)
    {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public Element getFirstElement() {
        return firstElement;
    }

    public Element getSecondElement() {
        return secondElement;
    }

    public Element getThirdElement() {
        return thirdElement;
    }

    public int getTick(){
        return tick+3;
    }
}
