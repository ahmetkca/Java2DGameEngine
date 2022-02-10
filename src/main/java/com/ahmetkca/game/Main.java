package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;

public class Main {

    public static void main(String[] args) {
        Game game = new GameManager();
        GameContainer gc = new GameContainer(game);
        gc.setWIDTH(320);
        gc.setHEIGHT(240);
        gc.setScale(3f);
        gc.start();
    }
}
