import numpy as np
from scipy.sparse import load_npz
import json
import sys

def process_graph2(file_index, target_node):
    # 动态生成文件路径
    # file_path = f'../csv/words_{file_index}.npz'
    file_path = f'/home/suijiwords/csv/words_{file_index}.npz'
    
    # 加载稀疏矩阵
    adj_matrix = load_npz(file_path)
    
    # 获取一跳邻居
    one_hop_neighbors = set(adj_matrix[target_node].nonzero()[1])
    
    # 获取二跳邻居
    second_neighbors = set()
    for neighbor in one_hop_neighbors:
        second_neighbors.update(adj_matrix[neighbor].nonzero()[1])
    
    # 合并所有邻居
    all_neighbors = [target_node] + list(one_hop_neighbors) + list(second_neighbors)
    
    # 获取边
    edges = set()
    for neighbor in one_hop_neighbors:
        if target_node < neighbor:
            edges.add((target_node, neighbor))
    for neighbor in second_neighbors:
        for neighbor_of_neighbor in adj_matrix[neighbor].nonzero()[1]:
            if neighbor_of_neighbor != target_node and target_node < neighbor_of_neighbor:
                edges.add((neighbor, neighbor_of_neighbor))
    
    # 构建结果字典
    result = {
        "nodes": [{"id": str(node), "text": str(node)} for node in all_neighbors],
        "lines": [{"from": str(edge[0]), "to": str(edge[1])} for edge in edges]
    }
    
    # 将结果字典转换为 JSON 字符串
    json_str = json.dumps(result)
    
    return json_str

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(json.dumps({"error": "需要两个参数: 文件索引和目标节点"}))
        sys.exit(1)
    
    file_index = int(sys.argv[1])
    target_node = int(sys.argv[2])
    
    result = process_graph2(file_index, target_node)
    print(result)

