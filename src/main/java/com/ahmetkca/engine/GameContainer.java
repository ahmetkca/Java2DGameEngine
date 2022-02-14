package com.ahmetkca.engine;

import com.ahmetkca.game.GameManager;
import com.ahmetkca.utils.Observer;
import com.ahmetkca.utils.Subject;

public class GameContainer implements Runnable{
    public static boolean DEBUG = false;

    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private final Game game;
    private boolean isRunning = false;
    private final double UPDATE_CAP = 1.0/60.0;

//    public static int WIDTH = (int)(1920f/4), HEIGHT = (int)(1080f/4);
    public static int WIDTH = 0, HEIGHT = 0;
    private static float scale = 0f;
    private String title = "AhEngine v1.0";


    public GameContainer(Game game) {
        this.game = game;
    }

    public void start() {
        window = new Window(this);

        renderer = new Renderer(this);
        input = new Input(this);
        thread = new Thread(this);
        game.setSub(input.getSubject());
        thread.start(); // this is going to be main thread
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;

        boolean render;
        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        game.init(this);

        while (isRunning) {
            render = false;

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;
                //TODO: Update game
                game.update(this, (float) UPDATE_CAP);
                input.update();
                //
                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                    System.out.println("FPS: " + fps);
                }
            }
            if (render) {
                renderer.clear();
                //TODO: Render the game here
                game.render(this, renderer);
                renderer.processAlpha();
                if (GameContainer.DEBUG)
                    renderer.drawText("Fps:" + fps, 0, 0, 0xffffffff);
                //
                window.update();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        dispose();
    }

    private void dispose() {

    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public float getScale() {
        return scale;
    }

    public static void setScale(float scale) {
        GameContainer.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public static void setWIDTH(int WIDTH) { GameContainer.WIDTH = WIDTH; }

    public static void setHEIGHT(int HEIGHT) { GameContainer.HEIGHT = HEIGHT; }

    public Renderer getRenderer() {
        return renderer;
    }

}
