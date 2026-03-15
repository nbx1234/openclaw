package com.openclaw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("memory_summary")
public class MemorySummary {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String memoryType;
    private String priorityLevel;
    private String retentionLevel;
    private String sourceType;
    private String tags;
    private Boolean pinned;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
