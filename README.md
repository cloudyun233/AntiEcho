# 破茧索

## 项目简介

破茧索是一款**信息茧房破除系统**，致力于为用户提供跨领域知识探索工具。

短视频和购物网站算法往往将用户困于相对狭窄的兴趣圈层。破茧索通过**随机词条获取**与**知识图谱可视化**，以轻量、便捷的方式帮助用户接触原本不熟悉的知识领域，降低陌生领域的认知门槛。

## 核心功能

### 快速跳转（随机搜索）

```
Step1：从词库中随机抽取一个词
Step2：放入随机网站进行搜索
Step3：返回搜索结果
```

- 基于加权随机算法，保证词库多样性高、重复率低
- 支持按领域热度优先搜索（随机领域 → 高热度词汇），快速了解领域全貌

### 知识图谱

直观展示概念间的关联关系，帮助用户在探索过程中：
- 拓宽视野
- 理解全局知识结构

### 搜索历史管理

记录用户的搜索历史，支持查询、删除及定时过期清理。

**在线访问**：`http://111.231.191.2/#/`

## 项目结构

```
13组_破茧索项目/
├── 后端归档/                      # 后端代码及资源
│   ├── random/                    # Spring Boot 项目
│   │   ├── src/main/java/        # Java 源代码
│   │   │   └── org/cloud/random/
│   │   │       ├── Controller/   # API 控制器
│   │   │       ├── Service/      # 业务逻辑
│   │   │       ├── Repository/   # 数据访问层
│   │   │       ├── Entity/       # 实体类
│   │   │       ├── Security/     # JWT认证
│   │   │       └── Schedule/     # 定时任务
│   │   ├── src/main/resources/
│   │   │   └── application.properties
│   │   └── pom.xml
│   ├── python/                    # Python 图计算脚本
│   │   └── process_graph2.py
│   ├── csv/                       # 稀疏矩阵数据
│   │   └── words_5.npz
│   ├── words/                     # 原始词库文件
│   │   ├── CET4-clean.txt        # CET4 词汇
│   │   ├── CET6-clean.txt        # CET6 词汇
│   │   ├── TOEFL-CLEAN.txt       # TOEFL 词汇
│   │   ├── hot_vocab.txt         # 热词
│   │   ├── 科技.txt              # 科技词汇
│   │   ├── txttosql.py           # 导入脚本
│   │   └── 数据库编号.txt         # 词库编号说明
│   └── 数据库sql/                  # SQL 建表语句
│       ├── word.sql              # 单词表结构
│       ├── history.sql           # 历史记录表结构
│       └── words_1~5.sql         # 各词库数据
├── 前端归档/                      # 前端代码压缩包
├── PPTs/                         # 项目汇报PPT
├── 文档/                         # 项目文档
└── README.md                     # 项目主文档
```

## 技术栈

| 组件 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.6 |
| 安全框架 | Spring Security + JWT |
| 数据库 | MySQL (wordtest) |
| ORM | Spring Data JPA |
| 图计算 | Python + NumPy + SciPy |
| 前端 | uni-app + Vue2 |
| 构建工具 | Maven |
| Java版本 | 1.8 |

## 快速部署

### 1. 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE wordtest;"

# 导入表结构
mysql -u root -p wordtest < 数据库sql/word.sql
mysql -u root -p wordtest < 数据库sql/history.sql

# 导入词库数据
mysql -u root -p wordtest < 数据库sql/words_1.sql  # CET4
mysql -u root -p wordtest < 数据库sql/words_2.sql  # CET6
mysql -u root -p wordtest < 数据库sql/words_3.sql  # TOEFL
mysql -u root -p wordtest < 数据库sql/words_4.sql  # 科技词汇
mysql -u root -p wordtest < 数据库sql/words_5.sql  # 热词
```

### 2. 编译后端

```bash
cd random
mvn clean package
```

### 3. 启动服务

```bash
java -jar target/random-0.0.1-SNAPSHOT.jar
```

服务端口：`8080`

## 数据字典

| dictionaryId | 词库 | 说明 |
|--------------|------|------|
| 1 | CET4 | 大学英语四级词汇 |
| 2 | CET6 | 大学英语六级词汇 |
| 3 | TOEFL | TOEFL 词汇 |
| 4 | 科技词汇 | 科技类英语词汇 |
| 5 | 热词 | 网络热词/热搜词 |

## API 接口

### 随机词条接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/random-word` | GET | dictionaryId | 获取单个随机词条（按热度加权） |
| `/api/five-random-words` | GET | dictionaryId | 获取5个分层随机词条 |
| `/api/getwordbyId` | GET | dictionaryId, id | 根据ID获取词条 |
| `/api/getidbyword` | GET | dictionaryId, word | 根据词条获取ID |
| `/api/savehistory` | GET | word, token, type | 保存到搜索历史 |
| `/api/redirect` | GET | url | URL重定向（跳转到指定网站） |

