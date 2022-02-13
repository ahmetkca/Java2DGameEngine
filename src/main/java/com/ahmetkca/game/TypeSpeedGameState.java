package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.utils.MyRandomGenerator;
import com.ahmetkca.utils.WordBankAPI;

import java.util.*;

public class TypeSpeedGameState {
    private int MAX_WORDS = 7;
    private final int MAX_Y_POSITION = GameContainer.HEIGHT - (3*Font.DEFAULT.getFontImage().getH());



    private final float Y_POS_LOCAL_RANGE = Font.DEFAULT.getFontImage().getH() * 1.25f;
    private final List<String> uniqueWords;
    private final List<Integer> uniqueYPositions;
    private final HashMap<String, Integer> uniqueWordsAndYpositions;
    private final WordBankAPI wordBankAPI;

    public TypeSpeedGameState(WordBankAPI wordBankAPI, List<String> uniqueWords, List<Integer> uniqueYPositions) {
        System.out.println(Font.DEFAULT.getFontImage().getH());
        uniqueWordsAndYpositions = new HashMap<>();
        this.wordBankAPI = wordBankAPI;
        this.uniqueWords = uniqueWords;
        this.uniqueYPositions = uniqueYPositions;
    }

    public TypeSpeedGameState(WordBankAPI wordBankAPI) {
        System.out.println(Font.DEFAULT.getFontImage().getH());
        uniqueWordsAndYpositions = new HashMap<>();
        uniqueWords = new ArrayList<>();
        uniqueYPositions = new ArrayList<>();
        this.wordBankAPI = wordBankAPI;
        uniqueWords.addAll( wordBankAPI.getRandomUniqueWords(MAX_WORDS));
        while (uniqueYPositions.size() != MAX_WORDS) {
            uniqueYPositions.add(generateUniqueYPosition());
        }
//        uniqueYPositions.addAll(createUniqueYPositionSet(MAX_WORDS));
    }

    public boolean checkYpositionUniqueness(int posY, List<Integer> tempUniqYposSet) {
        for (Integer i : tempUniqYposSet) {
            System.out.println(i);
            if (posY >= (i - Y_POS_LOCAL_RANGE)  && posY <= (i + Y_POS_LOCAL_RANGE)) {
                return false;
            }
        }
        return true;
    }

    public int generateUniqueYPosition() {
        int randYpos = -1;
        boolean isUnique = false;
        while (!isUnique) {
            randYpos = MyRandomGenerator.getRandomNumberInRange(0, MAX_Y_POSITION);
            isUnique = checkYpositionUniqueness(randYpos, uniqueYPositions);
        }
        return randYpos;
    }

    public boolean isWordOnScreen(String word) {
        for (String s: uniqueWords) {
            if (s.equals(word))
                return true;
        }
        return false;
    }

    public boolean removeWord(String word) {
        uniqueWords.remove(word);
        return true;
    }

    public boolean removeYpos(int posY) {
        try {
            uniqueYPositions.remove(Integer.valueOf(posY));
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public String addUniqueWord() {
        String t = generateUniqueWord();
        uniqueWords.add(t);
        return t;
    }

    public int addUniqueYpos() {
        int t = generateUniqueYPosition();
        uniqueYPositions.add(t);
        return t;
    }

    public String generateUniqueWord() {
        String randWord = wordBankAPI.getRandomWord();
        while (uniqueWords.contains(randWord)) {
            randWord = wordBankAPI.getRandomWord();
        }
        return randWord;
    }

    public int getMAX_WORDS() {
        return MAX_WORDS;
    }

    public void setMAX_WORDS(int MAX_WORDS) {
        this.MAX_WORDS = MAX_WORDS;
    }

    public List<String> getUniqueWords() {
        return uniqueWords;
    }

    public List<Integer> getUniqueYPositions() {
        return uniqueYPositions;
    }

    public WordBankAPI getWordBankAPI() {
        return wordBankAPI;
    }

    public int getMAX_Y_POSITION() {
        return MAX_Y_POSITION;
    }

    public float getY_POS_LOCAL_RANGE() {
        return Y_POS_LOCAL_RANGE;
    }
}
