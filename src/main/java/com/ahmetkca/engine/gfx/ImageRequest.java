package com.ahmetkca.engine.gfx;

public class ImageRequest {
    private Image image;
    private int zDepth;
    private int offsetX, offsetY;
    public ImageRequest(Image image, int zDepth, int offsetX, int offsetY) {
        this.image = image;
        this.zDepth = zDepth;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
