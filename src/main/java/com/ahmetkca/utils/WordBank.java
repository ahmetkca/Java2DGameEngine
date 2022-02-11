package com.ahmetkca.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordBank {
    private final String RANDOM_WORD_API = "https://random-word-api.herokuapp.com/word?number=%d";
    private List<String> words;
    public WordBank() {
         words = new ArrayList<>();
         init();
    }
    private void init() {
        words = getRandomWords(5);
    }

    private List<String> getRandomWords(int num) {
        List<String> words = new ArrayList<>();
        BufferedReader bufferedReader;
        StringBuffer responseContent = new StringBuffer();
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
            for (int i = 0; i < splitResult.length; i++) {
                String word = splitResult[i].replaceAll("\\[", "").replaceAll("\"", "");
                words.add(word);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return words;
    }

    public String getRandomWord() {
        return words.get(WordBank.getRandomNumberInRange(0, words.size()-1));
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void main(String[] args) {
        WordBank wordBank = new WordBank();
        System.out.println(wordBank.getRandomWord());
    }

}
