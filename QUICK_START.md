# OpenClaw 快速启动指南

## 前置要求

### 系统环境
- Windows 10+ / macOS / Linux
- Node.js 14+ 和 npm 6+
- Java 8+
- MySQL 5.7+
- Redis 5.0+

## 快速启动步骤

### 1. 数据库初始化

```bash
# 使用 MySQL 客户端执行 SQL 脚本
mysql -u root -p < backend/sql/create_table.sql

# 或者在 MySQL 客户端中直接执行 backend/sql/create_table.sql 文件内容
```

### 2. 启动后端服务

```bash
# 进入后端目录
cd backend

# 使用 Maven 启动
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/memory-system-1.0.0.jar
```

后端服务将在 `http://localhost:8080` 启动

### 3. 启动前端服务

```bash
# 进入前端目录
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

前端服务将在 `http://localhost:5173` 启动（或其他可用端口）

### 4. 访问应用

打开浏览器访问：`http://localhost:5173`

## 功能使用说明

### 记忆总结页面
1. 页面顶部显示最新的 3 条记忆总结卡片
2. 下方是完整的历史记录表格
3. 点击"查看详情"可以在模态框中查看完整内容
4. 点击"删除"可以删除记录

### 实时直播页面
1. 中央显示视频流
2. 点击"刷新"按钮重新加载视频
3. 点击"全屏"按钮进入全屏模式
4. 如果视频加载失败，将在 5 秒后自动重连

## 常见问题

### Q: 前端无法连接到后端
**A:** 检查以下几点：
- 后端服务是否正常运行（`http://localhost:8080/api/memory-summary`）
- 检查 `frontend/src/api/index.js` 中的 API 基础 URL 是否正确
- 检查浏览器控制台是否有 CORS 错误

### Q: 数据库连接失败
**A:** 检查以下几点：
- MySQL 服务是否正常运行
- 检查 `backend/src/main/resources/application.yml` 中的数据库配置
- 确保数据库用户名和密码正确
- 确保已执行 SQL 脚本创建数据库和表

### Q: Redis 连接失败
**A:** 检查以下几点：
- Redis 服务是否正常运行
- 检查 `backend/src/main/resources/application.yml` 中的 Redis 配置
- 默认配置为 `localhost:6379`，无密码

### Q: 视频流无法加载
**A:** 检查以下几点：
- 视频流服务是否在 `http://127.0.0.1:8888/stream` 运行
- 检查网络连接
- 查看浏览器控制台错误信息

## 开发模式

### 前端开发
```bash
cd frontend
npm run dev
```
- 支持热更新
- 自动刷新浏览器
- 源代码映射用于调试

### 后端开发
```bash
cd backend
mvn spring-boot:run
```
- 支持热重启（需要配置 DevTools）
- 自动编译

## 生产构建

### 前端构建
```bash
cd frontend
npm run build
```
生成的文件在 `frontend/dist` 目录中

### 后端构建
```bash
cd backend
mvn clean package
```
生成的 JAR 文件在 `backend/target` 目录中

## 项目结构

```
claw/
├── frontend/                 # 前端项目
│   ├── src/
│   │   ├── api/             # API 调用
│   │   ├── components/      # 组件
│   │   ├── router/          # 路由
│   │   ├── utils/           # 工具函数
│   │   ├── views/           # 页面
│   │   ├── App.vue
│   │   ├── main.js
│   │   └── style.scss
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── backend/                  # 后端项目
│   ├── src/
│   │   ├── main/java/com/openclaw/
│   │   │   ├── config/      # 配置
│   │   │   ├── controller/  # 控制器
│   │   │   ├── entity/      # 实体
│   │   │   ├── mapper/      # 数据访问
│   │   │   ├── service/     # 业务逻辑
│   │   │   └── Application.java
│   │   └── main/resources/
│   │       └── application.yml
│   ├── sql/
│   │   └── create_table.sql
│   └── pom.xml
├── prompts/                  # 需求文档
└── PROJECT_SUMMARY.md        # 项目总结
```

## 配置修改

### 修改后端端口
编辑 `backend/src/main/resources/application.yml`：
```yaml
server:
  port: 8080  # 修改为需要的端口
```

### 修改数据库连接
编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/openclaw  # 修改数据库地址
    username: root  # 修改用户名
    password: root  # 修改密码
```

### 修改 Redis 连接
编辑 `backend/src/main/resources/application.yml`：
```yaml
spring:
  redis:
    host: localhost  # 修改 Redis 地址
    port: 6379       # 修改 Redis 端口
    password:        # 修改 Redis 密码
```

### 修改前端 API 地址
编辑 `frontend/src/api/index.js`：
```javascript
const API_BASE_URL = 'http://localhost:8080/api'  // 修改为后端地址
```

## 调试技巧

### 前端调试
- 打开浏览器开发者工具（F12）
- 查看 Console 标签页的错误信息
- 使用 Network 标签页检查 API 请求
- 使用 Vue DevTools 浏览器扩展调试 Vue 组件

### 后端调试
- 查看控制台输出日志
- 在 `application.yml` 中启用 SQL 日志：
  ```yaml
  mybatis-plus:
    configuration:
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  ```
- 使用 IDE 的调试功能设置断点

## 性能优化建议

### 前端
- 使用 `npm run build` 生成生产版本
- 配置 CDN 加速静态资源
- 启用 Gzip 压缩

### 后端
- 调整 MySQL 连接池大小
- 优化 Redis 缓存策略
- 使用数据库索引优化查询

## 安全建议

- 修改默认的数据库密码
- 修改 Redis 密码
- 在生产环境中启用 HTTPS
- 实现用户认证和授权
- 定期备份数据库

## 获取帮助

如遇到问题，请：
1. 查看项目日志
2. 检查浏览器控制台错误
3. 查看后端服务日志
4. 检查数据库和 Redis 连接状态
