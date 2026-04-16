package org.cloud.random.Service;

import org.cloud.random.entity.History;
import java.util.List;

public interface HistoryService {



    List<History> getSearchHistory(String token, int type); // 根据 type 区分
    void clearSearchHistory(String token, int type); // 根据 type 删除
    void migrateHistoryToNewToken(String oldUserId, String newUserId);

}
