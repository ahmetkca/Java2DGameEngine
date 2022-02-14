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
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final HashMap<String, FlyingWord> uniqueWords;
    Light light = new Light(50, 0xffffffff);
    Image background = new Image("/background_2.png");
    private final WordBankAPI wordBankAPI;
    private int playersScore = 500;
    private String lastCorrectlyEnteredWord = "";
    private final FixedWord inputBox;
    private final FixedWord scoreBox;
    private final FixedWord timeBox;

    public NewGameManagerWithTypeSpeedGameState() {
        inputBox = (FixedWord) new FixedWord((GameContainer.WIDTH/2.0f),
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH())
                .setWord("")
                .setColor(0xffffffff);
        scoreBox = (FixedWord) new FixedWord(25,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH())
                .setWord("Score: " + playersScore)
                .setColor(0xffffffff);
        timeBox = (FixedWord) new FixedWord(0,
                GameContainer.HEIGHT - Font.DEFAULT.getFontImage().getH())
                .setWord(String.format("%02d:%02d", ((int)totalTime) / 60, ((int)totalTime) % 60))
                .setColor(0xffffffff);
        timeBox.setPosX((GameContainer.WIDTH - timeBox.getW() - 25));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_1.wav").setVolume(0.65f));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_2.wav").setVolume(0.65f));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_3.wav").setVolume(0.65f));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_4.wav").setVolume(0.65f));
        scorePointsSoundEffects.add(new SoundClip("/audio/bonus_points_sound_effect_5.wav").setVolume(0.65f));
        scorePointsSoundEffects.add(new SoundClip("/audio/scoring_point.wav").setVolume(0.65f));
        scorePointsSoundEffects.add( new SoundClip("/audio/dabby_lets_go.wav").setVolume(0.65f));
        oofSoundEffect = new SoundClip("/audio/minecraft_oof_sound_effect.wav").setVolume(0.70f);
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
        gc.getRenderer().setAmbientColor(0xff353535);
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
                if (!lastCorrectlyEnteredWord.equals("")) {
                    if (!((FlyingWord) gameObject).isCorrectlyEntered()) {
                        playersScore -= ((FlyingWord) gameObject).getPoints() * (1.0 - (1.0 / (uniqueWords.get(lastCorrectlyEnteredWord).getSpeed())));
                        oofSoundEffect.play();
                    } else {
                        playersScore += uniqueWords.get(lastCorrectlyEnteredWord).getPoints() * (1.0- (1.0 / (uniqueWords.get(lastCorrectlyEnteredWord).getSpeed())));
                        scorePointsSoundEffects.get(MyRandomGenerator.getRandomNumberInRange(0, scorePointsSoundEffects.size()-1)).play();

                    }
                }
                typeSpeedGameState.removeWord(((FlyingWord) gameObject).getWord());
                typeSpeedGameState.removeYpos((int) gameObject.getPosY());
                gameObjects.remove(i);
                --i;
            }
        }
        if (gameObjects.size() < typeSpeedGameState.getMAX_WORDS()) {
            String uw = typeSpeedGameState.addUniqueWord();
            int uyp = typeSpeedGameState.addUniqueYpos();

            FlyingWord flyingWord = new FlyingWord(uw, 0, uyp);
            gameObjects.add(flyingWord);
            uniqueWords.put(flyingWord.getWord(), flyingWord);
        }
        scoreBox.setWord("Score: " + playersScore);
        timeBox.setWord(String.format("%02d:%02d", ((int)totalTime) / 60, ((int)totalTime) % 60));
    }

    @Override
    public void render(GameContainer gc, Renderer renderer) {
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
        inputBox.render(gc, renderer);
        scoreBox.render(gc ,renderer);
        timeBox.render(gc ,renderer);
    }

    @Override
    public void updateObs() {
        int chr = subject.getState();
        if (chr == KeyEvent.VK_ENTER) {
            //TODO check if the input-word is one of the words on the screen
            if (uniqueWords.containsKey(inputBox.getWord())) {
                uniqueWords.get(inputBox.getWord()).setCorrectlyEntered(true);
                uniqueWords.get(inputBox.getWord()).setDead(true);
                lastCorrectlyEnteredWord = "" + inputBox.getWord();
            }
            inputBox.setWord("");
            inputBox.setPosX((GameContainer.WIDTH/2.0f) - (inputBox.getW() / 2.0f));
        } else if (chr == KeyEvent.VK_BACK_SPACE) {
            //TODO remove the last character from input-word
            if (inputBox.getWord().length() != 0) {
                inputBox.setWord(inputBox.getWord().substring(0, inputBox.getWord().length()-1));
                inputBox.setPosX((GameContainer.WIDTH/2.0f) - (inputBox.getW() / 2.0f));
            }
        } else if ((chr >= KeyEvent.VK_A && chr <= KeyEvent.VK_Z) || (chr >= 97 && chr <= 122)) {
            inputBox.setWord(inputBox.getWord() + (char) chr);
            inputBox.setPosX((GameContainer.WIDTH/2.0f) - (inputBox.getW() / 2.0f));
        }
    }
}
