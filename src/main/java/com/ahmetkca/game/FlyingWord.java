package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.engine.gfx.Light;
import com.ahmetkca.utils.MyRandomGenerator;

public class FlyingWord extends Word {
    public static final int CHAR_WEIGHT = 25;
    private final Light light;
    private int points;

    private boolean isCorrectlyEntered = false;

    public FlyingWord(float posX, float posY) {
        int randomLightColor = (255 << 24) | MyRandomGenerator.getRandomNumberInRange(0,(255 << 16) | (255 << 8) | 255);
        light = new Light(50, randomLightColor);
        this.posX=posX;
        this.posY=posY;
        this.w=0;
        this.h=0;
    }

    public static FlyingWord createFlyingWord(String word) {
        return (FlyingWord) new FlyingWord(0, MyRandomGenerator.getRandomNumberInRange(0, GameContainer.HEIGHT - (3* Font.DEFAULT.getFontImage().getH())))
                .setWord(word);
    }

    @Override
    public Word setWord(String word) {
        super.setWord(word);
        points = CHAR_WEIGHT * word.length();
        light.setRadius((int) (getW() + (getW()/4.0)));
        return this;
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (posX > GameContainer.WIDTH)
            this.setDead(true);
        changeColor((int) posX, dt);
        posX += (dt*speed);
    }
    private void changeColor(int postX, float dt) {
        if (postX >= 0 && postX <= ((GameContainer.WIDTH/2.0) - (getW()/2.0))) {
            color = 0xff00ff00 | ((((int)((posX*255.0)/((GameContainer.WIDTH/2.0f) - (getW()/2.0))))) << 16) ;
        } else if (posX > ((GameContainer.WIDTH/2.0f)- (getW()/2.0)) && posX <= ((GameContainer.WIDTH) - (getW()/2.0))) {
            int g = (255-Math.abs( 255-((int)((posX*255.0)/((GameContainer.WIDTH/2.0f)- (getW()/2.0))))));
            if (g < 0) {
                g = 0;
            }
            color =(255 << 24) | (255 << 16) | (g << 8);
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.drawText(word, (int) posX, (int) posY, color);
        renderer.drawLight(light, (int) (posX + (getW()/2.0)), (int) (posY + (getH()/2)));
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isCorrectlyEntered() {
        return isCorrectlyEntered;
    }

    public void setCorrectlyEntered(boolean correctlyEntered) {
        isCorrectlyEntered = correctlyEntered;
    }
}
