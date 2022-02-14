package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;

public abstract class GameObject {
    protected String id;
    protected float posX;
    protected float posY;
    protected int w, h;
    protected boolean dead = false;

    public abstract void update(GameContainer gc, float dt);
    public abstract void render(GameContainer gc, Renderer renderer);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPosX() {
        return posX;
    }

    public GameObject setPosX(float posX) {
        this.posX = posX;
        return this;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
