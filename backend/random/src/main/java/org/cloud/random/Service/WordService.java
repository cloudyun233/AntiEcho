package org.cloud.random.Service;

import java.util.List;

public interface WordService {

    String getRandomWord(int dictionaryId);
    List<String> getFiveRandomWordsByPopularity(int dictionaryId);
    void saveWordToHistory(String word, String token, int type);
    String getWordById(int dictionaryId, int id);
    String getIdByWord(int dictionaryId, String word);

}
