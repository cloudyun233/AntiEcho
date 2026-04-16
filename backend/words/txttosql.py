import pymysql
import random


# 使用原始字符串来定义路径
file_path = r'd:\Hilling\Desktop\12345\words\hot_vocab.txt'

def read_txt_to_list(file_path):
    # 使用原始字符串来定义文件路径
    file_path = file_path.strip('"')  # 移除引号
    with open(file_path, 'r', encoding='utf-8') as file:
        lines = file.readlines()
        result = []
        for line in lines:
            parts = line.strip().split()
            if len(parts) == 3:
                word = parts[1]
                popularity = int(parts[2])
                result.append((word, popularity))
        return result

# 调用函数
words_with_popularity = read_txt_to_list(file_path)

connection = pymysql.connect(
    host='localhost',      # 数据库地址
    port=3306,            # 数据库端口
    user='root',          # 用户名
    password='hello',     # 密码
    db='wordtest',        # 数据库名称
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

try:
    with connection.cursor() as cursor:
        # 创建表（如果尚未存在）
        create_table_sql = '''
        CREATE TABLE IF NOT EXISTS words_1 (
                id INT AUTO_INCREMENT PRIMARY KEY,
                word VARCHAR(255) NOT NULL,
                popularity INT NOT NULL
        )
        '''
        cursor.execute(create_table_sql)

        # 插入每个词
        insert_word_sql = '''
        INSERT INTO words_1 (word, popularity) VALUES (%s, %s)
        '''
        for word, popularity in words_with_popularity:
            cursor.execute(insert_word_sql, (word, popularity))

    # 提交到数据库
    connection.commit()

finally:
    connection.close()

print("词条已成功插入数据库！")