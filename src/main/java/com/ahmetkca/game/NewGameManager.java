package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.gfx.Image;
import com.ahmetkca.engine.gfx.Light;
import com.ahmetkca.utils.WordBank;

import java.util.ArrayList;

public class NewGameManager extends Game {
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    Light light = new Light(50, 0xffffffff);
    Image background = new Image("/background.png");

    public NewGameManager() {
        WordBank wordBank = new WordBank();
        gameObjects.add(new Player(64,64, 16, 16));
        gameObjects.add(new FlyingWord(0, GameContainer.HEIGHT/2f).setWord(wordBank.getRandomWord()));
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(-1);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        for (int i =0;i < gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.update(gc, dt);
            if (gameObject.isDead()) {
                gameObjects.remove(i);
                --i;
            }
        }
    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
        renderer.setAmbientColor(0xff232323);
        renderer.drawImage(background, 0, 0);
        renderer.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        for (int i =0;i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.render(gc, renderer);
            if (gameObject.isDead()) {
                gameObjects.remove(i);
                --i;
            }
        }
    }

    @Override
    public void updateObs() {
        System.out.println(subject.getState());
    }
}
