package org.cloud.random.Controller;

import org.cloud.random.Service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/matrix")
public class CsvMatrixController {

    @Autowired
    private GraphService graphService;

    /**
     * 调用 Python 脚本处理图数据，返回指定节点的一跳和二跳节点。
     *
     * @param fileIndex 文件索引，用于标识要处理的 CSV 文件
     * @param targetNode 目标节点的 ID
     * @return 包含一跳和二跳节点信息的字符串
     */
    @GetMapping("/process-graph")
    public String processGraph(@RequestParam int fileIndex, @RequestParam int targetNode) {
        return graphService.processGraph(fileIndex, targetNode);
    }
}
