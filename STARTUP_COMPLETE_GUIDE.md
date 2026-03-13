# 🚀 OpenClaw 项目启动完整指南

## 当前状态检查

根据你的 IDE 截图，我看到：
- ✅ 后端项目已在 IDE 中打开
- ✅ Application.java 已识别
- ✅ Maven 正在扫描项目
- ⚠️ 可能有 POM 处理问题

---

## 📋 启动前检查清单

### 1️⃣ 环境检查

#### Java 版本
```bash
java -version
# 需要: Java 8 或更高版本
```

#### Maven 版本
```bash
mvn -version
# 需要: Maven 3.6 或更高版本
```

#### MySQL 状态
```bash
# Windows
net start MySQL80

# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

#### Redis 状态
```bash
# Windows (如果已安装)
redis-server

# macOS
brew services start redis

# Linux
sudo systemctl start redis-server
```

### 2️⃣ 数据库初始化

```bash
# 执行 SQL 脚本
mysql -u root -p < backend/sql/create_table.sql

# 或者在 MySQL 客户端中执行
mysql> source backend/sql/create_table.sql;
```

### 3️⃣ 验证数据库连接

```bash
# 测试 MySQL 连接
mysql -u root -p -h localhost -e "SELECT 1;"

# 测试 Redis 连接
redis-cli ping
# 应该返回: PONG
```

---

## 🔧 启动方式

### 方式 1: IDE 直接启动（推荐）

1. 在 IDE 中打开 `Application.java`
2. 右键点击 `Application` 类
3. 选择 "Run 'Application.main()'"
4. 或按快捷键 Shift+F10

### 方式 2: Maven 命令启动

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### 方式 3: 编译后启动

```bash
cd backend
mvn clean package
java -jar target/memory-system-1.0.0.jar
```

---

## ✅ 启动成功标志

启动成功后，你应该看到：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.7.15)

2024-03-13 10:00:00.000  INFO 12345 --- [main] com.openclaw.Application : Starting Application v1.0.0
2024-03-13 10:00:02.000  INFO 12345 --- [main] com.openclaw.Application : Started Application in 2.123 seconds
```

---

## 🧪 验证启动成功

### 1. 检查服务是否运行

```bash
# 检查端口 8080 是否监听
netstat -an | grep 8080

# 或者
lsof -i :8080
```

### 2. 测试 API

```bash
# 使用 curl 测试
curl http://localhost:8080/api/memory-summary

# 应该返回:
# {"code":200,"message":"查询成功","data":[]}
```

### 3. 在浏览器中测试

访问: `http://localhost:8080/api/memory-summary`

应该看到 JSON 响应

---

## ⚠️ 常见错误及解决方案

### 错误 1: 无法连接到 MySQL

**错误信息:**
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**解决方案:**
1. 检查 MySQL 是否启动
2. 检查用户名和密码是否正确
3. 检查数据库是否存在
4. 检查防火墙设置

```bash
# 验证 MySQL 连接
mysql -u root -p -h localhost -e "USE openclaw; SHOW TABLES;"
```

### 错误 2: 无法连接到 Redis

**错误信息:**
```
org.springframework.data.redis.connection.RedisConnectionException
```

**解决方案:**
1. 检查 Redis 是否启动
2. 检查 Redis 密码是否正确
3. 检查 Redis 端口是否正确

```bash
# 验证 Redis 连接
redis-cli -h localhost -p 6379 -a 123456 ping
```

### 错误 3: 端口被占用

**错误信息:**
```
Address already in use: bind
```

**解决方案:**
1. 修改 `application.yml` 中的端口
2. 或者杀死占用该端口的进程

```bash
# 查找占用 8080 端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>
```

### 错误 4: Maven 依赖下载失败

**错误信息:**
```
Failed to read artifact descriptor
```

**解决方案:**
1. 检查网络连接
2. 清除 Maven 缓存
3. 更新 Maven 仓库

