# 后端启动故障排查指南

## 启动前检查清单

### 1. 检查 Java 环境
```bash
java -version
```
需要 Java 8 或更高版本

### 2. 检查 Maven 环境
```bash
mvn -version
```
需要 Maven 3.6+

### 3. 检查 MySQL 服务
```bash
# Windows
net start MySQL80

# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

### 4. 检查 Redis 服务
```bash
# Windows - 如果已安装
redis-server

# macOS
brew services start redis

# Linux
sudo systemctl start redis-server
```

### 5. 初始化数据库
```bash
mysql -u root -p < backend/sql/create_table.sql
```

## 启动步骤

### 方式 1：使用 Maven 直接启动（推荐开发）
```bash
cd backend
mvn spring-boot:run
```

### 方式 2：先编译再启动
```bash
cd backend
mvn clean package
java -jar target/memory-system-1.0.0.jar
```

### 方式 3：使用 IDE 启动
- 在 IDE 中打开 `Application.java`
- 右键选择 "Run" 或按 Shift+F10

## 常见错误及解决方案

### 错误 1: 无法连接到 MySQL
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**解决方案：**
1. 检查 MySQL 是否启动
2. 检查 `application.yml` 中的数据库配置
3. 确保数据库用户名和密码正确
4. 确保已执行 SQL 脚本创建数据库

### 错误 2: 无法连接到 Redis
```
org.springframework.data.redis.connection.RedisConnectionException
```

**解决方案：**
1. 检查 Redis 是否启动
2. 检查 `application.yml` 中的 Redis 配置
3. 确保 Redis 密码正确
4. 如果没有 Redis，可以注释掉 Redis 相关代码

### 错误 3: 端口被占用
```
Address already in use: bind
```

**解决方案：**
1. 修改 `application.yml` 中的 `server.port`
2. 或者杀死占用该端口的进程

### 错误 4: 依赖下载失败
```
Failed to read artifact descriptor
```

**解决方案：**
1. 检查网络连接
2. 清除 Maven 缓存：`mvn clean`
3. 更新 Maven 仓库：`mvn -U clean install`

### 错误 5: 编译错误
```
[ERROR] COMPILATION ERROR
```

**解决方案：**
1. 检查 Java 版本是否为 1.8+
2. 检查代码是否有语法错误
3. 清除编译缓存：`mvn clean compile`

## 验证启动成功

启动成功后，应该看到类似的日志：
```
2024-03-13 10:00:00.000  INFO 12345 --- [main] com.openclaw.Application : Started Application in 5.123 seconds (JVM running for 5.456)
```

然后访问以下 URL 验证：
```
http://localhost:8080/api/memory-summary
```

应该返回：
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

## 调试技巧

### 启用详细日志
在 `application.yml` 中添加：
```yaml
logging:
  level:
    root: DEBUG
    com.openclaw: DEBUG
```

### 查看 SQL 语句
在 `application.yml` 中已配置：
```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 使用 IDE 调试
1. 在代码中设置断点
2. 以 Debug 模式启动应用
3. 发送请求触发断点

## 性能优化

### 增加 JVM 内存
```bash
java -Xmx512m -Xms256m -jar target/memory-system-1.0.0.jar
```

### 调整数据库连接池
在 `application.yml` 中修改：
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

## 生产部署

### 创建 systemd 服务（Linux）
```bash
sudo nano /etc/systemd/system/openclaw.service
```

```ini
[Unit]
Description=OpenClaw Memory System
After=network.target

[Service]
Type=simple
User=openclaw
WorkingDirectory=/opt/openclaw
ExecStart=/usr/bin/java -jar /opt/openclaw/memory-system-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
sudo systemctl start openclaw
sudo systemctl enable openclaw
```

## 监控和日志

### 查看实时日志
```bash
tail -f nohup.out
```

### 后台运行
```bash
nohup java -jar target/memory-system-1.0.0.jar > nohup.out 2>&1 &
```

### 查看进程
```bash
ps aux | grep java
```

## 获取帮助

如果以上方案都不能解决问题，请：
1. 查看完整的错误日志
2. 检查 `application.yml` 配置
3. 确保所有依赖都已正确安装
4. 尝试清除 Maven 缓存重新构建
