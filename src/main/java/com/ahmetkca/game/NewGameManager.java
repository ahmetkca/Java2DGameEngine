package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.engine.gfx.Image;
import com.ahmetkca.engine.gfx.Light;
import com.ahmetkca.utils.MyRandomGenerator;
import com.ahmetkca.utils.WordBank;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NewGameManager extends Game {

    private final int MAX_WORDS_IN_SCREEN = 6;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final HashMap<String, FlyingWord> uniqueWords;
    Light light = new Light(50, 0xffffffff);
    Image background = new Image("/background.png");
    private String inputWord = "";
    private final WordBank wordBank;

    public NewGameManager() {
        uniqueWords = new HashMap<>();
        wordBank = new WordBank();
//        gameObjects.add(new Player(64,64, 16, 16));
        Set<String> tempUniqueWords = wordBank.getRandomUniqueWords(MAX_WORDS_IN_SCREEN);
        String[] uniqueWordsArray = tempUniqueWords.toArray(new String[0]);
        Set<Integer> randYSet = MyRandomGenerator.getUniqueRandomNumberSet(
                MAX_WORDS_IN_SCREEN,
                0,
                GameContainer.HEIGHT - (3*Font.DEFAULT.getFontImage().getH()));
        Integer[] randYs = randYSet.toArray(new Integer[0]);
        for (int i = 0; i < MAX_WORDS_IN_SCREEN; i++) {
            Word word = new FlyingWord(0, randYs[i]);
            String randomWord = uniqueWordsArray[i];
            uniqueWords.put(randomWord, (FlyingWord) word);
            word.setWord(randomWord);
            word.setSpeed(MyRandomGenerator.getRandomNumberInRange(15, 35));
            gameObjects.add(word);
        }
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(-1);
    }

//    private String createRandomUniqueWord() {
//        while ()
//    }

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
        if (gameObjects.size() < MAX_WORDS_IN_SCREEN)
            gameObjects.add(FlyingWord.createFlyingWord(wordBank.getRandomWord()));


    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
//        renderer.setAmbientColor(0xff131313);
//        renderer.drawImage(background, 0, 0);
//        renderer.drawLight(light, gc.getInput().getMouseX(), gc.getInput().getMouseY());
        for (int i =0;i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.render(gc, renderer);
            if (gameObject.isDead()) {
                gameObjects.remove(i);
                --i;
            }
        }
        renderer.drawText(inputWord,
                10,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH(),
                0xffffffff);
    }

    @Override
    public void updateObs() {
        int chr = subject.getState();
        if (chr == KeyEvent.VK_ENTER) {
            if (uniqueWords.containsKey(inputWord)) {
                uniqueWords.get(inputWord).setDead(true);
            }
            inputWord = "";
            //TODO check if the input-word is one of the words on the screen
        } else if (chr == KeyEvent.VK_BACK_SPACE) {
            //TODO remove the last character from input-word
            if (inputWord.length() != 0)
                inputWord = inputWord.substring(0, inputWord.length()-1);
        } else if ((chr >= KeyEvent.VK_A && chr <= KeyEvent.VK_Z) || (chr >= 97 && chr <= 122)) {
            inputWord = inputWord + (char) chr;
        }
//        System.out.println(chr);
    }
}