### 历史记录接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/checktoken` | GET | Authorization | 验证/刷新JWT令牌 |
| `/api/gethistory` | GET | token, type | 获取搜索历史 |
| `/api/clearhistory` | GET | token, type | 清除历史记录 |

### 知识图谱接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/matrix/process-graph` | GET | fileIndex, targetNode | 获取节点的一跳/二跳邻居 |

## 核心功能详解

### 1. 加权随机词条

根据 `popularity` 字段加权随机抽取，热度越高考中概率越高：

```sql
SELECT * FROM words_N ORDER BY popularity * RAND() DESC LIMIT 1
```

### 2. 分层随机抽样

将词条按热度分为5层，每层随机抽取1个，保证抽取多样性。

### 3. JWT身份认证

- 令牌有效期：1小时
- Token过期自动刷新，历史记录自动迁移

### 4. 知识图谱（仅热词）

**MySQL与稀疏矩阵的关系**：
- MySQL `words_5` 表存储词条本体（id, word, popularity）
- 稀疏矩阵 `words_5.npz` 存储词条关联关系
- 两者通过 `id` 字段关联：矩阵的行/列索引对应MySQL中的 `id`

**稀疏矩阵存储方式**：
- 格式：`.npz`（NumPy稀疏矩阵格式）
- 类型：稀疏矩阵（COO/CSR格式）
- 存储内容：邻接矩阵，仅存储非零位置
  - `matrix[i][j] = 1` 表示第i个词与第j个词有关联
  - 矩阵大小 = 词数 × 词数
- 优势：避免存储大量0值，节省存储空间

**Python图计算流程**：
1. 加载 `words_5.npz` 稀疏矩阵
2. 根据目标节点ID查找其一行
3. `nonzero()` 找出一跳邻居（非零位置的列索引）
4. 遍历一跳邻居，再查找它们的邻居得到二跳邻居
5. 构建节点和边的JSON返回

```python
adj_matrix = load_npz('words_5.npz')  # 加载稀疏矩阵
one_hop = adj_matrix[target_node].nonzero()[1]  # 一跳邻居
second_hop = set()
for neighbor in one_hop:
    second_hop.update(adj_matrix[neighbor].nonzero()[1])  # 二跳邻居
```

### 5. 定时清理

每天凌晨4点自动清理30天前的历史记录。

## 配置文件

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/wordtest
spring.datasource.username=swu1001
spring.datasource.password=hello
jwt.secret=zyycbwwjjyzq
jwt.expiration=3600000
```

## Python 环境

知识图谱功能依赖 Python 3 环境：

```bash
pip install numpy scipy
```

## 数据库设计

### 词条表 (words_N)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键 |
| word | VARCHAR(255) | 词条 |
| popularity | INT | 热度值 |

### 历史记录表 (history)

| 字段 | 类型 | 说明 |
|------|------|------|
| word | VARCHAR(255) | 词条（主键） |
| timestamp | TIMESTAMP | 查询时间 |
| userid | VARCHAR(255) | 用户ID |
| type | INT | 0-热搜，1-英文词条 |

## 后端开发职责

- 用户搜索历史管理：记录保存、查询、删除、定时过期清理
- 基于字典ID权重的随机词条抽取，支持多领域词库灵活扩展
- 集成JWT认证体系：Token生成、校验、自动续签
- 集成Python知识图谱模块：Java调用Python脚本，实现概念关系可视化

## 项目成员

| 姓名 | 学号 | 职责 |
|------|------|------|
| 叶知秋 | 222021321062004 | 产品经理、需求分析师、项目经理 |
| 张云轶 | 222022321062020 | 后端工程师 |
| 王俊杰 | 222022321062018 | 审核、运维 |
| 曹卜文 | 222022321062021 | UI设计、前端工程师 |
