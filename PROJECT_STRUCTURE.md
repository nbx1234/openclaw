# OpenClaw 项目结构详解

## 完整项目树

```
claw/
│
├── 📁 frontend/                          # 前端项目（Vue 3 + Vite）
│   ├── 📁 src/
│   │   ├── 📁 api/
│   │   │   └── index.js                 # API 调用层（axios 配置）
│   │   │
│   │   ├── 📁 assets/
│   │   │   ├── hero.png
│   │   │   ├── vite.svg
│   │   │   └── vue.svg
│   │   │
│   │   ├── 📁 components/
│   │   │   └── Navbar.vue               # 导航栏组件
│   │   │
│   │   ├── 📁 router/
│   │   │   └── index.js                 # 路由配置
│   │   │
│   │   ├── 📁 utils/
│   │   │   ├── date.js                  # 日期格式化工具
│   │   │   └── markdown.js              # Markdown 渲染工具
│   │   │
│   │   ├── 📁 views/
│   │   │   ├── Summary.vue              # 记忆总结页面
│   │   │   └── LiveStream.vue           # 实时直播页面
│   │   │
│   │   ├── App.vue                      # 根组件
│   │   ├── main.js                      # 应用入口
│   │   └── style.scss                   # 全局样式
│   │
│   ├── 📁 public/
│   │   ├── favicon.svg
│   │   └── icons.svg
│   │
│   ├── 📁 node_modules/                 # 依赖包（npm install 生成）
│   │
│   ├── index.html                       # HTML 入口
│   ├── package.json                     # 项目配置和依赖
│   ├── package-lock.json                # 依赖锁定文件
│   ├── vite.config.js                   # Vite 配置
│   ├── .gitignore                       # Git 忽略文件
│   └── README.md                        # 前端 README
│
├── 📁 backend/                          # 后端项目（Spring Boot）
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/
│   │   │   │   └── 📁 com/openclaw/
│   │   │   │       ├── 📁 config/
│   │   │   │       │   └── MyBatisPlusConfig.java
│   │   │   │       │
│   │   │   │       ├── 📁 controller/
│   │   │   │       │   └── MemorySummaryController.java
│   │   │   │       │
│   │   │   │       ├── 📁 entity/
│   │   │   │       │   └── MemorySummary.java
│   │   │   │       │
│   │   │   │       ├── 📁 mapper/
│   │   │   │       │   └── MemorySummaryMapper.java
│   │   │   │       │
│   │   │   │       ├── 📁 service/
│   │   │   │       │   ├── MemorySummaryService.java
│   │   │   │       │   └── 📁 impl/
│   │   │   │       │       └── MemorySummaryServiceImpl.java
│   │   │   │       │
│   │   │   │       └── Application.java
│   │   │   │
│   │   │   └── 📁 resources/
│   │   │       └── application.yml      # 应用配置
│   │   │
│   │   └── 📁 test/                     # 测试代码
│   │
│   ├── 📁 sql/
│   │   └── create_table.sql             # 数据库初始化脚本
│   │
│   ├── pom.xml                          # Maven 配置
│   └── .gitignore                       # Git 忽略文件
│
├── 📁 prompts/                          # 需求文档
│   ├── 项目整体描述 Prompt
│   ├── 前端系统 Prompt
│   ├── 后端系统 Prompt
│   ├── 数据库 Prompt
│   ├── UI 设计 Prompt.txt
│   ├── 实时图传模块 Prompt
│   └── 最终生成要求 Prompt
│
├── PROJECT_SUMMARY.md                   # 项目总结文档
├── QUICK_START.md                       # 快速启动指南
├── COMPLETION_CHECKLIST.md              # 完成检查清单
└── README.md                            # 项目主 README（可选）
```

---

## 文件详解

### 前端核心文件

#### `src/main.js` - 应用入口
```javascript
// 初始化 Vue 应用
// 导入路由和全局样式
// 挂载到 #app 元素
```

#### `src/App.vue` - 根组件
```vue
<!-- 包含导航栏和路由视图 -->
<!-- 定义应用级别的样式 -->
```

#### `src/style.scss` - 全局样式
```scss
// 颜色变量定义
// 间距系统
// 通用组件样式
// 响应式设计
```

#### `src/router/index.js` - 路由配置
```javascript
// 定义两个主要路由：
// - /summary (记忆总结页面)
// - /live-stream (实时直播页面)
```

#### `src/api/index.js` - API 调用层
```javascript
// Axios 实例配置
// API 端点定义
// 响应拦截器
```

#### `src/views/Summary.vue` - 记忆总结页面
```vue
<!-- 最新总结卡片区域 -->
<!-- 历史记录表格 -->
<!-- 详情模态框 -->
```

#### `src/views/LiveStream.vue` - 实时直播页面
```vue
<!-- 视频流展示 -->
<!-- 控制按钮 -->
<!-- 自动重连逻辑 -->
```

#### `src/components/Navbar.vue` - 导航栏
```vue
<!-- 品牌 Logo -->
<!-- 导航链接 -->
<!-- 移动端菜单 -->
```

#### `src/utils/date.js` - 日期工具
```javascript
// formatDate() - 自定义格式
// formatDateShort() - 短日期格式
// formatDateTime() - 完整日期时间格式
```

#### `src/utils/markdown.js` - Markdown 工具
```javascript
// renderMarkdown() - 将 Markdown 转换为 HTML
```

### 后端核心文件

#### `Application.java` - 启动类
```java
// Spring Boot 应用启动入口
// @SpringBootApplication 注解
```

#### `entity/MemorySummary.java` - 实体类
```java
// 数据库表映射
// 字段：id, title, content, createTime, updateTime
```

