package com.ahmetkca.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class WordBank {
    private final String RANDOM_WORD_API = "https://random-word-api.herokuapp.com/word?number=%d";
    private List<String> words;
    public WordBank() {
         words = new ArrayList<>();
         init();
    }
    private void init() {
        System.out.println("Initialize WordBank");
        words = getRandomWords(500);
//        words.add("boat");
//        words.add("into");
//        words.add("other");
//        words.add("govern");
//        words.add("inch");
//        words.add("charm");
    }

    public Set<String> getRandomUniqueWords(int len) {
        Set<String> uniqueWords = new HashSet<>();
        while (uniqueWords.size() != len) {
            uniqueWords.add(getRandomWord());
        }
        return uniqueWords;
    }

    private List<String> getRandomWords(int num) {
        List<String> words = new ArrayList<>();
        BufferedReader bufferedReader;
        StringBuilder responseContent = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(String.format(RANDOM_WORD_API, num));
            connection = (HttpURLConnection) url.openConnection();
            // request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            if(status != 200)
                return words;
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseContent.append(line);
            }
            String[] splitResult = responseContent.toString().split(",");
            for (String s : splitResult) {
                String word = s.replaceAll("\\[", "")
                        .replaceAll("\"", "")
                        .replaceAll("]", "");
                words.add(word);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return words;
    }

    public String getRandomWord() {
        return words.get(MyRandomGenerator.getRandomNumberInRange(0, words.size()-1));
    }

    public static void main(String[] args) {
        WordBank wordBank = new WordBank();
        System.out.println(wordBank.getRandomWord());
    }

}
