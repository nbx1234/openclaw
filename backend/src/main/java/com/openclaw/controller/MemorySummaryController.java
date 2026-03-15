package com.openclaw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openclaw.dto.MemoryOptionItem;
import com.openclaw.dto.PersistentMemoryDisplayConfig;
import com.openclaw.dto.PersistentMemoryFieldConfigRequest;
import com.openclaw.dto.PersistentMemoryPayload;
import com.openclaw.entity.MemorySummary;
import com.openclaw.service.MemorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/memory-summary")
@CrossOrigin(origins = "*")
public class MemorySummaryController {

    @Autowired
    private MemorySummaryService memorySummaryService;

    /**
     * 保存记忆总结
     */
    @PostMapping({"", "/save"})
    public Map<String, Object> save(@RequestBody MemorySummary memorySummary) {
        MemorySummary result = memorySummaryService.save(memorySummary);
        return buildResponse(200, "保存成功", result);
    }

    /**
     * 更新记忆总结
     */
    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody MemorySummary memorySummary) {
        memorySummary.setId(id);
        MemorySummary result = memorySummaryService.update(memorySummary);
        return buildResponse(200, "更新成功", result);
    }

    /**
     * 根据ID获取记忆总结
     */
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        MemorySummary result = memorySummaryService.getById(id);
        return buildResponse(200, "查询成功", result);
    }

    /**
     * 分页查询历史记录
     */
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Page<MemorySummary> result = memorySummaryService.getList(page, size);
        return buildResponse(200, "查询成功", result);
    }

    /**
     * 获取所有记忆总结
     */
    @GetMapping
    public Map<String, Object> getAll() {
        List<MemorySummary> result = memorySummaryService.getAll();
        return buildResponse(200, "查询成功", result);
    }

    /**
     * 获取持久化记忆面板数据
     */
    @GetMapping("/persistent")
    public Map<String, Object> getPersistentMemoryPayload() {
        PersistentMemoryPayload result = memorySummaryService.getPersistentMemoryPayload();
        return buildResponse(200, "查询成功", result);
    }

    /**
     * 获取持久化记忆字段配置
     */
    @GetMapping("/persistent-fields")
    public Map<String, Object> getPersistentMemoryFields() {
        PersistentMemoryDisplayConfig result = memorySummaryService.getPersistentMemoryFieldOptions();
        return buildResponse(200, "查询成功", result);
    }

    /**
     * 更新持久化记忆字段配置
     */
    @PutMapping("/persistent-fields")
    public Map<String, Object> updatePersistentMemoryFields(@RequestBody PersistentMemoryFieldConfigRequest request) {
        PersistentMemoryDisplayConfig result = memorySummaryService.updatePersistentMemoryFieldOptions(request.getTitle(), request.getFields());
        return buildResponse(200, "更新成功", result);
    }

    /**
     * 删除记忆总结
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean result = memorySummaryService.delete(id);
        if (result) {
            return buildResponse(200, "删除成功", null);
        } else {
            return buildResponse(404, "记录不存在", null);
        }
    }

    private Map<String, Object> buildResponse(int code, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}