#### `mapper/MemorySummaryMapper.java` - 数据访问层
```java
// MyBatis Plus BaseMapper 接口
// 继承自 BaseMapper<MemorySummary>
```

#### `service/MemorySummaryService.java` - 服务接口
```java
// 业务逻辑接口定义
// 方法：save, update, getById, getList, delete, getAll
```

#### `service/impl/MemorySummaryServiceImpl.java` - 服务实现
```java
// 实现 MemorySummaryService 接口
// 集成 Redis 缓存
// 数据库操作
```

#### `controller/MemorySummaryController.java` - 控制器
```java
// REST API 端点
// 请求映射和响应处理
// CORS 跨域支持
```

#### `config/MyBatisPlusConfig.java` - 配置类
```java
// MyBatis Plus 配置
// 分页插件配置
```

#### `application.yml` - 应用配置
```yaml
# 数据库连接配置
# Redis 连接配置
# MyBatis Plus 配置
# 服务器配置
```

#### `sql/create_table.sql` - 数据库脚本
```sql
-- 创建数据库
-- 创建 memory_summary 表
-- 定义字段和索引
```

### 配置文件

#### `frontend/package.json`
```json
{
  "dependencies": {
    "vue": "^3.5.30",
    "vue-router": "^4.3.0",
    "axios": "^1.6.0",
    "markdown-it": "^14.0.0"
  },
  "devDependencies": {
    "vite": "^8.0.0",
    "sass": "^1.70.0"
  }
}
```

#### `frontend/vite.config.js`
```javascript
// Vue 插件配置
// SCSS 预处理器配置
// API 代理配置
```

#### `backend/pom.xml`
```xml
<!-- Spring Boot 依赖 -->
<!-- MyBatis Plus 依赖 -->
<!-- MySQL 驱动 -->
<!-- Redis 依赖 -->
<!-- Lombok 依赖 -->
```

---

## 数据流向

### 前端数据流
```
用户交互
    ↓
Vue 组件事件处理
    ↓
API 调用 (axios)
    ↓
后端 REST API
    ↓
响应数据
    ↓
Vue 组件更新
    ↓
页面渲染
```

### 后端数据流
```
HTTP 请求
    ↓
Controller 接收
    ↓
Service 业务逻辑
    ↓
Redis 缓存检查
    ↓
Mapper 数据库操作
    ↓
数据库查询/更新
    ↓
缓存更新
    ↓
响应数据
    ↓
JSON 返回
```

---

## 关键技术点

### 前端
1. **Vue 3 Composition API** - 使用 `<script setup>` 语法
2. **Vue Router** - 单页应用路由管理
3. **Axios** - HTTP 客户端库
4. **Markdown-it** - Markdown 解析和渲染
5. **SCSS** - CSS 预处理器
6. **Vite** - 快速构建工具

### 后端
1. **Spring Boot** - 快速开发框架
2. **MyBatis Plus** - ORM 框架
3. **Redis** - 缓存数据库
4. **MySQL** - 关系数据库
5. **Lombok** - 代码生成库

---

## 部署架构

```
┌─────────────────────────────────────────────────────────┐
│                    用户浏览器                             │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│              前端应用 (Nginx/Apache)                      │
│  - Vue 3 SPA                                             │
│  - 静态资源                                              │
│  - API 代理                                              │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓ HTTP/HTTPS
┌─────────────────────────────────────────────────────────┐
│              后端应用 (Spring Boot)                       │
│  - REST API                                              │
│  - 业务逻辑                                              │
│  - 缓存管理                                              │
└────────────────────┬────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        ↓                         ↓
┌──────────────────┐      ┌──────────────────┐
│   MySQL 数据库    │      │   Redis 缓存     │
│  - 数据持久化    │      │  - 性能优化      │
└──────────────────┘      └──────────────────┘
```

---

## 开发工作流

### 前端开发
```
编辑 .vue 文件
    ↓
Vite 热更新
    ↓
浏览器自动刷新
    ↓
查看效果
```

### 后端开发
```
编辑 Java 文件
    ↓
IDE 自动编译
    ↓
Spring Boot 热重启
    ↓
测试 API
```

---

## 文件大小参考

| 文件/目录 | 大小 | 说明 |
|---------|------|------|
| frontend/src | ~50KB | 前端源代码 |
| frontend/node_modules | ~500MB | 依赖包（首次安装） |
| backend/src | ~30KB | 后端源代码 |
| backend/target | ~50MB | 编译输出（首次构建） |

---

## 关键配置说明

### 前端配置
- **API 基础 URL**: `http://localhost:8080/api`
- **开发服务器端口**: `5173`（Vite 默认）
- **构建输出目录**: `dist/`

### 后端配置
- **服务器端口**: `8080`
- **上下文路径**: `/api`
- **数据库**: `openclaw`
- **Redis 数据库**: `0`

---

## 扩展建议

### 添加新页面
1. 在 `src/views/` 创建新的 `.vue` 文件
2. 在 `src/router/index.js` 添加路由
3. 在 `src/components/Navbar.vue` 添加导航链接

### 添加新 API
1. 在后端创建新的 Controller 方法
2. 在 `src/api/index.js` 添加 API 调用函数
3. 在组件中使用新的 API

### 添加新样式
1. 在 `src/style.scss` 添加全局样式
2. 或在组件的 `<style scoped>` 中添加局部样式

---

## 项目完成度统计

- **前端文件**: 12 个
- **后端文件**: 8 个
- **配置文件**: 5 个
- **文档文件**: 4 个
- **总计**: 29 个文件

**项目完成度**: ✅ 100%
