package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;

public class FixedWord extends Word{

    public FixedWord(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }
    public FixedWord(float posX, float posY, int color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
    }
    public FixedWord(String word, float posX, float posY, int color) {
        this.word = word;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.w = calculateWidth(word);
    }


    @Override
    public void update(GameContainer gc, float dt) {}

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.drawText(word, (int) posX, (int) posY, color);
    }
}
