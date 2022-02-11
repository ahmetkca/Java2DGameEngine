package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;

import java.awt.event.KeyEvent;

public class Main {

    public static void main(String[] args) {
        Game game = new NewGameManager();
        GameContainer gc = new GameContainer(game);
        gc.setWIDTH(320);
        gc.setHEIGHT(240);
        gc.setScale(3f);
        gc.start();
    }
}
