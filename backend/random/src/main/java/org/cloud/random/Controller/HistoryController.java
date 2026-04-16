package org.cloud.random.Controller;

import org.cloud.random.Service.HistoryService;
import org.cloud.random.entity.History;
import org.cloud.random.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class HistoryController {

    @Autowired
    private HistoryService historyService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 检查并验证提供的 JWT 令牌的有效性。
     * 如果令牌无效或不存在，则生成一个新的令牌。
     *
     * @param token 包含在 Authorization 头中的 JWT 令牌，格式为 "Bearer <token>"
     * @return 如果令牌有效，返回 HTTP 200 OK 和 true；
     *         如果令牌无效或不存在，返回 HTTP 401 Unauthorized 和新生成的令牌。
     */
    @GetMapping("/checktoken")
    public ResponseEntity<?> checkToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            // 没有传入 token，则生成新的 token
            String userId = UUID.randomUUID().toString();
            String newToken = jwtTokenProvider.generateToken(userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(newToken);
        }

        token = token.substring(7); // 去掉 "Bearer " 前缀

        // 验证 token
        if (jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok(true);
        } else {
            // Token 无效，生成新的 token 并迁移历史记录
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String newToken = jwtTokenProvider.generateToken(userId);
            String newuserId = jwtTokenProvider.getUserIdFromToken(newToken);

            // 调用服务层迁移历史记录
            historyService.migrateHistoryToNewToken(userId, newuserId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(newToken);
        }
    }

    /**
     * 根据提供的 JWT 令牌和搜索类型获取用户的搜索历史记录。
     *
     * @param token 包含在 Authorization 头中的 JWT 令牌，格式为 "Bearer <token>"
     * @param type  搜索历史记录的类型
     * @return 如果令牌有效，返回 HTTP 200 OK 和搜索历史记录列表；
     *         如果令牌无效，返回 HTTP 401 Unauthorized 和 "刷新" 提示。
     */
    @GetMapping("/gethistory")
    public ResponseEntity<?> getSearchHistory(@RequestHeader(value = "Authorization") String token,
                                              @RequestParam int type) {

        token = token.substring(7);
        if (!(jwtTokenProvider.validateToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("刷新");
        }

        List<History> historyList = historyService.getSearchHistory(token, type);
        return ResponseEntity.ok(historyList);
    }

    /**
     * 根据提供的 JWT 令牌和搜索类型清除用户的搜索历史记录。
     *
     * @param token 包含在 Authorization 头中的 JWT 令牌，格式为 "Bearer <token>"
     * @param type  搜索历史记录的类型
     * @return 如果令牌有效，返回 HTTP 200 OK 和 "历史记录已清除" 消息；
     *         如果令牌无效，返回 HTTP 401 Unauthorized 和 "刷新" 提示。
     */
    @GetMapping("/clearhistory")
    public ResponseEntity<String> clearSearchHistory(@RequestHeader(value = "Authorization") String token,
                                                     @RequestParam int type) {

        token = token.substring(7);
        if (!(jwtTokenProvider.validateToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("刷新");
        }

        historyService.clearSearchHistory(token, type);
        return ResponseEntity.ok("历史记录已清除");
    }

}
