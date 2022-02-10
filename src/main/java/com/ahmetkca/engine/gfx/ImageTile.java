package com.ahmetkca.engine.gfx;

public class ImageTile extends  Image{
    private int tileW, tileH;

    public ImageTile(String path, int tileW, int tileH) {
        super(path);
        this.tileH = tileH;
        this.tileW = tileW;
    }

    public int getTileW() {
        return tileW;
    }

    public void setTileW(int tileW) {
        this.tileW = tileW;
    }

    public int getTileH() {
        return tileH;
    }

    public void setTileH(int tileH) {
        this.tileH = tileH;
    }

    public Image getSpecificTileImage(int tileX, int tileY) {
        int[] p = getSpecificTilePixelArray(tileX, tileY);
        return new Image(p, tileW, tileH);
    }

    public int[] getSpecificTilePixelArray(int tileX, int tileY) {
        int[] pixelArray = new int[tileW * tileH];
        for (int x = 0; x < tileW; x++)
            for (int y = 0; y < tileH; y++)
                pixelArray[x + y * tileW] = getP()[(y * this.getW()) + x + (this.getW() * tileY * tileH) + (tileX * tileW)];
        return pixelArray;
//        int beg = (this.getW() * tileY * tileH) + (tileX * tileW);
//        for (int y = 0; y < tileH; y++) {
//            for (int x = 0; x < tileW; x++) {
//                tile[x + y * tileW] = getP()[]
//            }
//        }
//        return Arrays.copyOfRange(getP(), )
    }
}
