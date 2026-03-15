package com.openclaw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openclaw.dto.MemoryOptionItem;
import com.openclaw.dto.PersistentMemoryDisplayConfig;
import com.openclaw.dto.PersistentMemoryFieldItem;
import com.openclaw.dto.PersistentMemoryPayload;
import com.openclaw.entity.MemorySummary;

import java.util.List;
import java.util.Map;

public interface MemorySummaryService {
    /**
     * 保存记忆总结
     */
    MemorySummary save(MemorySummary memorySummary);

    /**
     * 更新记忆总结
     */
    MemorySummary update(MemorySummary memorySummary);

    /**
     * 根据ID获取记忆总结
     */
    MemorySummary getById(Long id);

    /**
     * 分页查询历史记录
     */
    Page<MemorySummary> getList(int page, int size);

    /**
     * 删除记忆总结
     */
    boolean delete(Long id);

    /**
     * 获取所有记忆总结
     */
    List<MemorySummary> getAll();

    PersistentMemoryPayload getPersistentMemoryPayload();

    PersistentMemoryDisplayConfig getPersistentMemoryFieldOptions();

    PersistentMemoryDisplayConfig updatePersistentMemoryFieldOptions(String title, List<PersistentMemoryFieldItem> fields);
}
