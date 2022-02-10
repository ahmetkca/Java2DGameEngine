package com.ahmetkca.engine;

import static java.lang.Math.*;

import com.ahmetkca.engine.gfx.*;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collections;


public class Renderer {

    protected class NewBoundary {
        protected int offScreenX;
        protected int offScreenY;
        protected int calcNewWidth;
        protected int calcNewHeight;

        public NewBoundary(int offScreenX, int offScreenY, int calcNewWidth, int calcNewHeight) {
            this.offScreenX = offScreenX;
            this.offScreenY = offScreenY;
            this.calcNewWidth = calcNewWidth;
            this.calcNewHeight = calcNewHeight;
        }

        // check what part of the image is out of screen in negative x and y direction
        protected void calculateNewStart(int offsetX, int offsetY) {
            if (offsetX < 0) offScreenX = Math.abs(offsetX);
            if (offsetY < 0) offScreenY = Math.abs(offsetY);
        }

        // check what part of the image is out of screen in positive x and y direction
        protected void calculateNewEnd(int offsetX, int offsetY) {
            if (calcNewWidth + offsetX > pW) calcNewWidth -= calcNewWidth + offsetX - pW;
            if (calcNewHeight + offsetY > pH) calcNewHeight -= calcNewHeight + offsetY - pH;
        }
    }

    private ArrayList<ImageRequest> imageRequests = new ArrayList<>();
    private ArrayList<LightRequest> lightRequests = new ArrayList<>();

    private int pW, pH;
    private int[] pixelArray;

    // z buffering for alpha
    private int[] zBuffer;
    private int zDepth = 0;
    private boolean isProcessingAlpha = false;

    // lights
    private int[] lightMap;
    private int[] lightBlockMap;
//    private int ambientColor = 0xff232323;
    private int ambientColor = 0xff000000;


    // Default font
    private Font defaultFont = Font.DEFAULT;

    public Renderer(GameContainer gc) {
        pW = gc.getWIDTH();
        pH = gc.getHEIGHT();

        pixelArray = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
        zBuffer = new int[pixelArray.length];

        lightMap = new int[pixelArray.length];
        lightBlockMap = new int[pixelArray.length];
    }

