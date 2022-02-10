package com.ahmetkca.engine.gfx;

public class Light {
    public static final int NONE = 0;
    public static final int FULL = 1;

    private int radius, diameter, color;
    private int[] lightMap;

    public Light(int radius, int color) {
        System.out.println("Light");
        this.radius = radius;
        this.color = color;
        diameter = radius * 2;
        lightMap = new int[diameter * diameter];


        for (int y = 0; y < diameter; y++) {
            for (int x= 0; x < diameter; x++) {
                // distance from center of the circle
                double dist = Math.sqrt((x - radius)*(x-radius)+(y-radius)*(y-radius));
                // is it in the circle
                if (dist < radius) {
                    double p = 1 - (dist / radius);
                    this.lightMap[x+y*diameter] = (0xff << 24) | (int) (((color >> 16) & 0xff) * p) << 16 | (int) (((color >> 8) & 0xff) * p) << 8 | (int)((color & 0xff) * p);
                } else {
                    this.lightMap[x+y*diameter] = 0;
                }
            }
        }
    }
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getLightMap() {
        return lightMap;
    }

    public void setLightMap(int[] lightMap) {
        this.lightMap = lightMap;
    }

    public int getLightValue(int x, int y) {
        if (x < 0 || x >= diameter || y < 0 || y >= diameter)
            return 0xff000000;
        return lightMap[x + y * diameter];
    }
}
