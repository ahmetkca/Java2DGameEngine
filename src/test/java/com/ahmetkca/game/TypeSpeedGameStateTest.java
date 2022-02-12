package com.ahmetkca.game;

import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.utils.WordBankAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TypeSpeedGameStateTest {

    private static final WordBankAPI wordBankAPI = new WordBankAPI();
    private static final TypeSpeedGameState typeSpeedGameState = new TypeSpeedGameState(wordBankAPI);

    @DisplayName("Constructor of TypeSpeedGameState is successfully completed both uniqueWords and uniqueYPositions initialized.")
    @Test
    public void test_TypeSpeedGameState_Constructor() {
        assertNotNull(typeSpeedGameState.getUniqueWords());
        assertNotNull(typeSpeedGameState.getUniqueYPositions());
    }

    @DisplayName("uniqueWords list contains only unique words.")
    @Test
    public void check_Uniqueness_Of_The_Word_List() {
        int uniqueWordListSize = typeSpeedGameState.getUniqueWords().size();
        // Java Set util only contains unique elements
        // if the size of newly created set is equals to unique word list (which is a normal list) then it means
        // uniqueWord list contains only unique
        Set<String> uniqueWordSet = new HashSet<>(typeSpeedGameState.getUniqueWords());
        assertEquals(uniqueWordSet.size(), uniqueWordListSize);
    }

    @DisplayName("TypeSpeedGameState successfully created uniqueWord list with the size MAX_WORDS")
    @Test
    public void test_UniqueWordList_size() {
        assertEquals(typeSpeedGameState.getUniqueWords().size(), typeSpeedGameState.getMAX_WORDS());
    }

    @DisplayName("uniqueYPositions list contains only unique words.")
    @Test
    public void check_Uniqueness_Of_The_YPosition_List() {
        int uniqueYPositionsListSize = typeSpeedGameState.getUniqueYPositions().size();
        // Java Set util only contains unique elements
        // if the size of newly created set is equals to uniqueYpositions list (which is a normal list) then it means
        // uniqueYpositions list only contains unique y-coordinates.
        Set<Integer> uniqueYPositionsSet = new HashSet<>(typeSpeedGameState.getUniqueYPositions());
        assertEquals(uniqueYPositionsSet.size(), uniqueYPositionsListSize);
    }

    @DisplayName("TypeSpeedGameState successfully created uniqueYPositions list with the size MAX_WORDS")
    @Test
    public void test_UniqueYPositionsList_size() {
        assertEquals(typeSpeedGameState.getUniqueYPositions().size(), typeSpeedGameState.getMAX_WORDS());
    }

    @DisplayName("Check Y Position Uniqueness function correctly checks if the given y-position unique and in the correct range.")
    @Test
    void test_checkYpositionUniqueness() {
        List<Integer> tempUniqueYpositionsList = new ArrayList<>(
                Arrays.asList(50, 100, 150, 200, 250, 300));

        // Default font image is 15 pixel long
        assertEquals(Font.DEFAULT.getFontImage().getH(), 15);

        // no y-position must be greater than any y-position in the list plus font-image height
        // and less than any y-position in the list minus font-image height
        assertFalse(typeSpeedGameState.checkYpositionUniqueness(55, tempUniqueYpositionsList));
        assertTrue(typeSpeedGameState.checkYpositionUniqueness(75, tempUniqueYpositionsList));
    }

    @Test
    public void test_generateUniqueYPosition() {
        List<Integer> tempUniqueYpositionsList = new ArrayList<>(
                Arrays.asList(50, 100));

        int typeSpeedGameState.generateUniqueYPosition()
    }
}