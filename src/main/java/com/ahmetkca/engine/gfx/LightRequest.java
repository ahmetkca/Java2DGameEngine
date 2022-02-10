package com.ahmetkca.engine.gfx;

public class LightRequest {
    private Light light;
    private int x, y;
    public LightRequest(Light light, int x, int y) {
        this.light = light;
        this.x = x;
        this.y = y;
    }
    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