    public void clear() {
        for (int i = 0; i < pixelArray.length; i++) {
            pixelArray[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlockMap[i] = 0;
        }
    }
//    public void clear(int color) {
//        for (int i = 0; i < pixelArray.length; i++) {
//            pixelArray[i] = color;
//            zBuffer[i] = 0;
//        }
//    }
//    public void clear(int rgb, int alpha) {
//        int color = (alpha << 24) + rgb;
//        for (int i = 0; i < pixelArray.length; i++) {
//            pixelArray[i] = color;
//            zBuffer[i] = 0;
//        }
//    }

    public void changePixelAtLightMap(int x, int y, int val) {
        if (x < 0 || x >= pW || y < 0 || y >= pH) return;
        int lightMapIndex = x+y*pW;
        int currentLightVal = lightMap[lightMapIndex];
        int maxR=Math.max((currentLightVal & 0xff0000) >> 16, (val & 0xff0000) >> 16);
        int maxG=Math.max((currentLightVal & 0xff00) >> 8, (val & 0xff00) >> 8);
        int maxB=Math.max(currentLightVal & 0xff, val & 0xff);
        lightMap[lightMapIndex] = (0xff << 24)| (maxR<<16)|(maxG<<8)|(maxB) ;
    }

    public void changePixelAtLightBlockMap(int x, int y, int val) {
        if (x < 0 || x >= pW || y < 0 || y >= pH) return;
        int lightMapIndex = x+y*pW;
        if (zBuffer[lightMapIndex] > zDepth) return;
        lightBlockMap[lightMapIndex] = val;
    }



    protected void drawLightRequest(Light light, int offsetX, int offsetY) {
        for (int i = 0 ; i <= light.getDiameter(); i++) {
            // divide it into 90 degree angles
            drawLightLine(light, light.getRadius(), light.getRadius() // move across to top of the light
                    , i, 0, offsetX, offsetY);
            drawLightLine(light, light.getRadius(), light.getRadius() // move across the bottom of the light
                    , i, light.getDiameter(), offsetX, offsetY);
            drawLightLine(light, light.getRadius(), light.getRadius() // move across to left side of the light
                    , 0, i, offsetX, offsetY);
            drawLightLine(light, light.getRadius(), light.getRadius() // move across to right side of the light
                    , light.getDiameter(), i, offsetX, offsetY);
        }
    }

    public void drawLight(Light light, int offsetX, int offsetY) {
        lightRequests.add(new LightRequest(light, offsetX, offsetY));
    }

    private void drawLightLine(Light light, int x0, int y0, int x1, int y1, int offsetX, int offsetY) {
        //brenhemn's line drawing algorithm without floating point computation
        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);

        //determine the quadrant (first quadrant, second third etc.)
        //
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int e = dx - dy;
        int e2;
        while (true) {
            int screenX =x0 - light.getRadius() + offsetX;
            int screenY = y0 - light.getRadius() + offsetY;

            // check for out of bounds
            if (screenX < 0 || screenX >= pW || screenY < 0 || screenY >= pH)
                return;

            int lightVal = light.getLightValue(x0, y0);
            if (lightVal == 0)
                return;

            if (lightBlockMap[screenX + screenY * pW] == Light.FULL) {
                return;
            }

            changePixelAtLightMap(screenX,
                    screenY,
                    lightVal);

            // stop the while loop since we hit the location we are trying to go
            if (x0 == x1 && y0== y1)
                break;
            e2 = 2 * e;
            if (e2>-1 * dy) {
                // move on the x-axis
                e -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                // move on the y-axis
                e += dx;
                y0 += sy;
            }
        }

    }

    public void processAlpha() {
        isProcessingAlpha = true;
        Collections.sort(imageRequests, (o1, o2) -> {
            if (o1.getzDepth() > o2.getzDepth()) {
                return 1;
            } else if (o1.getzDepth() < o2.getzDepth()) {
                return -1;
            } else {
                return 0;
            }
        });
        for (int i=0; i< imageRequests.size(); i++) {
            setzDepth(imageRequests.get(i).getzDepth());
            drawImage(imageRequests.get(i).getImage(),
                    imageRequests.get(i).getOffsetX(),
                    imageRequests.get(i).getOffsetY());
        }

        //TODO: Draw the lights here
        for (int i = 0; i < lightRequests.size(); i++) {
            LightRequest lightRequest = lightRequests.get(i);
            drawLightRequest(lightRequest.getLight(),
                    lightRequest.getX(),
                    lightRequest.getY());
        }

        // merge original Pixel Array with Light Map array
        for (int i = 0; i < pixelArray.length; i++) {
//            System.out.println(i);
            // get the rgb values in range 0.0 to 1.0
            float r=((lightMap[i] >> 16) & 0xff) / 255.0f;
            float g=((lightMap[i] >> 8) & 0xff) / 255.0f;
            float b=(lightMap[i] & 0xff) / 255.0f;

            pixelArray[i] = (0xff << 24) | (int) (((pixelArray[i] >> 16) & 0xff) * r) << 16 | (int) (((pixelArray[i] >> 8) & 0xff) * g) << 8 | (int)((pixelArray[i] & 0xff) * b);
        }

        imageRequests.clear();
        lightRequests.clear();
        isProcessingAlpha = false;
    }

