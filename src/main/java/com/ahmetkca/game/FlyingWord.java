package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;

public class FlyingWord extends Word {


    public FlyingWord(float posX, float posY) {
        this.posX=posX;
        this.posY=posY;
        this.w=0;
        this.h=0;
    }

    @Override
    public void update(GameContainer gc, float dt) {
        changeColor((int) posX, dt);
        posX += (dt*speed);
    }
    private void changeColor(int postX, float dt) {
        System.out.println(((GameContainer.WIDTH/2.0f)- (getW()/2.0)) + " " + posX + " " + ((GameContainer.WIDTH) - (getW()/2.0)));
        if (postX >= 0 && postX <= ((GameContainer.WIDTH/2.0) - (getW()/2.0))) {
            color = 0xff00ff00 | ((((int)((posX*255.0)/((GameContainer.WIDTH/2.0f) - (getW()/2.0))))) << 16) ;
        } else if (posX > ((GameContainer.WIDTH/2.0f)- (getW()/2.0)) && posX <= ((GameContainer.WIDTH) - (getW()/2.0))) {
            System.out.println("Yellow to Red");
            color =(255 << 24) | (255 << 16) | (255-Math.abs( 255-((int)((posX*255.0)/((GameContainer.WIDTH/2.0f)- (getW()/2.0)))))) << 8;
        } else {
            color = 0xffff0000;
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.drawText(word, (int) posX, (int) posY, color);
    }
}
