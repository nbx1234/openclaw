# 🔧 Apifox 使用指南 - OpenClaw API 测试

## 什么是 Apifox？

Apifox 是一个 API 文档、调试、测试一体化的工具，类似于 Postman，但功能更强大。

---

## 📥 安装 Apifox

### 方式 1: 官网下载
访问: https://www.apifox.cn/

### 方式 2: 直接下载
- Windows: https://www.apifox.cn/download
- macOS: https://www.apifox.cn/download
- Linux: https://www.apifox.cn/download

---

## 🚀 快速开始

### 第 1 步: 创建项目

1. 打开 Apifox
2. 点击 "新建项目"
3. 输入项目名称: `OpenClaw`
4. 点击 "创建"

### 第 2 步: 创建 API 分组

1. 在左侧菜单中右键
2. 选择 "新建分组"
3. 输入分组名称: `记忆总结 API`
4. 点击 "确定"

### 第 3 步: 创建 API 请求

1. 右键点击分组
2. 选择 "新建 API"
3. 输入 API 名称
4. 点击 "创建"

---

## 📝 API 请求配置

### 1. 获取所有记忆总结

#### 基本信息
```
请求名称: 获取所有记忆总结
请求方法: GET
请求 URL: http://localhost:8080/api/memory-summary
```

#### 配置步骤

1. **设置请求方法**
   - 点击 "GET" 下拉框
   - 选择 "GET"

2. **设置请求 URL**
   - 在 URL 输入框中输入: `http://localhost:8080/api/memory-summary`

3. **发送请求**
   - 点击 "发送" 按钮
   - 查看响应结果

#### 预期响应
```json
{
  "code": 200,
  "message": "查询成功",
  "data": []
}
```

---

### 2. 创建记忆总结

#### 基本信息
```
请求名称: 创建记忆总结
请求方法: POST
请求 URL: http://localhost:8080/api/memory-summary
```

#### 配置步骤

1. **设置请求方法**
   - 选择 "POST"

2. **设置请求 URL**
   - 输入: `http://localhost:8080/api/memory-summary`

3. **设置请求头**
   - 点击 "Headers" 标签
   - 添加: `Content-Type: application/json`

4. **设置请求体**
   - 点击 "Body" 标签
   - 选择 "raw" 和 "JSON"
   - 输入以下内容:

```json
{
  "title": "今天的学习总结",
  "content": "# 学习内容\n\n今天学习了 Vue 3 的基础知识。\n\n## 主要内容\n- 组件化开发\n- 响应式数据\n- 生命周期钩子"
}
```

5. **发送请求**
   - 点击 "发送" 按钮

#### 预期响应
```json
{
  "code": 200,
  "message": "保存成功",
  "data": {
    "id": 1,
    "title": "今天的学习总结",
    "content": "# 学习内容\n\n今天学习了 Vue 3 的基础知识。\n\n## 主要内容\n- 组件化开发\n- 响应式数据\n- 生命周期钩子",
    "createTime": "2024-03-13T10:00:00",
    "updateTime": "2024-03-13T10:00:00"
  }
}
```

---

### 3. 获取单个记忆总结

#### 基本信息
```
请求名称: 获取单个记忆总结
请求方法: GET
请求 URL: http://localhost:8080/api/memory-summary/{id}
```

#### 配置步骤

1. **设置请求方法**
   - 选择 "GET"

2. **设置请求 URL**
   - 输入: `http://localhost:8080/api/memory-summary/1`
   - 或使用路径参数: `http://localhost:8080/api/memory-summary/{{id}}`

3. **设置路径参数（可选）**
   - 点击 "Params" 标签
   - 在 "Path" 部分添加:
     - 参数名: `id`
     - 参数值: `1`

4. **发送请求**
   - 点击 "发送" 按钮

#### 预期响应
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "title": "今天的学习总结",
    "content": "...",
    "createTime": "2024-03-13T10:00:00",
    "updateTime": "2024-03-13T10:00:00"
  }
}
```

---

### 4. 更新记忆总结

#### 基本信息
```
请求名称: 更新记忆总结
请求方法: PUT
请求 URL: http://localhost:8080/api/memory-summary/{id}
```

#### 配置步骤

1. **设置请求方法**
   - 选择 "PUT"

2. **设置请求 URL**
   - 输入: `http://localhost:8080/api/memory-summary/1`

3. **设置请求头**
   - 添加: `Content-Type: application/json`

4. **设置请求体**
   - 选择 "raw" 和 "JSON"
   - 输入:

```json
{
  "title": "更新后的学习总结",
  "content": "# 更新的内容\n\n今天又学习了更多内容。"
}
```

5. **发送请求**
   - 点击 "发送" 按钮

#### 预期响应
```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "更新后的学习总结",
    "content": "# 更新的内容\n\n今天又学习了更多内容。",
    "createTime": "2024-03-13T10:00:00",
    "updateTime": "2024-03-13T10:05:00"
  }
}
```

---

### 5. 删除记忆总结

#### 基本信息
```
请求名称: 删除记忆总结
请求方法: DELETE
请求 URL: http://localhost:8080/api/memory-summary/{id}
```

#### 配置步骤

1. **设置请求方法**
   - 选择 "DELETE"

2. **设置请求 URL**
   - 输入: `http://localhost:8080/api/memory-summary/1`

3. **发送请求**
   - 点击 "发送" 按钮

#### 预期响应
```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

### 6. 分页查询记忆总结

#### 基本信息
```
请求名称: 分页查询记忆总结
请求方法: GET
请求 URL: http://localhost:8080/api/memory-summary/list
```

#### 配置步骤

1. **设置请求方法**
   - 选择 "GET"

2. **设置请求 URL**
   - 输入: `http://localhost:8080/api/memory-summary/list`

