package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;

import java.awt.event.KeyEvent;

public class Player extends GameObject{
    private float speed = 100f;
    public Player(int posX, int posY, int w, int h) {
        id="player";

        this.posX = posX;
        this.posY = posY;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKey(KeyEvent.VK_A)) {
            posX -= (dt * speed);
        }
        if (gc.getInput().isKey(KeyEvent.VK_D)) {
            posX += (dt * speed);
        }
        if (gc.getInput().isKey(KeyEvent.VK_W)) {
            posY -= (dt * speed);
        }
        if (gc.getInput().isKey(KeyEvent.VK_S)) {
            posY += (dt * speed);
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.fillRectangle((int)posX, (int)posY, w, h,0xff00ff00);
    }
}