    public void setPixel(int x, int y, int colorVal) {
        // colorVal is an RGB value
        // An int has 32 bits, 3x8 = 24 is used to store the RGB components (8 bits for each) in the following
        //                       if there is alpha value then it is equals to last 8 bit
        //            3          2          1          0
        // bitpos    10987654 32109876 54321098 76543210
        // ------   +--------+--------+--------+--------+
        // bits     |AAAAAAAA|RRRRRRRR|GGGGGGGG|BBBBBBBB|

        // So for example in order to get alpha value make first 24 bit 0 by using bitwise AND
        // then shift 24 bit to the right.

        int colorValAlphaVal = (colorVal >> 24) & 0xff;
        // check if pixel is out of bound or its transparent
        if ((x < 0 || x >= pW || y < 0 || y >= pH) || colorValAlphaVal == 0) return;

        int twoDto1dIndex = x + y * pW;
        // check if the current z-buffer for given pixel coordinate is greater than current z-depth
        if (zBuffer[twoDto1dIndex] > zDepth) return;

        zBuffer[twoDto1dIndex] = zDepth;

        // if the given color value is opaque which is 255 alpha value
        // then replace the oldPixel color value with given color value
        if (colorValAlphaVal == 255) {
            pixelArray[twoDto1dIndex] = colorVal;
        } else {
            // if the alpha value of given color is not 255, or it is partially transparent
            // then alpha blend the old pixel color value with new given color value
            int currentPixelVal = pixelArray[twoDto1dIndex];
            int currentPixelValRed      =   (currentPixelVal & 0xff0000 ) >> 16;
            int currentPixelValGreen    =   (currentPixelVal & 0xff00) >> 8;
            int currentPixelValBlue     =   (currentPixelVal & 0xff);

            int colorValRed     =   (colorVal & 0xff0000) >> 16;
            int colorValGreen   =   (colorVal & 0xff00) >> 8;
            int colorValBlue    =   colorVal & 0xff;

            int newRed  = currentPixelValRed
                    - (int) ((currentPixelValRed - colorValRed) * (colorValAlphaVal / 255.0f));
            int newGreen= currentPixelValGreen
                    - (int) ((currentPixelValGreen - colorValGreen) * (colorValAlphaVal / 255.0f));
            int newBlue = currentPixelValBlue
                    - (int) ((currentPixelValBlue - colorValBlue) * (colorValAlphaVal / 255.0f));

            // blue is first 8-bit
            // green is second 8-bit
            // red is third 8-bit
            // alpha if there is, last 8 bit of 32-bit integer
//            int newRed = ((currentPixelVal >> 16) & 0xff)  - (int)((((currentPixelVal >> 16) & 0xff) - ((colorVal >> 16) & 0xff) * (colorValAlphaVal / 255f)));
//            int newGreen = ((currentPixelVal >> 8) & 0xff)  - (int)((((currentPixelVal >> 8) & 0xff) - ((colorVal >> 8) & 0xff) * (colorValAlphaVal / 255f)));
//            int newBlue = (currentPixelVal& 0xff)  - (int)(((currentPixelVal >> 0& 0xff) - ((colorVal >> 0) & 0xff) * (colorValAlphaVal / 255f)));
            pixelArray[twoDto1dIndex] = (255 << 24) + (newRed << 16) + (newGreen << 8) + newBlue;
//            p[x + y * pW] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);

        }

    }

    public void drawImage(Image image, int offsetX, int offsetY) {
        if (image.isAlpha() && !isProcessingAlpha) {
            imageRequests.add(new ImageRequest(image, zDepth, offsetX, offsetY));
            return;
        }

        NewBoundary newBoundary = new NewBoundary(0, 0, image.getW(), image.getH());
        newBoundary.calculateNewStart(offsetX, offsetY);
        newBoundary.calculateNewEnd(offsetX, offsetY);
        for (int y = newBoundary.offScreenY; y < newBoundary.calcNewHeight; y++) {
            for (int x = newBoundary.offScreenX; x < newBoundary.calcNewWidth; x++) {
                setPixel(x + offsetX,y + offsetY, image.getP()[x + y * image.getW()]);
                changePixelAtLightBlockMap(x + offsetX, y + offsetY, image.getLightBlock());
            }
        }
    }

