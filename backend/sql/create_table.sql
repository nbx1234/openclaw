CREATE DATABASE IF NOT EXISTS openclaw DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE openclaw;

CREATE TABLE IF NOT EXISTS memory_summary (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content LONGTEXT COMMENT '内容',
    memory_type VARCHAR(64) DEFAULT 'context' COMMENT '记忆类型',
    priority_level VARCHAR(32) DEFAULT 'medium' COMMENT '优先级',
    retention_level VARCHAR(32) DEFAULT 'long_term' COMMENT '保留周期',
    source_type VARCHAR(32) DEFAULT 'user_explicit' COMMENT '来源类型',
    tags VARCHAR(512) DEFAULT '' COMMENT '标签，逗号分隔',
    pinned TINYINT(1) DEFAULT 0 COMMENT '是否置顶',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='记忆总结表';