3. **设置查询参数**
   - 点击 "Params" 标签
   - 在 "Query" 部分添加:
     - 参数名: `page`, 参数值: `1`
     - 参数名: `size`, 参数值: `10`

4. **发送请求**
   - 点击 "发送" 按钮

#### 预期响应
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "...",
        "content": "...",
        "createTime": "2024-03-13T10:00:00",
        "updateTime": "2024-03-13T10:00:00"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

---

## 🔗 环境变量配置

### 创建环境

1. 点击左上角 "环境" 按钮
2. 点击 "新建环境"
3. 输入环境名称: `本地开发`
4. 添加变量:

```
变量名: baseUrl
变量值: http://localhost:8080/api

变量名: id
变量值: 1
```

### 使用环境变量

在 URL 中使用: `{{baseUrl}}/memory-summary`

---

## 📊 完整 API 集合

### 创建集合

1. 点击 "新建集合"
2. 输入集合名称: `OpenClaw API`
3. 添加以下 API:

| 名称 | 方法 | URL |
|------|------|-----|
| 获取所有 | GET | {{baseUrl}}/memory-summary |
| 创建 | POST | {{baseUrl}}/memory-summary |
| 获取单个 | GET | {{baseUrl}}/memory-summary/{{id}} |
| 更新 | PUT | {{baseUrl}}/memory-summary/{{id}} |
| 删除 | DELETE | {{baseUrl}}/memory-summary/{{id}} |
| 分页查询 | GET | {{baseUrl}}/memory-summary/list |

---

## 🧪 测试用例

### 测试场景 1: 完整的 CRUD 操作

1. **创建** - POST 请求创建新记录
2. **读取** - GET 请求获取记录
3. **更新** - PUT 请求更新记录
4. **删除** - DELETE 请求删除记录

### 测试场景 2: 分页查询

1. 发送 GET 请求到 `/list?page=1&size=10`
2. 验证返回的分页数据

### 测试场景 3: 错误处理

1. 尝试获取不存在的 ID
2. 验证错误响应

---

## 💡 高级功能

### 1. 自动化测试

1. 点击 "测试" 标签
2. 添加测试脚本:

```javascript
// 验证响应状态码
pm.test("状态码是 200", function () {
    pm.response.to.have.status(200);
});

// 验证响应数据
pm.test("响应包含 code 字段", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.code).to.eql(200);
});
```

### 2. 前置脚本

在 "前置脚本" 标签中添加:

```javascript
// 设置时间戳
pm.environment.set("timestamp", new Date().getTime());
```

### 3. 后置脚本

在 "后置脚本" 标签中添加:

```javascript
// 保存响应数据
var jsonData = pm.response.json();
pm.environment.set("id", jsonData.data.id);
```

---

## 🔍 调试技巧

### 1. 查看请求详情

- 点击 "请求" 标签查看完整请求
- 点击 "响应" 标签查看完整响应

### 2. 格式化 JSON

- 响应会自动格式化
- 点击 "Pretty" 查看格式化的 JSON

### 3. 搜索响应

- 在响应中使用 Ctrl+F 搜索

---

## 📚 常用快捷键

| 快捷键 | 功能 |
|--------|------|
| Ctrl+Enter | 发送请求 |
| Ctrl+S | 保存 |
| Ctrl+/ | 注释 |
| Ctrl+Shift+C | 复制 cURL |

---

## 🎯 最佳实践

### 1. 组织 API

- 按功能分组
- 使用有意义的名称
- 添加描述和文档

### 2. 使用环境变量

- 不要硬编码 URL
- 使用 `{{baseUrl}}` 等变量
- 便于切换环境

### 3. 编写测试

- 为每个 API 添加测试
- 验证响应数据
- 检查错误情况

### 4. 保存请求

- 定期保存
- 使用版本控制
- 分享给团队

---

## 🚨 常见问题

### Q: 无法连接到后端？
A: 
1. 检查后端是否启动
2. 检查 URL 是否正确
3. 检查防火墙设置

### Q: 请求超时？
A:
1. 增加超时时间
2. 检查网络连接
3. 检查后端性能

### Q: 响应格式错误？
A:
1. 检查 Content-Type 是否为 application/json
2. 检查请求体格式
3. 查看后端日志

---

## 📖 完整示例

### 示例 1: 创建并查询

```
1. POST http://localhost:8080/api/memory-summary
   Body: {"title": "测试", "content": "内容"}
   
2. 获取返回的 id
   
3. GET http://localhost:8080/api/memory-summary/{id}
   
4. 验证数据
```

### 示例 2: 更新并删除

```
1. PUT http://localhost:8080/api/memory-summary/{id}
   Body: {"title": "更新", "content": "新内容"}
   
2. DELETE http://localhost:8080/api/memory-summary/{id}
   
3. 验证删除成功
```

---

## 🎓 学习资源

- Apifox 官方文档: https://www.apifox.cn/help/
- API 测试最佳实践: https://www.apifox.cn/blog/
- 视频教程: https://www.apifox.cn/video/

---

## ✅ 检查清单

- [ ] 安装 Apifox
- [ ] 创建项目
- [ ] 创建分组
- [ ] 配置环境变量
- [ ] 创建 GET 请求
- [ ] 创建 POST 请求
- [ ] 创建 PUT 请求
- [ ] 创建 DELETE 请求
- [ ] 测试所有 API
- [ ] 保存项目

---

**祝你使用 Apifox 愉快！** 🚀

如有问题，请参考 Apifox 官方文档或本指南。
