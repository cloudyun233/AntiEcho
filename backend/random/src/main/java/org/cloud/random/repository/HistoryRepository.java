package org.cloud.random.repository;


import org.cloud.random.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, String> {

    List<History> findByUseridAndTypeOrderByTimestampDesc(String userid, int type);
    void deleteByUseridAndType(String userid, int type);


    @Modifying
    @Transactional
    @Query("UPDATE History h SET h.userid = ?2 WHERE h.userid = ?1")
    void updateUserIdByOldUserId(String oldUserId, String newUserId);

    Optional<History> findByWordAndUserid(String word, String userId);

    @Modifying
    @Transactional
    void deleteByTimestampBefore(LocalDateTime timestamp);
}