package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.audio.SoundClip;
import com.ahmetkca.engine.gfx.Image;
import com.ahmetkca.engine.gfx.ImageTile;
import com.ahmetkca.engine.gfx.Light;
import com.ahmetkca.utils.Subject;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameManager extends Game {
    private Image woody;
    private Image background;
//    private Image light;
    private Image test_me;
    private Image img;
    private Image testImage;
    private ImageTile imageTile;
    private SoundClip soundClip;
    private Image testImageAlphaBlending;
    private Image testImage2;
    private Image redRect, greenRect, blueRect;
    private Image yetAnotherTestImage;
    private Light light;

    private String testInputString = "";

    public GameManager(Subject subject) {
        this.subject = subject;
        this.subject.add(this);
    }

    public GameManager() {
        woody = new Image("/woody.png");
        background = new Image("/background.png");
//        light = new Image("/light.png");
        light = new Light(240/2, 0xffffffff);
        test_me = new Image("/opaque_blue_rect.png");
        img= new Image("/test_abc.png");
        redRect = new Image("/red_rect.png");
        greenRect = new Image("/green_rect.png");
        blueRect = new Image("/blue_rect.png");
        testImageAlphaBlending = new ImageTile("/test_alpha_blending.png", 32, 32);
        testImage2 = new Image("/test_image2.png");
        testImage = new Image("/test_image.png");
        imageTile = new ImageTile("/test_image_tile.png", 16, 16);
        soundClip = new SoundClip("/audio/test_audio.wav");
        yetAnotherTestImage = new Image("/yetAnotherTestImage.png");
        redRect.setAlpha(true);
        redRect.setLightBlock(Light.FULL);
        woody.setAlpha(false);
        woody.setLightBlock(Light.FULL);

    }
    float temp = 0;

    @Override
    public void init(GameContainer gc) {

    }

    @Override
    public void update(GameContainer gc, float dt) {
        if (gc.getInput().isKeyDown(KeyEvent.VK_A)) {
            soundClip.play();
//            System.out.println("Keyboard key A is DOWN");
        }
        if (gc.getInput().isButtonUp(MouseEvent.BUTTON1))
            System.out.println("Left mouse button is UP");
        temp += dt * 200;
//        System.out.println(temp);

        if (temp > 150) {
            light.setColor((0xff << 24) + ((int)(Math.random() * 255) << 16)
                    + ((int)(Math.random() * 255) << 8)
                    + (int)(Math.random() * 255));
            light.setupLightMap();
            temp = 0;
        }
    }


    @Override
    public void render(GameContainer gc, Renderer renderer) {
//        renderer.drawImage(light, 0, 0);
        renderer.fillRectangle(75, 75, 0,0, (((int)(0.15*255))<<24)+0xff0000);

//        renderer.setzDepth(0);
        renderer.drawImage(background, 0, 0);
        renderer.drawImage(woody,
                (gc.getWIDTH()/2) - (woody.getW()/2),
                (gc.getHEIGHT()/2) - (woody.getH()/2));

        renderer.drawImage(redRect,
                (gc.getWIDTH()/2),
                (gc.getHEIGHT()/2));
        renderer.drawLight(light,
                gc.getInput().getMouseX(),
                gc.getInput().getMouseY());
        renderer.drawText(testInputString, 25, 25, 0xffffffff);

//        renderer.fillRectangle(0, 0, 100, 100, 0xff000000);

//        renderer.drawImageTile((ImageTile) testImageAlphaBlending,
//                gc.getInput().getMouseX() - (testImageAlphaBlending.getW() / 2),
//                gc.getInput().getMouseY() - (testImageAlphaBlending.getH() / 2), 1, 1);
//        renderer.setzDepth(0);
//        renderer.drawImage(yetAnotherTestImage, 50,50);

//        renderer.setzDepth(1);
//        renderer.drawImage(greenRect, 25 + 32, 150 + 32);
//        renderer.setzDepth(2);
//        renderer.drawImage(redRect, 25, 150);
//        renderer.setzDepth(3);
//        renderer.drawImage(img , (gc.getWIDTH()/4), (gc.getHEIGHT()/4) - (blueRect.getH()/2));



//        renderer.drawImage(test_me , (gc.getWIDTH()/2), (gc.getHEIGHT()/2) - (blueRect.getH()/2));

//        renderer.drawImage(yetAnotherTestImage, 200, 50);

    }

    @Override
    public void updateObs() {
//        if (subject.getState() == KeyEvent.)
        if (subject.getState() == KeyEvent.VK_BACK_SPACE) {
            if (testInputString.length() != 0)
                testInputString = testInputString.substring(0, testInputString.length()-1);
        } else {
            testInputString += (char) subject.getState();
        }

        System.out.println("Get state: " + ((char) subject.getState()));
    }
}
