package org.cloud.random.Service;


import org.cloud.random.entity.History;
import org.cloud.random.entity.Word;
import org.cloud.random.repository.DynamicWordRepository;
import org.cloud.random.repository.HistoryRepository;
import org.cloud.random.repository.WordRepository;
import org.cloud.random.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
public class WordServiceimpl implements WordService{

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private DynamicWordRepository dynamicWordRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    @Override
    public void saveWordToHistory(String word, String token, int type) {
        String userId = jwtTokenProvider.getUserIdFromToken(token);

        // 查找是否存在相同的word
        Optional<History> existingHistory = historyRepository.findByWordAndUserid(word, userId);

        History history;
        if (existingHistory.isPresent()) {
            // 如果存在相同的word，更新存入时间
            history = existingHistory.get();
            history.setTimestamp(LocalDateTime.now());
        } else {
            // 否则，创建并保存新的记录
            history = new History();
            history.setWord(word);
            history.setTimestamp(LocalDateTime.now());
            history.setUserid(userId);
            history.setType(type);  // 设置type字段
        }
        historyRepository.save(history);

    }



    @Override
    public String getRandomWord(int dictionaryId) {
        String tableName = "words_" + dictionaryId;
        Word word = dynamicWordRepository.findWeightedRandomWord(tableName);
        if (word != null) {
            return word.getWord();
        } else {
            return "No word found";
        }
    }

    @Override
    public List<String> getFiveRandomWordsByPopularity(int dictionaryId) {
        String tableName = "words_" + dictionaryId;
        List<Word> words = dynamicWordRepository.findAllOrderByPopularityDesc(tableName);
        int size = words.size();
        List<String> selectedWords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int fromIndex = i * (size / 5);
            int toIndex = (i + 1) * (size / 5) - 1;
            if (i == 4) { // 最后一部分可能需要包括剩余的所有元素
                toIndex = size - 1;
            }
            List<Word> partition = words.subList(fromIndex, toIndex + 1);
            Random random = new Random();
            int randomIndex = random.nextInt(partition.size());
            selectedWords.add(partition.get(randomIndex).getWord());
        }
        return selectedWords;
    }

    @Override
    public String getWordById(int dictionaryId, int id){
        return dynamicWordRepository.findWordById("words_"+dictionaryId,id);
    };


    @Override
    public String getIdByWord(int dictionaryId, String word) {
        return dynamicWordRepository.findIdByWord("words_"+dictionaryId,word)+"";
    }
}