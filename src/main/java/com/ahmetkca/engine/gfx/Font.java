package com.ahmetkca.engine.gfx;

public class Font {
    public static final int UNICODE_OFFSET = 0;
    public static final Font DEFAULT = new Font("/fonts/arial_11.png");

    private Image fontImage;
    private int[] offsets;
    private int[] widths;
    public Font(String path) {
        fontImage = new Image(path);

        offsets = new int[256];
        widths = new int[256];

        int unicode = 0;

        for (int i = 0; i < fontImage.getW(); i++) {
            if (fontImage.getP()[i] == 0xff0000ff)
                offsets[unicode] = i;

            if (fontImage.getP()[i] == 0xffffff00) {
                widths[unicode] = i - offsets[unicode];
                unicode++;
            }
        }
    }

    public int[] getImageOfSpecificCharacter(int unicode) {
        int[] charImage = new int[widths[unicode] * fontImage.getH()];
        for (int x = 0; x < widths[unicode]; x++) {
            for (int y = 1; y < getFontImage().getH(); y++) {
                charImage[x + y * widths[unicode]] = fontImage.getP()[(offsets[unicode] + x) + (y * fontImage.getW())];
            }
        }
        return charImage;
    }

    public int[] getImageOfSpecificCharacter(int unicode, int color) {
        int[] charImage = new int[widths[unicode] * fontImage.getH()];
        for (int x = 0; x < widths[unicode]; x++) {
            for (int y = 1; y < getFontImage().getH(); y++) {
                if (fontImage.getP()[(offsets[unicode] + x) + (y * fontImage.getW())] == 0xffffffff)
                    charImage[x + y * widths[unicode]] = color;
                else
                    charImage[x + y * widths[unicode]] = fontImage.getP()[(offsets[unicode] + x) + (y * fontImage.getW())];
            }
        }
        return charImage;
    }

    public Image getFontImage() {
        return fontImage;
    }

    public void setFontImage(Image fontImage) {
        this.fontImage = fontImage;
    }

    public int[] getOffsets() {
        return offsets;
    }

    public void setOffsets(int[] offsets) {
        this.offsets = offsets;
    }

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }
}
