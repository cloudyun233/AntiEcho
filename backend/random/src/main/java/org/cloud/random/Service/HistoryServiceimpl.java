package org.cloud.random.Service;

import org.cloud.random.entity.History;
import org.cloud.random.repository.HistoryRepository;
import org.cloud.random.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class HistoryServiceimpl implements HistoryService {


    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public List<History> getSearchHistory(String token, int type) {
    String userId = jwtTokenProvider.getUserIdFromToken(token);
    return historyRepository.findByUseridAndTypeOrderByTimestampDesc(userId, type);
}

    @Override
    public void clearSearchHistory(String token, int type) {
        String userId = jwtTokenProvider.getUserIdFromToken(token);
        historyRepository.deleteByUseridAndType(userId, type);
    }


    @Override
    public void migrateHistoryToNewToken(String oldUserId, String newUserId) {
        historyRepository.updateUserIdByOldUserId(oldUserId, newUserId);
    }


}
