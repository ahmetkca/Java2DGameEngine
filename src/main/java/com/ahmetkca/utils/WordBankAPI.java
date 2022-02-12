package com.ahmetkca.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class WordBankAPI {
    private final String RANDOM_WORD_API = "https://random-word-api.herokuapp.com/word?number=%d";
    private List<String> words;
    public WordBankAPI() {
         words = new ArrayList<>();
         init();
    }
    private void init() {
        System.out.println("Initialize WordBankAPI");
//        words = getRandomWords(500);
        String[] initWords = {"lonely","with","air","become","explore","beauty","answer","descend","perfect","poet","son","find","top","axe","honest","speech","lead","hot","soft","straw","network","network","qualify","spot","wipe","serve","pile","top","mixture","madden","honesty","nursery","fever","widower","speech","open","soldier","judge","key","rank","urgent","pour","course","sad","mail","disturb","shilling","crown","trouble","gas","afraid","ripe","toe","hit","improve","birth","you","rake","disagree","splendid","stuff","treasure","ocean","director","grass","anxiety","horse","deal","scold","dismiss","hardly","better","parent","trust","damage","meal","thirst","weekend","nature","ton","standard","lunch","monkey","ring","board","toe","duty","surround","tip","temple","fat","cure","soul","share","headache","actor","powder","feed","break","pause"};
        for (int i = 0; i < initWords.length ; i++) {
            words.add(initWords[i]);
        }
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
        WordBankAPI wordBankAPI = new WordBankAPI();
        System.out.println(wordBankAPI.getRandomWord());
    }

}
