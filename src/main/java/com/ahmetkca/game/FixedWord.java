package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.engine.gfx.Light;

public class FixedWord extends Word{

    public FixedWord(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
//        this.w = 0;
//        this.h = 0;
    }
    public FixedWord(float posX, float posY, int color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
//        this.w = 0;
//        this.h = 0;
    }
    public FixedWord(String word, float posX, float posY, int color) {
        this.word = word;
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.w = calculateWidth(word);
        this.h= Font.DEFAULT.getFontImage().getH();
    }


    @Override
    public void update(GameContainer gc, float dt) {}

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.drawText(word, (int) posX, (int) posY, color);
    }
}