    public void drawImageTile(ImageTile imageTile,
                              int offsetX, int offsetY,
                              int tileX, int tileY) {
        Image tileFromImageTile = new Image(imageTile.getSpecificTilePixelArray(tileX, tileY),
                imageTile.getTileW(), imageTile.getTileH());
        tileFromImageTile.setAlpha(imageTile.isAlpha());
        drawImage(tileFromImageTile, offsetX, offsetY);
    }

    public void drawText(String text, int offsetX, int offsetY, int color) {
//        text = text.toUpperCase(Locale.ROOT);
        int charOffset = 0;
        for (int i = 0; i < text.length(); i++) {
            int unicode = text.charAt(i) - Font.UNICODE_OFFSET;
            // TODO: use drawImage member function to draw each character from text and also apply offset so they don't render on top of each other
            drawImage(new Image(defaultFont.getImageOfSpecificCharacter(unicode, color), defaultFont.getWidths()[unicode], defaultFont.getFontImage().getH()), offsetX + charOffset, offsetY);
            charOffset += defaultFont.getWidths()[unicode];
        }
    }

    public void drawRectangle(int offsetX, int offsetY, int width, int height, int color) {
        for (int x = 0; x <= width; x++) {
            setPixel(x + offsetX, offsetY, color);
            setPixel(x + offsetX, offsetY + width, color);
        }
        for (int y = 1; y <= height - 1; y++) {
            setPixel(offsetX, y + offsetY, color);
            setPixel(offsetX + width, y + offsetY, color);
        }
    }

    public void fillRectangle(int offsetX, int offsetY, int width, int height, int color) {
        NewBoundary newBoundary = new NewBoundary(0, 0, width, height);
        newBoundary.calculateNewStart(offsetX, offsetY);
        newBoundary.calculateNewEnd(offsetX, offsetY);

        for (int y = newBoundary.offScreenY; y < newBoundary.calcNewHeight; y++) {
            for (int x = newBoundary.offScreenX; x < newBoundary.calcNewWidth; x++) {
                setPixel(x + offsetX, y + offsetY, color);
            }
        }
    }

//    private int integerPart(float x) {return (int)x;}
//    private float fractionalPart(float x) {
//        if (x>0) return x - integerPart(x);
//        else return x - (integerPart(x) + 1);
//    }
//    private int roundNumber(float x) {return integerPart(x + 0.5f);}
//    private float rfPart(float x) {return 1 - fractionalPart(x);}
//    protected int brightnessToAlpha(float c) {
//        return (int) (c*255.0f);
//    }
//
//    public void drawLine(int x0, int y0, int x1, int y1, int color) {
////        float m = ((float) y2-y1)/((float) x2-x1);
////        float b = y1 - (m * x1);
////        for (int x = x1; x < x2; x++) {
////            setPixel(x, Math.round(m*x+b), color);
//        boolean steep = Math.abs(y1-y0) > Math.abs(x1-x0);
//        // swap the co-ordinates if slope > 1 or draw backwards
//        if (steep) {
//            x0 = x0 ^ y0 ^ (y0 = x0);
//            x1 = x1 ^ y1 ^ (y1 = x1);
//        }
//        if (x0 > x1) {
//            x0 = x0 ^ x1 ^ (x1 = x0);
//            y0 = y0 ^ y1 ^ (y1 = y0);
//        }
//        // compute slope
//        float dx = x1 - x0;
//        float dy = y1 - y0;
//        float gradient = dy/dx;
//        if (dx == 0.0f)
//            gradient=1.0f;
//
//        int xpxl1 = x0;
//        int xpxl2 = x1;
//        float intersectY = y0;
//        //main loop
//        if (steep) {
//
//            for (int x= xpxl1; x <= xpxl2; x++) {
//                setPixel(integerPart(intersectY), x,
//                        (int)(rfPart(intersectY)*255)<<24);
//                setPixel(integerPart(intersectY)-1, x,
//                        (int)(fractionalPart(intersectY)*255)<<24);
//                intersectY += gradient;
//            }
//        } else {
//            for (int x=xpxl1;x<=xpxl2;x++){
//                setPixel(x, integerPart(intersectY),
//                        (int)(rfPart(intersectY)*255)<<24);
//                setPixel(x, integerPart(intersectY)-1,
//                        (int)(fractionalPart(intersectY)*255)<<24);
//                System.out.printf("%f %f %f\n", intersectY, rfPart(intersectY), fractionalPart(intersectY));
//                intersectY += gradient;
//            }
//        }
//    }

