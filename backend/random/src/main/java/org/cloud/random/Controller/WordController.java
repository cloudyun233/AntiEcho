package org.cloud.random.Controller;

import org.cloud.random.Service.WordService;
import org.cloud.random.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WordController {
    @Autowired
    private WordService wordService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 保存单词的历史记录
     *
     * @param word 单词
     * @param token JWT令牌
     * @param type 单词类型
     * @return 如果保存成功，返回状态码200和消息"存入成功"；否则返回状态码401和消息"刷新"
     */
    @GetMapping("/savehistory")
    public ResponseEntity<String> saveHistory(@RequestParam String word,
                                              @RequestHeader(value = "Authorization") String token,
                                              @RequestParam int type) {

        token = token.substring(7);
        if (!(jwtTokenProvider.validateToken(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("刷新");
        }

        wordService.saveWordToHistory(word, token, type);
        return ResponseEntity.ok("存入成功");
    }

    /**
     * 获取一个随机单词
     *
     * @param dictionaryId 字典ID，默认值为1
     * @return 随机单词
     * @throws UnsupportedEncodingException 如果字符编码不支持，抛出此异常
     */
    @GetMapping("/random-word")
    public String getRandomWord(@RequestParam(defaultValue = "1") int dictionaryId) throws UnsupportedEncodingException {
        return wordService.getRandomWord(dictionaryId);
    }

    /**
     * 获取五个随机单词
     *
     * @param dictionaryId 字典ID，默认值为1
     * @return 包含五个随机单词的列表
     */
    @GetMapping("/five-random-words")
    @ResponseBody
    public List<String> getFiveRandomWords(@RequestParam(defaultValue = "1") int dictionaryId) {
        return wordService.getFiveRandomWordsByPopularity(dictionaryId);
    }

    /**
     * 根据ID获取单词
     *
     * @param dictionaryId 字典ID
     * @param id 单词ID
     * @return 单词
     */
    @GetMapping("/getwordbyId")
    public String getWordById(@RequestParam int dictionaryId,
                              @RequestParam int id) {
        return wordService.getWordById(dictionaryId, id);
    }

    /**
     * 根据单词获取ID
     *
     * @param dictionaryId 字典ID
     * @param word 单词
     * @return 单词对应的ID
     */
    @GetMapping("/getidbyword")
    public String getIdByWord(@RequestParam int dictionaryId,
                              @RequestParam String word) {
        return wordService.getIdByWord(dictionaryId, word);
    }

    /**
     * 根据前端传来的URL进行重定向
     *
     * @param url 目标URL
     * @return 重定向视图
     */
    @GetMapping("/redirect")
    public RedirectView redirectToUrl(@RequestParam String url) {
        return new RedirectView(url);
    }
}
