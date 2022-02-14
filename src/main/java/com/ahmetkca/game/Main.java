package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;

import java.awt.event.KeyEvent;

public class Main {

    public static void main(String[] args) {
        GameContainer.setWIDTH(320);
        GameContainer.setHEIGHT(240);
        GameContainer.setScale(3.5f);
        Game game = new NewGameManagerWithTypeSpeedGameState();
//        Game game = new TestGameManager();
        GameContainer gc = new GameContainer(game);
        gc.start();
    }
}