    int ipart(double x) {
        return (int) x;
    }

    double fpart(double x) {
        return x - floor(x);
    }

    double rfpart(double x) {
        return 1.0 - fpart(x);
    }

    public void drawLine(double x0, double y0, double x1, double y1, int color) {

        boolean steep = abs(y1 - y0) > abs(x1 - x0);
        if (steep)
            drawLine(y0, x0, y1, x1, color);

        if (x0 > x1)
            drawLine(x1, y1, x0, y0, color);

        double dx = x1 - x0;
        double dy = y1 - y0;
        double gradient = dy / dx;

        // handle first endpoint
        double xend = round(x0);
        double yend = y0 + gradient * (xend - x0);
        double xgap = rfpart(x0 + 0.5);
        double xpxl1 = xend; // this will be used in the main loop
        double ypxl1 = ipart(yend);

        if (steep) {
            setPixel((int) ypxl1, (int) xpxl1, (((int)(rfpart(yend) * xgap))<<24)|color);
            setPixel((int) (ypxl1 + 1), (int) xpxl1, (((int)(fpart(yend) * xgap))<<24)|color);
        } else {
            setPixel((int) xpxl1, (int) ypxl1, (((int)(rfpart(yend) * xgap))<<24)|color);
            setPixel((int) xpxl1, (int) (ypxl1 + 1), (((int)(fpart(yend) * xgap))<<24)|color);
        }

        // first y-intersection for the main loop
        double intery = yend + gradient;

        // handle second endpoint
        xend = round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5);
        double xpxl2 = xend; // this will be used in the main loop
        double ypxl2 = ipart(yend);

        if (steep) {
            setPixel((int) ypxl2, (int) xpxl2, (((int)(rfpart(yend) * xgap))<<24)|color);
            setPixel((int) (ypxl2 + 1), (int) xpxl2, (((int)(fpart(yend) * xgap))<<24)|color);
        } else {
            setPixel((int) xpxl2, (int) ypxl2, (((int)(rfpart(yend) * xgap))<<24)|color);
            setPixel((int) xpxl2, (int)(ypxl2 + 1), (((int)(fpart(yend) * xgap))<<24)|color);
        }

        // main loop
        for (double x = xpxl1 + 1; x <= xpxl2 - 1; x++) {
            if (steep) {
                setPixel(ipart(intery), (int) x, ((int)(rfpart(intery)*255)<<24)|color);
                setPixel(ipart(intery) + 1, (int) x, ((int)(fpart(intery)*255)<<24)|color);
            } else {
                setPixel((int) x, ipart(intery), (int)(rfpart(intery)*255)<<24|color);
                setPixel((int) x, ipart(intery) + 1, ((int)(fpart(intery)*255)<<24)|color);
            }
            intery = intery + gradient;
        }
    }

    public void drawLineAliased(int x1, int y1, int x2, int y2) {
        // delta of exact value and rounded value of the dependent variable
        int d = 0;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;

        int x = x1;
        int y = y1;

        if (dx >= dy) {
            while (true) {
                setPixel(x, y, 0xff000000);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                setPixel(x, y, 0xff000000);;
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }
}
