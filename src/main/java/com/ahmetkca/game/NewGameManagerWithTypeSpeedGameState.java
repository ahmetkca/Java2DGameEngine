package com.ahmetkca.game;

import com.ahmetkca.engine.Game;
import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.Renderer;
import com.ahmetkca.engine.audio.SoundClip;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.engine.gfx.Image;
import com.ahmetkca.engine.gfx.Light;
import com.ahmetkca.utils.MyRandomGenerator;
import com.ahmetkca.utils.WordBankAPI;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NewGameManagerWithTypeSpeedGameState extends Game {

    private final TypeSpeedGameState typeSpeedGameState;

    private final List<SoundClip> scorePointsSoundEffects = new ArrayList<>();

    private float totalTime = 1*60;
    private final SoundClip oofSoundEffect;
    private final SoundClip letsGoSoundClip = null;
    private final SoundClip scoringPoint = null;
    private final int MAX_WORDS_IN_SCREEN = 6;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final HashMap<String, FlyingWord> uniqueWords;
    Light light = new Light(50, 0xffffffff);
    Image background = new Image("/background.png");
    private String inputWord = "";
    private final WordBankAPI wordBankAPI;
    private int playersScore = 500;
    private String lastCorrectlyEnteredWord = "";

    public NewGameManagerWithTypeSpeedGameState() {
        System.out.println(Font.DEFAULT.getFontImage().getH());
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_1.wav"));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_2.wav"));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_3.wav"));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_4.wav"));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_5.wav"));
        oofSoundEffect = new SoundClip("/audio/minecraft_oof_sound_effect.wav");
//        letsGoSoundClip = new SoundClip("/audio/dabby_lets_go.wav");
//        letsGoSoundClip.setVolume(0.5f);
//        scoringPoint = new SoundClip("/audio/scoring_point.wav");
//        scoringPoint.setVolume(0.5f);
        uniqueWords = new HashMap<>();
        wordBankAPI = new WordBankAPI();
        typeSpeedGameState = new TypeSpeedGameState(wordBankAPI);
        for (int i = 0; i < typeSpeedGameState.getMAX_WORDS(); i++) {
            FlyingWord flyingWord = new FlyingWord(
                    typeSpeedGameState.getUniqueWords().get(i),
                    0,
                    typeSpeedGameState.getUniqueYPositions().get(i));
            flyingWord.setSpeed(MyRandomGenerator.getRandomNumberInRange(15, 55));
            gameObjects.add(flyingWord);
            uniqueWords.put(typeSpeedGameState.getUniqueWords().get(i), flyingWord);
        }
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(-1);
    }

    @Override
    public void update(GameContainer gc, float dt) {
        totalTime -= dt;
        if (playersScore < 0)
            gc.stop();
        for (int i =0;i < gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.update(gc, dt);
            if (gameObject.isDead()) {
                if (!((FlyingWord) gameObject).isCorrectlyEntered()) {
                    playersScore -= ((FlyingWord) gameObject).getPoints() * (1.0 - (1.0 / (uniqueWords.get(lastCorrectlyEnteredWord).getSpeed())));
//                    playersScore -= 50;
//                    if (!oofSoundEffect.isRunning())
                    oofSoundEffect.play();
                } else {
                    playersScore += uniqueWords.get(lastCorrectlyEnteredWord).getPoints() * (1.0- (1.0 / (uniqueWords.get(lastCorrectlyEnteredWord).getSpeed())));

//                    if (!sc.isRunning())
                    scorePointsSoundEffects.get(MyRandomGenerator.getRandomNumberInRange(0, scorePointsSoundEffects.size()-1)).play();
//                    if (!scoringPoint.isRunning())
//                        scoringPoint.play();
                }
                typeSpeedGameState.removeWord(((FlyingWord) gameObject).getWord());
                typeSpeedGameState.removeYpos((int) gameObject.getPosY());
                gameObjects.remove(i);
                --i;
            }
        }
        if (gameObjects.size() < MAX_WORDS_IN_SCREEN) {
            String uw = typeSpeedGameState.addUniqueWord();
            int uyp = typeSpeedGameState.addUniqueYpos();

            FlyingWord flyingWord = new FlyingWord(uw, 0, uyp);
            gameObjects.add(flyingWord);
            uniqueWords.put(flyingWord.getWord(), flyingWord);
        }


    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
//        renderer.setAmbientColor(0xff353535);
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
                (int) (GameContainer.WIDTH/2.0)-50,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH(),
                0xffffffff);
        renderer.drawText("Score: " + playersScore,
                10,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH(),
                0xffffffff);

        renderer.drawText(String.format("%02d:%02d", ((int)totalTime) / 60, ((int)totalTime) % 60),
                (int) (GameContainer.WIDTH/2.0)+75,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH(),
                0xffffffff);
    }

    @Override
    public void updateObs() {
        int chr = subject.getState();
        if (chr == KeyEvent.VK_ENTER) {
            if (uniqueWords.containsKey(inputWord)) {
                uniqueWords.get(inputWord).setCorrectlyEntered(true);
                uniqueWords.get(inputWord).setDead(true);
                lastCorrectlyEnteredWord = "" + inputWord;
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
    }
}
