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

mysqldump -u root -p seat > seat.sql
```

- [ ]  座位状态：使用中、维修中、以预约、空闲
- [ ]  座位查询
- [ ]  预约某时间段
- [ ]  退出使用状态
- [ ]  统计功能（失约，使用频率统计，用户来频率，用户平均时长统计，排行榜）