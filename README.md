# 破茧索 - 信息茧房破除系统

[![Java](https://img.shields.io/badge/Java-1.8-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.6-green.svg)](https://spring.io/projects/spring-boot)
[![uni-app](https://img.shields.io/badge/uni-app-Vue2-blue.svg)](https://uniapp.dcloud.net.cn/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 项目简介

破茧索是一款**信息茧房破除系统**，致力于为用户提供跨领域知识探索工具。

短视频和购物网站算法往往将用户困于相对狭窄的兴趣圈层。破茧索通过**随机词条获取**与**知识图谱可视化**，以轻量、便捷的方式帮助用户接触原本不熟悉的知识领域，降低陌生领域的认知门槛。

## 核心功能

### 随机词条探索

- 基于加权随机算法，根据词条热度智能分配抽取概率
- 支持单次随机抽取或分层抽样（5个词条），保证知识多样性
- 多领域词库支持：CET4/6、TOEFL、科技词汇、网络热词

### 知识图谱可视化

- 直观展示概念间的关联关系（一跳/二跳邻居节点）
- 基于稀疏矩阵存储的图数据结构，高效处理大规模关联
- 交互式图形界面，支持节点探索和关系查看

### 搜索历史管理

- 记录用户搜索历史，支持查询和删除操作
- JWT身份认证，保障用户数据安全
- 定时任务自动清理30天前的过期记录

## 项目结构

```
AntiEcho/
├── backend/                        # 后端相关代码和资源
│   ├── random/                     # Spring Boot 后端项目
│   │   ├── src/main/java/org/cloud/random/
│   │   │   ├── Controller/         # REST API控制器
│   │   │   ├── Service/            # 业务逻辑层
│   │   │   ├── entity/             # 数据实体类
│   │   │   ├── repository/         # 数据访问层(JPA)
│   │   │   ├── security/           # JWT认证组件
│   │   │   ├── schedule/           # 定时任务调度
│   │   │   └── RandomApplication.java  # 应用入口
│   │   ├── src/main/resources/
│   │   │   ├── application.properties  # 应用配置
│   │   │   └── static/index.html   # 静态资源
│   │   ├── pom.xml                 # Maven依赖配置
│   │   └── .gitignore
│   ├── python/                     # Python图计算脚本
│   │   └── process_graph2.py       # 稀疏矩阵处理脚本
│   ├── csv/                        # 图数据稀疏矩阵
│   │   └── words_5.npz             # 热词关联矩阵(NumPy格式)
│   ├── words/                      # 原始词库文件
│   │   ├── CET4-clean.txt          # CET4词汇
│   │   ├── CET6-clean.txt          # CET6词汇
│   │   ├── TOEFL-CLEAN.txt         # TOEFL词汇
│   │   ├── hot_vocab.txt           # 网络热词
│   │   ├── 科技.txt                # 科技类词汇
│   │   ├── txttosql.py             # 词库导入脚本
│   │   └── 数据库编号.txt           # 词库编号说明
│   └── 数据库sql/                  # 数据库建表和数据脚本
│       ├── word.sql                # 词条表结构
│       ├── history.sql             # 历史记录表结构
│       └── words_1~5.sql           # 各词库数据导入脚本
├── frontend/                       # 前端项目
│   └── random/                     # uni-app移动端应用
│       ├── pages/                  # 页面组件
│       │   ├── index/              # 首页(中文)
│       │   ├── index-en/           # 首页(英文)
│       │   ├── randomword/         # 随机词条页面
│       │   ├── graph/              # 知识图谱页面
│       │   ├── history/            # 历史记录页面(中文)
│       │   ├── history/history_en/ # 历史记录页面(英文)
│       │   └── members/            # 项目成员页面
│       ├── static/                 # 静态资源
│       ├── uni_modules/uview-ui/   # UI组件库
│       ├── package.json            # 前端依赖配置
│       ├── pages.json              # 页面路由配置
│       ├── manifest.json           # 应用配置
│       └── App.vue                 # 应用入口
├── PPTs/                           # 项目汇报演示文稿
│   ├── 破茧索项目汇报.pptx
│   └── 软件开发实践pre.pptx
└── README.md                       # 项目主文档
```

## 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| 后端框架 | Spring Boot 2.7.6 | RESTful API服务 |
| 安全认证 | JWT (jjwt 0.9.1) | 无状态身份认证 |
| 数据库 | MySQL 8.0 | 词条和历史记录存储 |
| ORM框架 | Spring Data JPA | 数据持久化 |
| 图处理 | Python + NumPy + SciPy | 稀疏矩阵图计算 |
| 图计算库 | JGraphT 1.5.1 | Java图算法支持 |
| 前端框架 | uni-app + Vue2 | 跨平台移动端应用 |
| UI组件 | uView UI | 移动端组件库 |
| 图谱可视化 | relation-graph 2.2.8 | 关系图渲染 |
| 构建工具 | Maven | 后端项目构建 |
| Java版本 | 1.8 | 运行时环境 |

## 快速开始

### 环境要求

- Java 1.8+
- Maven 3.6+
- MySQL 8.0+
- Python 3.7+ (知识图谱功能)
- Node.js 14+ (前端开发)

### 1. 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE wordtest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入表结构
mysql -u root -p wordtest < backend/数据库sql/word.sql
mysql -u root -p wordtest < backend/数据库sql/history.sql

# 导入词库数据
mysql -u root -p wordtest < backend/数据库sql/words_1.sql  # CET4词汇
mysql -u root -p wordtest < backend/数据库sql/words_2.sql  # CET6词汇
mysql -u root -p wordtest < backend/数据库sql/words_3.sql  # TOEFL词汇
mysql -u root -p wordtest < backend/数据库sql/words_4.sql  # 科技词汇
mysql -u root -p wordtest < backend/数据库sql/words_5.sql  # 网络热词
```

### 2. 配置后端

编辑 `backend/random/src/main/resources/application.properties`:

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/wordtest
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT配置
jwt.secret=your_secret_key_here
jwt.expiration=3600000  # 1小时(毫秒)
```

### 3. 构建和启动后端

```bash
cd backend/random
mvn clean package
java -jar target/random-0.0.1-SNAPSHOT.jar
```

后端服务将在 `http://localhost:8080` 启动。

### 4. 安装前端依赖(可选)

如需开发前端:

```bash
cd frontend/random
npm install
```

使用 [HBuilderX](https://www.dcloud.io/hbuilderx.html) 打开 `frontend/random` 目录进行开发和调试。

### 5. Python环境配置(知识图谱)

```bash
pip install numpy scipy
```

## API接口文档

### 词条相关接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/random-word` | GET | dictionaryId (默认1) | 获取单个随机词条(按热度加权) |
| `/api/five-random-words` | GET | dictionaryId (默认1) | 获取5个分层随机词条 |
| `/api/getwordbyId` | GET | dictionaryId, id | 根据ID获取词条内容 |
| `/api/getidbyword` | GET | dictionaryId, word | 根据词条获取ID |
| `/api/savehistory` | GET | word, Authorization, type | 保存搜索历史 |
| `/api/redirect` | GET | url | URL重定向跳转 |

### 历史记录接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/checktoken` | GET | Authorization | 验证/刷新JWT令牌 |
| `/api/gethistory` | GET | token, type | 获取用户搜索历史 |
| `/api/clearhistory` | GET | token, type | 清除用户搜索历史 |

### 知识图谱接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/api/matrix/process-graph` | GET | fileIndex, targetNode | 获取指定节点的一跳和二跳邻居 |

### 请求示例

```bash
# 获取随机热词(dictionaryId=5为热词库)
curl "http://localhost:8080/api/random-word?dictionaryId=5"

# 获取5个随机英文词条(dictionaryId=1为CET4)
curl "http://localhost:8080/api/five-random-words?dictionaryId=1"

# 获取知识图谱邻居(fileIndex=5对应热词矩阵, targetNode=10)
curl "http://localhost:8080/api/matrix/process-graph?fileIndex=5&targetNode=10"
```

## 核心功能设计

### 加权随机算法

根据词条的 `popularity` 字段进行加权随机抽取，热度越高的词条被选中的概率越大：

```sql
SELECT * FROM words_N ORDER BY popularity * RAND() DESC LIMIT 1
```

### 分层抽样机制

将词库按热度分为5个层次，每层随机抽取1个词条，确保抽取结果的多样性和覆盖面。

### JWT身份认证

- 令牌有效期：1小时(3600000毫秒)
- 使用HS512签名算法保障安全性
- Token过期后仍可提取用户ID用于历史数据迁移

### 知识图谱数据结构

**MySQL与稀疏矩阵的关联关系**：

- MySQL `words_5` 表存储词条基本信息(id, word, popularity)
- 稀疏矩阵 `words_5.npz` 存储词条间的关联关系(邻接矩阵)
- 通过 `id` 字段关联：矩阵的行/列索引对应MySQL中的词条ID

**稀疏矩阵优势**：

- 采用NumPy COO/CSR格式存储，仅保存非零元素
- `matrix[i][j] = 1` 表示第i个词与第j个词存在关联
- 有效避免存储大量0值，节省空间并提升计算效率

**图计算流程**：

1. 加载 `.npz` 格式的稀疏矩阵
2. 根据目标节点ID定位到对应行
3. 使用 `nonzero()` 方法找出一跳邻居节点
4. 遍历一跳邻居，收集二跳邻居节点
5. 构建节点和边的JSON结构返回前端

## 数据字典

### 词库编号映射

| dictionaryId | 词库名称 | 说明 | 数据文件 |
|--------------|----------|------|----------|
| 1 | CET4 | 大学英语四级词汇 | words_1.sql |
| 2 | CET6 | 大学英语六级词汇 | words_2.sql |
| 3 | TOEFL | 托福考试词汇 | words_3.sql |
| 4 | 科技词汇 | 科学技术类英语词汇 | words_4.sql |
| 5 | 热词 | 网络热搜词/流行词 | words_5.sql + words_5.npz |

### 数据库表结构

**词条表 (words_N)**

| 字段 | 类型 | 说明 |
|------|------|------|
| id | INT | 主键，自增 |
| word | VARCHAR(255) | 词条内容 |
| popularity | INT | 热度值(用于加权随机) |

**历史记录表 (history)**

| 字段 | 类型 | 说明 |
|------|------|------|
| word | VARCHAR(255) | 搜索的词条(主键) |
| timestamp | TIMESTAMP | 搜索时间 |
| userid | VARCHAR(255) | 用户ID(从JWT提取) |
| type | INT | 类型：0-热搜词，1-英文词条 |

## 项目成员

| GitHub | 职责 |
|--------|------|
| [@Painnb](https://github.com/Painnb) | 产品经理、知识图谱与热度算法设计、项目经理 |
| [@cloudyun233](https://github.com/cloudyun233) | 后端开发工程师、运维工程师 |
| [@bufan1213](https://github.com/bufan1213) | UI设计师、前端开发工程师 |

## 开发说明

### 后端开发

- **Controller层**：处理HTTP请求，参数校验和响应封装
- **Service层**：核心业务逻辑，包括随机算法、历史记录管理等
- **Repository层**：基于Spring Data JPA的数据访问接口
- **Security包**：JWT令牌生成、验证和解析
- **Schedule包**：定时任务，每天凌晨4点自动清理30天前历史记录

### 前端开发

- 使用uni-app框架，支持多端发布(H5、小程序、App)
- uView UI组件库提供丰富的移动端组件
- relation-graph库用于知识图谱的交互式可视化
- 支持中英文双语界面

### 扩展词库

1. 准备词库文本文件(格式：每行一个词)
2. 使用 `words/txttosql.py` 脚本生成SQL文件
3. 将SQL文件放入 `数据库sql/` 目录
4. 在 `application.properties` 或代码中配置新的dictionaryId
5. 执行SQL文件导入数据

## 许可证

本项目仅供学习和研究使用。
