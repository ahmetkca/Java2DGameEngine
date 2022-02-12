package com.ahmetkca.game;

import com.ahmetkca.engine.GameContainer;
import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.utils.MyRandomGenerator;
import com.ahmetkca.utils.WordBankAPI;

import java.util.*;

public class TypeSpeedGameState {
    private int MAX_WORDS = 6;
    private int MAX_Y_POSITION = GameContainer.HEIGHT - (3*Font.DEFAULT.getFontImage().getH());
    private final List<String> uniqueWords;
    private final List<Integer> uniqueYPositions;
    private final WordBankAPI wordBankAPI;

    public TypeSpeedGameState(WordBankAPI wordBankAPI) {
        uniqueWords = new ArrayList<>();
        uniqueYPositions = new ArrayList<>();
        this.wordBankAPI = wordBankAPI;
        uniqueWords.addAll( wordBankAPI.getRandomUniqueWords(MAX_WORDS));
        uniqueYPositions.addAll(createUniqueYPositionSet(MAX_WORDS));
    }

    public boolean checkYpositionUniqueness(int posY, List<Integer> tempUniqYposSet) {
        int fontHeight = Font.DEFAULT.getFontImage().getH();
        for (Integer i : tempUniqYposSet) {
            if (posY >= (i - fontHeight)  && posY <= (i + fontHeight)) {
                return false;
            }
        }
        return true;
    }

    public Set<Integer> createUniqueYPositionSet(int len) {
        List<Integer> tempSet = new ArrayList<>();
        while (tempSet.size() != len) {
            boolean isValid;
            int randYpos = MyRandomGenerator.getRandomNumberInRange(0, MAX_Y_POSITION);
            if (!(isValid = checkYpositionUniqueness(randYpos, tempSet)))
                continue;
            else
                tempSet.add(randYpos);
        }
        return new HashSet<>(tempSet);
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
}
