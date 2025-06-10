# 软件课程设计

状态: 进行中
截止日期: 06/20/2025

# 前端

Vue 2

```bash
# node.js 环境还在
node -v
npm -v

# npm install -g @vue/cli

npm install
npm run serve
```

# 后端

spring boot

```bash
# java 环境
java -version
# 安装 mavnen
mvn -v
# 配置数据库
# 运行
mvn spring-boot:run

```

在 Windows 上安装 Maven 步骤如下：

---

## ✅ 一、下载 Maven

1. 打开官网下载地址：[https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. 找到 **Binary zip archive**（不要下载 source）：
    
    > 例如：apache-maven-3.9.6-bin.zip
    > 
3. 下载后解压到一个目录，比如：
    
    ```
    C:\Program Files\Apache\Maven\apache-maven-3.9.6
    ```
    

---

## ✅ 二、配置环境变量

### 1. 设置 `MAVEN_HOME` 环境变量

- **右键此电脑 → 属性 → 高级系统设置 → 环境变量**
- 新建系统变量：
    
    ```
    变量名：MAVEN_HOME
    变量值：C:\Program Files\Apache\Maven\apache-maven-3.9.6
    ```
    

### 2. 修改 `Path` 变量

在系统变量中的 `Path` 里添加：

```
%MAVEN_HOME%\bin
```

---

## ✅ 三、验证安装

打开命令行窗口（Win + R → 输入 `cmd` 回车）：

```bash
mvn -v

```

如果显示 Maven 版本信息，例如：

```
Apache Maven 3.9.6
Java version: 17.0.9
Default locale: zh_CN, platform encoding: UTF-8

```

说明安装成功 ✅

---

## ⚠️ 注意事项

- Maven 依赖 Java 环境，确保你已经安装了 JDK，并配置了 `JAVA_HOME` 和 `Path`
    
    ```bash
    java -version
    ```
    

---

在国内使用 `mvn` 下载依赖慢，主要是因为 Maven 默认使用的是国外的中央仓库（`https://repo.maven.apache.org`），访问速度慢。你可以通过 **配置国内镜像源** 来极大加速依赖下

---

## ✅ 解决方案：配置 Maven 使用国内镜像

### 🛠 步骤一：找到 `settings.xml`

默认路径：

```bash
C:\Users\<你的用户名>\.m2\settings.xml
```

如果没有，就去 Maven 安装目录下：

```bash
<你的 Maven 安装目录>\conf\settings.xml
```

推荐把改动写在用户目录（`~/.m2/settings.xml`）中，不改动全局配置。

---

### 🛠 步骤二：添加阿里云等国内镜像

在 `<mirrors>` 标签中添加以下内容（如果没有 `<mirrors>` 节点，就自己加）：

```xml
<mirrors>
  <mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>

```

---

### 🧪 步骤三：验证效果

修改完后，重新运行：

```bash
mvn clean install
```

---

# 数据库

1. 安装 MySQL
2. 配置数据库

```bash
mysql -h localhost -P 3306 -u root -p

# 设置密码, 或者修改后端数据源文件
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;

CREATE DATABASE seat CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE seat;

-- 用户表
CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(255) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `nickname` varchar(255) COMMENT '昵称',
    `email` varchar(255) COMMENT '邮箱',
    `phone` varchar(20) COMMENT '电话',
    `address` varchar(255) COMMENT '地址',
    `create_time` datetime COMMENT '创建时间',
    `avatar_url` varchar(255) COMMENT '头像',
    `role` varchar(50) COMMENT '角色',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `role` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `description` varchar(255) COMMENT '角色描述',
    `flag` varchar(50) NOT NULL COMMENT '唯一标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_flag` (`flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE `menu` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '菜单名称',
    `path` varchar(255) COMMENT '菜单路径',
    `icon` varchar(255) COMMENT '图标',
    `description` varchar(255) COMMENT '描述',
    `pid` int COMMENT '父菜单ID',
    `page_path` varchar(255) COMMENT '页面路径',
    `sort_num` varchar(255) COMMENT '排序',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 角色菜单关联表
CREATE TABLE `role_menu` (
    `role_id` int NOT NULL COMMENT '角色ID',
    `menu_id` int NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 字典表
CREATE TABLE `dict` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '字典名称',
    `value` varchar(255) NOT NULL COMMENT '字典值',
    `type` varchar(50) NOT NULL COMMENT '字典类型',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- 文件表
CREATE TABLE `file` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '文件名',
    `type` varchar(50) COMMENT '文件类型',
    `size` bigint COMMENT '文件大小',
    `url` varchar(255) COMMENT '文件URL',
    `md5` varchar(255) COMMENT '文件MD5',
    `is_delete` tinyint(1) DEFAULT 0 COMMENT '是否删除',
    `enable` tinyint(1) DEFAULT 1 COMMENT '是否启用',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 座位表
CREATE TABLE `seat` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(50) NOT NULL COMMENT '名称',
    `info` varchar(255) COMMENT '简介',
    `img` varchar(255) COMMENT '座位照片',
    `user_id` int COMMENT '用户id',
    `user_name` varchar(255) COMMENT '用户名称',
    `state` varchar(20) COMMENT '座位状态',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

-- 通知表
CREATE TABLE `notice` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '通知标题',
    `content` text COMMENT '通知内容',
    `time` varchar(255) COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 插入座位数据
INSERT INTO `seat` (`name`, `info`, `img`, `user_id`, `user_name`, `state`) VALUES
('A区-01', '靠窗座位，视野好', '/files/seat1.jpg', NULL, NULL, '空闲'),
('A区-02', '靠近电源插座', '/files/seat2.jpg', NULL, NULL, '空闲'),
('A区-03', '安静区域', '/files/seat3.jpg', 1, '张三', '使用中'),
('B区-01', '靠近空调', '/files/seat4.jpg', 2, '李四', '已预约'),
('B区-02', '靠窗座位', '/files/seat5.jpg', NULL, NULL, '空闲'),
('B区-03', '靠近打印机', '/files/seat6.jpg', 3, '王五', '使用中'),
('C区-01', '安静区域', '/files/seat7.jpg', NULL, NULL, '空闲'),
('C区-02', '靠近电源插座', '/files/seat8.jpg', 4, '赵六', '已预约'),
('C区-03', '靠窗座位', '/files/seat9.jpg', NULL, NULL, '空闲'),
('D区-01', '靠近空调', '/files/seat10.jpg', 5, '钱七', '使用中');

-- 插入通知数据
INSERT INTO `notice` (`name`, `content`, `time`) VALUES
('使用规则提醒', '请勿在座位上放置个人物品超过30分钟，否则系统将自动释放座位。', '2025-06-03 13:00:00'),
('系统升级通知', '系统将于本周日进行升级，新增座位使用统计功能。', '2025-06-03 14:00:00'),
('节假日安排', '图书馆开放时间调整为8:00-20:00。', '2025-06-03 09:00:00');

```

---

## 🐧 在 Ubuntu/Debian 上安装 MySQL（WSL）

### 1. 更新软件包索引

打开终端，执行以下命令以确保软件包列表是最新的：

```bash
sudo apt update

```

### 2. 安装 MySQL 服务器

执行以下命令安装 MySQL 服务器：

```bash
sudo apt install mysql-server

sudo mysqld &

mysql -h localhost -P 3306 -u root -p

# 设置密码
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;

exit
sudo pkill mysqld
sudo mysqld_safe &

CREATE DATABASE seat CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE seat;

-- 用户表
CREATE TABLE `user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(255) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `nickname` varchar(255) COMMENT '昵称',
    `email` varchar(255) COMMENT '邮箱',
    `phone` varchar(20) COMMENT '电话',
    `address` varchar(255) COMMENT '地址',
    `create_time` datetime COMMENT '创建时间',
    `avatar_url` varchar(255) COMMENT '头像',
    `role` varchar(50) COMMENT '角色',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE `role` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `description` varchar(255) COMMENT '角色描述',
    `flag` varchar(50) NOT NULL COMMENT '唯一标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_flag` (`flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表
CREATE TABLE `menu` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '菜单名称',
    `path` varchar(255) COMMENT '菜单路径',
    `icon` varchar(255) COMMENT '图标',
    `description` varchar(255) COMMENT '描述',
    `pid` int COMMENT '父菜单ID',
    `page_path` varchar(255) COMMENT '页面路径',
    `sort_num` varchar(255) COMMENT '排序',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 角色菜单关联表
CREATE TABLE `role_menu` (
    `role_id` int NOT NULL COMMENT '角色ID',
    `menu_id` int NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 字典表
CREATE TABLE `dict` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL COMMENT '字典名称',
    `value` varchar(255) NOT NULL COMMENT '字典值',
    `type` varchar(50) NOT NULL COMMENT '字典类型',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- 文件表
CREATE TABLE `file` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '文件名',
    `type` varchar(50) COMMENT '文件类型',
    `size` bigint COMMENT '文件大小',
    `url` varchar(255) COMMENT '文件URL',
    `md5` varchar(255) COMMENT '文件MD5',
    `is_delete` tinyint(1) DEFAULT 0 COMMENT '是否删除',
    `enable` tinyint(1) DEFAULT 1 COMMENT '是否启用',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 座位表
CREATE TABLE `seat` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(50) NOT NULL COMMENT '名称',
    `info` varchar(255) COMMENT '简介',
    `img` varchar(255) COMMENT '座位照片',
    `user_id` int COMMENT '用户id',
    `user_name` varchar(255) COMMENT '用户名称',
    `state` varchar(20) COMMENT '座位状态',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位表';

-- 通知表
CREATE TABLE `notice` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '通知标题',
    `content` text COMMENT '通知内容',
    `time` varchar(255) COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

mysqldump -u root -p seat > seat.sql
```

```java
-- 插入座位数据
INSERT INTO `seat` (`name`, `info`, `img`, `user_id`, `user_name`, `state`) VALUES
('A区-01', '靠窗座位，视野好', '/files/seat1.jpg', NULL, NULL, '空闲'),
('A区-02', '靠近电源插座', '/files/seat2.jpg', NULL, NULL, '空闲'),
('A区-03', '安静区域', '/files/seat3.jpg', 1, '张三', '使用中'),
('B区-01', '靠近空调', '/files/seat4.jpg', 2, '李四', '已预约'),
('B区-02', '靠窗座位', '/files/seat5.jpg', NULL, NULL, '空闲'),
('B区-03', '靠近打印机', '/files/seat6.jpg', 3, '王五', '使用中'),
('C区-01', '安静区域', '/files/seat7.jpg', NULL, NULL, '空闲'),
('C区-02', '靠近电源插座', '/files/seat8.jpg', 4, '赵六', '已预约'),
('C区-03', '靠窗座位', '/files/seat9.jpg', NULL, NULL, '空闲'),
('D区-01', '靠近空调', '/files/seat10.jpg', 5, '钱七', '使用中');

-- 插入通知数据
INSERT INTO `notice` (`name`, `content`, `time`) VALUES
('使用规则提醒', '请勿在座位上放置个人物品超过30分钟，否则系统将自动释放座位。', '2025-06-03 13:00:00'),
('系统升级通知', '系统将于本周日进行升级，新增座位使用统计功能。', '2025-06-03 14:00:00'),
('节假日安排', '图书馆开放时间调整为8:00-20:00。', '2025-06-03 09:00:00');
```

- [ ]  座位状态：使用中、维修中、以预约、空闲
- [ ]  座位查询
- [ ]  预约某时间段
- [ ]  退出使用状态
- [ ]  统计功能（失约，使用频率统计，用户来频率，用户平均时长统计，排行榜）