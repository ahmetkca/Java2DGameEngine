package com.ahmetkca.game;

import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.engine.gfx.Image;

public abstract class Word extends GameObject {
    public static final Font DEFAULT_FONT = Font.DEFAULT;

    private Image wordImage;

    protected String word;
    protected float speed = 55f;
    protected int color = 0xff00ff00;

    //TODO: word is not font independent
    // change how you draw text by creating FontFactory or FontBuilder
    // and passing the Font to use it for rendering
    protected int calculateWidth(String word) {
        int width = 0;
        for (int i = 0; i < word.length(); i++) {
            int unicode = word.charAt(i);
            width += Word.DEFAULT_FONT.getWidths()[unicode];
        }
        return width;
    }

    public String getWord() {
        return word;
    }

    //TODO: it is dependent on DEFAULT_FONT
    public Word setWord(String word) {
        this.word = word;
        setW(calculateWidth(word));
        setH(DEFAULT_FONT.getFontImage().getH());
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
