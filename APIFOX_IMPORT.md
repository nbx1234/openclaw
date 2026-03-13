# OpenClaw API 集合 - Apifox 导入指南

## 快速导入步骤

### 方式 1: 手动创建（推荐新手）

按照 `APIFOX_GUIDE.md` 中的步骤手动创建 API 请求。

### 方式 2: 导入 JSON（快速）

1. 复制下面的 JSON 内容
2. 在 Apifox 中点击 "导入"
3. 选择 "从文本导入"
4. 粘贴 JSON 内容
5. 点击 "导入"

---

## 📋 API 集合 JSON

```json
{
  "openapi": "3.0.0",
  "info": {
    "title": "OpenClaw 记忆系统 API",
    "version": "1.0.0",
    "description": "OpenClaw 记忆系统的完整 API 文档"
  },
  "servers": [
    {
      "url": "http://localhost:8080/api",
      "description": "本地开发环境"
    }
  ],
  "paths": {
    "/memory-summary": {
      "get": {
        "summary": "获取所有记忆总结",
        "operationId": "getAllMemorySummaries",
        "tags": ["记忆总结"],
        "responses": {
          "200": {
            "description": "成功获取所有记忆总结",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "查询成功"
                    },
                    "data": {
                      "type": "array",
                      "items": {
                        "$ref": "#/components/schemas/MemorySummary"
                      }
                    }
                  }
                }
              }
            }
          }
        }
      },
      "post": {
        "summary": "创建记忆总结",
        "operationId": "createMemorySummary",
        "tags": ["记忆总结"],
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "title": {
                    "type": "string",
                    "example": "今天的学习总结"
                  },
                  "content": {
                    "type": "string",
                    "example": "# 学习内容\n\n今天学习了 Vue 3"
                  }
                },
                "required": ["title", "content"]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "成功创建记忆总结",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "保存成功"
                    },
                    "data": {
                      "$ref": "#/components/schemas/MemorySummary"
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    "/memory-summary/{id}": {
      "get": {
        "summary": "获取单个记忆总结",
        "operationId": "getMemorySummaryById",
        "tags": ["记忆总结"],
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer"
            },
            "example": 1
          }
        ],
        "responses": {
          "200": {
            "description": "成功获取记忆总结",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "查询成功"
                    },
                    "data": {
                      "$ref": "#/components/schemas/MemorySummary"
                    }
                  }
                }
              }
            }
          }
        }
      },
      "put": {
        "summary": "更新记忆总结",
        "operationId": "updateMemorySummary",
        "tags": ["记忆总结"],
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer"
            },
            "example": 1
          }
        ],
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "title": {
                    "type": "string",
                    "example": "更新后的标题"
                  },
                  "content": {
                    "type": "string",
                    "example": "# 更新的内容"
                  }
                }
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "成功更新记忆总结",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "更新成功"
                    },
                    "data": {
                      "$ref": "#/components/schemas/MemorySummary"
                    }
                  }
                }
              }
            }
          }
        }
      },
      "delete": {
        "summary": "删除记忆总结",
        "operationId": "deleteMemorySummary",
        "tags": ["记忆总结"],
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "required": true,
            "schema": {
              "type": "integer"
            },
            "example": 1
          }
        ],
        "responses": {
          "200": {
            "description": "成功删除记忆总结",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "删除成功"
                    },
                    "data": {
                      "type": "null"
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    "/memory-summary/list": {
      "get": {
        "summary": "分页查询记忆总结",
        "operationId": "listMemorySummaries",
        "tags": ["记忆总结"],
        "parameters": [
          {
            "name": "page",
            "in": "query",
            "schema": {
              "type": "integer",
              "default": 1
            },
            "example": 1
          },
          {
            "name": "size",
            "in": "query",
            "schema": {
              "type": "integer",
              "default": 10
            },
            "example": 10
          }
        ],
        "responses": {
          "200": {
            "description": "成功获取分页数据",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "code": {
                      "type": "integer",
                      "example": 200
                    },
                    "message": {
                      "type": "string",
                      "example": "查询成功"
                    },
                    "data": {
                      "type": "object",
                      "properties": {
                        "records": {
                          "type": "array",
                          "items": {
                            "$ref": "#/components/schemas/MemorySummary"
                          }
                        },
                        "total": {
                          "type": "integer",
                          "example": 10
                        },
                        "size": {
                          "type": "integer",
                          "example": 10
                        },
                        "current": {
                          "type": "integer",
                          "example": 1
                        },
                        "pages": {
                          "type": "integer",
                          "example": 1
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "MemorySummary": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "example": 1
          },
          "title": {
            "type": "string",
            "example": "今天的学习总结"
          },
          "content": {
            "type": "string",
            "example": "# 学习内容\n\n今天学习了 Vue 3 的基础知识。"
          },
          "createTime": {
            "type": "string",
            "format": "date-time",
            "example": "2024-03-13T10:00:00"
          },
          "updateTime": {
            "type": "string",
            "format": "date-time",
            "example": "2024-03-13T10:00:00"
          }
        }
      }
    }
  }
}
```

---

## 🔧 导入步骤详解

### 步骤 1: 打开 Apifox

1. 启动 Apifox 应用
2. 登录或创建账户

### 步骤 2: 创建新项目

1. 点击 "新建项目"
2. 输入项目名称: `OpenClaw`
3. 点击 "创建"

### 步骤 3: 导入 API

1. 点击左上角 "导入"
2. 选择 "从 OpenAPI 导入"
3. 选择 "从文本导入"
4. 复制上面的 JSON 内容
5. 粘贴到文本框
6. 点击 "导入"

### 步骤 4: 配置环境

1. 点击 "环境" 按钮
2. 创建新环境: `本地开发`
3. 添加变量:
   - `baseUrl`: `http://localhost:8080/api`
   - `id`: `1`

### 步骤 5: 测试 API

1. 点击任意 API 请求
2. 点击 "发送" 按钮
3. 查看响应结果

---

## 📊 API 概览

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | /memory-summary | 获取所有 |
| POST | /memory-summary | 创建 |
| GET | /memory-summary/{id} | 获取单个 |
| PUT | /memory-summary/{id} | 更新 |
| DELETE | /memory-summary/{id} | 删除 |
| GET | /memory-summary/list | 分页查询 |

---

## 🧪 测试流程

### 完整测试流程

1. **创建** - POST /memory-summary
   ```json
   {
     "title": "测试标题",
     "content": "测试内容"
   }
   ```

2. **查询** - GET /memory-summary
   - 验证返回的数据

3. **获取单个** - GET /memory-summary/1
   - 验证返回的数据

4. **更新** - PUT /memory-summary/1
   ```json
   {
     "title": "更新标题",
     "content": "更新内容"
   }
   ```

5. **删除** - DELETE /memory-summary/1
   - 验证删除成功

---

## 💡 使用技巧

### 1. 使用环境变量

在 URL 中使用: `{{baseUrl}}/memory-summary/{{id}}`

### 2. 保存请求

每个请求都会自动保存

### 3. 查看历史

点击 "历史" 查看之前的请求

### 4. 导出结果

右键点击响应，选择 "导出"

---

## ✅ 验证清单

- [ ] Apifox 已安装
- [ ] 项目已创建
- [ ] API 已导入
- [ ] 环境已配置
- [ ] 后端已启动
- [ ] 测试成功

---

**祝你使用 Apifox 愉快！** 🚀