```bash
# 清除缓存
mvn clean

# 重新下载依赖
mvn -U clean install
```

### 错误 5: 编译错误

**错误信息:**
```
[ERROR] COMPILATION ERROR
```

**解决方案:**
1. 检查 Java 版本
2. 检查代码是否有语法错误
3. 清除编译缓存

```bash
# 检查 Java 版本
java -version

# 清除编译缓存
mvn clean compile
```

---

## 🔍 调试技巧

### 启用详细日志

编辑 `application.yml`:
```yaml
logging:
  level:
    root: DEBUG
    com.openclaw: DEBUG
    org.springframework.web: DEBUG
```

### 查看 SQL 语句

已在 `application.yml` 中配置:
```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 使用 IDE 调试

1. 在代码中设置断点
2. 以 Debug 模式启动应用
3. 发送请求触发断点

---

## 📊 启动流程图

```
启动应用
  ↓
加载配置文件 (application.yml)
  ↓
初始化数据源 (MySQL)
  ↓
初始化 Redis 连接
  ↓
初始化 MyBatis Plus
  ↓
扫描 Mapper (@MapperScan)
  ↓
启动 Spring 容器
  ↓
启动 Tomcat 服务器
  ↓
监听 8080 端口
  ↓
✅ 启动完成
```

---

## 🎯 启动后的下一步

### 1. 启动前端

```bash
cd frontend
npm install
npm run dev
```

### 2. 访问应用

打开浏览器访问: `http://localhost:5173`

### 3. 测试功能

- 查看记忆总结页面
- 查看实时直播页面
- 测试 API 调用

---

## 📝 配置文件检查

### application.yml 配置

```yaml
# ✅ 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/openclaw
    username: root
    password: mysql_xRXCDS

# ✅ Redis 配置
  redis:
    host: localhost
    port: 6379
    password: 123456

# ✅ 服务器配置
server:
  port: 8080
  servlet:
    context-path: /api
```

### pom.xml 依赖检查

```xml
✅ Spring Boot Starter Web
✅ MyBatis Plus Boot Starter
✅ MySQL Connector
✅ Spring Data Redis
✅ Jedis
✅ Jackson
✅ Lombok
```

---

## 🚨 紧急排查步骤

如果应用无法启动，按以下步骤排查：

1. **检查 Java 环境**
   ```bash
   java -version
   ```

2. **检查 Maven 环境**
   ```bash
   mvn -version
   ```

3. **清除 Maven 缓存**
   ```bash
   mvn clean
   ```

4. **重新编译**
   ```bash
   mvn clean compile
   ```

5. **检查数据库连接**
   ```bash
   mysql -u root -p -h localhost -e "SELECT 1;"
   ```

6. **检查 Redis 连接**
   ```bash
   redis-cli ping
   ```

7. **查看完整错误日志**
   ```bash
   mvn spring-boot:run 2>&1 | tee build.log
   ```

---

## 💡 性能优化建议

### 增加 JVM 内存

```bash
java -Xmx512m -Xms256m -jar target/memory-system-1.0.0.jar
```

### 调整数据库连接池

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

### 调整 Redis 连接池

```yaml
spring:
  redis:
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
```

---

## 📞 获取帮助

如果遇到问题：

1. 查看完整的错误日志
2. 检查 `application.yml` 配置
3. 确保所有服务都已启动
4. 查看本指南的故障排查部分
5. 检查项目文档

---

## ✅ 启动检查清单

- [ ] Java 8+ 已安装
- [ ] Maven 3.6+ 已安装
- [ ] MySQL 已启动
- [ ] Redis 已启动
- [ ] 数据库已初始化
- [ ] application.yml 配置正确
- [ ] 依赖已下载
- [ ] 代码已编译
- [ ] 应用已启动
- [ ] API 可访问

---

**祝你启动顺利！** 🚀

如有问题，请参考本指南的故障排查部分。
