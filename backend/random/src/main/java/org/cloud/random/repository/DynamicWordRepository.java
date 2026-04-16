package org.cloud.random.repository;

import org.cloud.random.entity.Word;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository
public class DynamicWordRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 根据流行度加权随机选择一个单词。
     * @param tableName 表名
     * @return 随机选择的单词
     */
    public Word findWeightedRandomWord(String tableName) {
        String sql = "SELECT * FROM " + tableName + " w ORDER BY w.popularity * RAND() DESC LIMIT 1";
        Query query = entityManager.createNativeQuery(sql, Word.class);
        return (Word) query.getSingleResult();
    }

    /**
     * 按照流行度降序获取所有单词。
     * @param tableName 表名
     * @return 单词列表
     */
    public List<Word> findAllOrderByPopularityDesc(String tableName) {
        String sql = "SELECT * FROM " + tableName + " w ORDER BY w.popularity DESC";
        Query query = entityManager.createNativeQuery(sql, Word.class);
        return query.getResultList();
    }

    /**
     * 根据序号查询单词。
     * @param tableName 表名
     * @param id 序号
     * @return 查询到的单词
     */
    public String findWordById(String tableName, int id) {
        String sql = "SELECT w.word FROM " + tableName + " w WHERE w.id = :id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("id", id);
        return (String) query.getSingleResult();
    }

    /**
     * 根据单词查询ID。
     * @param tableName 表名
     * @param word 单词
     * @return 查询到的单词ID
     */
    public Integer findIdByWord(String tableName, String word) {
        String sql = "SELECT w.id FROM " + tableName + " w WHERE w.word = :word";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("word", word);
        return (Integer) query.getSingleResult();
    }

}