//package org.cloud.random.Service;
//
//
//import org.springframework.stereotype.Service;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.List;
//
//
//
//@Service
//public class GraphService {
//
//    public String processGraph(int fileIndex, int targetNode) {
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("sudo","/root/anaconda3/bin/python", "/home/suijiwords/python/process_graph2.py", String.valueOf(fileIndex), String.valueOf(targetNode));
//
//
//            // 获取并打印构造的命令
//            List<String> command = processBuilder.command();
//            System.out.println("构造的命令: " + String.join(" ", command));
//
//            Process process = processBuilder.start();
//
//            // 读取Python脚本的标准输出
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder output = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                output.append(line).append("\n");
//            }
//
//            // 读取Python脚本的错误输出
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            StringBuilder errorOutput = new StringBuilder();
//            while ((line = errorReader.readLine()) != null) {
//                errorOutput.append(line).append("\n");
//            }
//
//            // 等待进程完成
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                throw new RuntimeException("Python script failed with exit code " + exitCode + ". Error: " + errorOutput.toString());
//            }
//
//            return output.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to run Python script", e);
//        }
//    }
//}
//
//
//

package org.cloud.random.Service;

import com.google.gson.Gson;
import org.cloud.random.repository.DynamicWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class GraphService {

    @Autowired
    private DynamicWordRepository dynamicWordRepository;

    public String processGraph(int fileIndex, int targetNode) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sudo", "/root/anaconda3/bin/python", "/home/suijiwords/python/process_graph2.py", String.valueOf(fileIndex), String.valueOf(targetNode));
//            ProcessBuilder processBuilder = new ProcessBuilder("python", "d:/Hilling/Desktop/12345/random/python/process_graph2.py", String.valueOf(fileIndex), String.valueOf(targetNode));

            // 获取并打印构造的命令
            List<String> command = processBuilder.command();
            System.out.println("构造的命令: " + String.join(" ", command));

            Process process = processBuilder.start();

            // 读取Python脚本的标准输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 读取Python脚本的错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python script failed with exit code " + exitCode + ". Error: " + errorOutput.toString());
            }

            // 假设 output 是 JSON 格式的字符串，解析它
            // 这里假设你有一个方法 parseJsonOutput 来解析 JSON 输出
            Result result = parseJsonOutput(output.toString(), fileIndex);

            // 将结果字典转换为 JSON 字符串
            String jsonStr = new Gson().toJson(result);

            return jsonStr;
        } catch (Exception e) {
            throw new RuntimeException("Failed to run Python script", e);
        }
    }

    private Result parseJsonOutput(String output, int fileIndex) {
        // 使用 Gson 解析 JSON 字符串
        Gson gson = new Gson();
        Result result = gson.fromJson(output, Result.class);

        // 替换 text 字段
        for (Node node : result.getNodes()) {
            String word = dynamicWordRepository.findWordById("words_" + fileIndex, Integer.parseInt(node.getId()));
            node.setText(word);
        }

        return result;
    }

    // 定义 Result, Node, Line 类来匹配 JSON 结构
    static class Result {
        private List<Node> nodes;
        private List<Line> lines;

        // Getters and Setters
        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public List<Line> getLines() {
            return lines;
        }

        public void setLines(List<Line> lines) {
            this.lines = lines;
        }
    }

    static class Node {
        private String id;
        private String text;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    static class Line {
        private String from;
        private String to;

        // Getters and Setters
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}