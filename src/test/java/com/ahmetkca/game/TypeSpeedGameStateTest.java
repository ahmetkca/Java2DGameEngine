package com.ahmetkca.game;

import com.ahmetkca.engine.gfx.Font;
import com.ahmetkca.utils.WordBankAPI;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
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
    @Order(1)
    public void test_UniqueYPositionsList_size() {
        assertEquals(typeSpeedGameState.getUniqueYPositions().size(), typeSpeedGameState.getMAX_WORDS());
    }

    @DisplayName("Check Y Position Uniqueness function correctly checks if the given y-position unique and in the correct range.")
    @Test
    void test_checkYpositionUniqueness() {
        List<Integer> tempUniqueYpositionsList = new ArrayList<>(
                Arrays.asList(50, 100, 150, 200, 269, 300));

        // Default font image is 15 pixel long
        assertEquals(Font.DEFAULT.getFontImage().getH(), 15);

        // no y-position must be greater than any y-position in the list plus font-image height
        // and less than any y-position in the list minus font-image height
        assertFalse(typeSpeedGameState.checkYpositionUniqueness(55, tempUniqueYpositionsList));
        assertFalse(typeSpeedGameState.checkYpositionUniqueness(180, tempUniqueYpositionsList));
        assertFalse(typeSpeedGameState.checkYpositionUniqueness(270, tempUniqueYpositionsList));
        assertTrue(typeSpeedGameState.checkYpositionUniqueness(19, tempUniqueYpositionsList));
    }

    @DisplayName("GenerateUniqueYPosition successfully generates unique y position that is not less then or greater than y-position of any other word's y-position plus or minus default font image's height")
    @Test
    public void test_generateUniqueYPosition() {

        assertEquals(Font.DEFAULT.getFontImage().getH(), 15);

        int generatedYpos = typeSpeedGameState.generateUniqueYPosition();
        System.out.println(typeSpeedGameState.getUniqueYPositions().toString());
        System.out.println(generatedYpos);
        for (int i = 0; i < typeSpeedGameState.getUniqueYPositions().size(); i++) {
            int uniqueYposInList = typeSpeedGameState.getUniqueYPositions().get(i);
            System.out.println((uniqueYposInList - typeSpeedGameState.getY_POS_LOCAL_RANGE())
                    + " -> " + uniqueYposInList + " <- "
                    + (uniqueYposInList + typeSpeedGameState.getY_POS_LOCAL_RANGE()));
            assertFalse(generatedYpos >= (uniqueYposInList - typeSpeedGameState.getY_POS_LOCAL_RANGE())
                    && generatedYpos <= (uniqueYposInList + typeSpeedGameState.getY_POS_LOCAL_RANGE()));
        }
    }

    @DisplayName("IsWordOnScreen successfully checks if the given word is in the list or not")
    @Test
    public void test_Is_Word_On_Screen() {
        List<String> uniqueWords = new ArrayList<>(
                Arrays.asList("hello", "world", "earth", "mars", "book", "loop", "tail")
        );
        TypeSpeedGameState test = new TypeSpeedGameState(wordBankAPI, uniqueWords, new ArrayList<>());

        assertTrue(test.isWordOnScreen("hello"));
        assertTrue(test.isWordOnScreen("loop"));
        assertFalse(test.isWordOnScreen("hand"));
        assertFalse(test.isWordOnScreen("pool"));
    }

    @DisplayName("GenerateUniqueWord successfully generates a word that is not in the current uniqueWord list")
    @Test
    public void test_Generate_Unique_Word() {
        List<String> uniqueWords = new ArrayList<>(
                Arrays.asList("hello", "world", "earth", "mars", "book", "loop", "tail")
        );
        TypeSpeedGameState test = new TypeSpeedGameState(wordBankAPI, uniqueWords, new ArrayList<>());

        String uniqueRandWord = test.generateUniqueWord();
        for (String word: test.getUniqueWords()) {
            assertNotEquals(uniqueRandWord, word);
        }
    }

    @DisplayName("addUniqueWord successfully adds a word to the uniqueWord list")
    @Test
    @Order(2)
    public void test_add_unique_word() {
        typeSpeedGameState.getUniqueWords().remove(typeSpeedGameState.getUniqueWords().size()-1);
        String temp = typeSpeedGameState.addUniqueWord();
        assertTrue(typeSpeedGameState.getUniqueWords().contains(temp));
        assertTrue(typeSpeedGameState.getUniqueWords().remove("" + temp));
    }

    @DisplayName("RemoveYPosition successfully removes given y-position from uniqueYPosition list")
    @Test
    public void test_remove_y_position() {
        TypeSpeedGameState test = new TypeSpeedGameState(wordBankAPI,
                new ArrayList<>(),
                new ArrayList<>(Arrays.asList(15, 35, 89, 111, 146)));
        assertTrue(test.removeYpos(15));
        for (Integer i: test.getUniqueYPositions()) {
            assertNotEquals(15, i);
        }
    }

    @DisplayName("")
    @Test
    public void test_Create_Unique_Y_Position_Set()  {

    }
}