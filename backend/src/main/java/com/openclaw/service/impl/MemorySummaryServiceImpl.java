package com.openclaw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.openclaw.entity.MemorySummary;
import com.openclaw.mapper.MemorySummaryMapper;
import com.openclaw.service.MemorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MemorySummaryServiceImpl implements MemorySummaryService {

    @Autowired
    private MemorySummaryMapper memorySummaryMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "memory:summary:";
    private static final String REDIS_KEY_LIST = "memory:summary:list";

    @Override
    public MemorySummary save(MemorySummary memorySummary) {
        memorySummary.setCreateTime(LocalDateTime.now());
        memorySummary.setUpdateTime(LocalDateTime.now());
        memorySummaryMapper.insert(memorySummary);
        // 清除列表缓存
        redisTemplate.delete(REDIS_KEY_LIST);
        return memorySummary;
    }

    @Override
    public MemorySummary update(MemorySummary memorySummary) {
        memorySummary.setUpdateTime(LocalDateTime.now());
        memorySummaryMapper.updateById(memorySummary);
        // 清除缓存
        redisTemplate.delete(REDIS_KEY_PREFIX + memorySummary.getId());
        redisTemplate.delete(REDIS_KEY_LIST);
        return memorySummary;
    }

    @Override
    public MemorySummary getById(Long id) {
        // 先从缓存获取
        MemorySummary summary = (MemorySummary) redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id);
        if (summary != null) {
            return summary;
        }

        // 从数据库查询
        summary = memorySummaryMapper.selectById(id);

        // 设置缓存
        if (summary != null) {
            redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + id, summary, 24, TimeUnit.HOURS);
        }

        return summary;
    }

    @Override
    public Page<MemorySummary> getList(int page, int size) {
        Page<MemorySummary> memorySummaryPage = new Page<>(page, size);
        QueryWrapper<MemorySummary> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return memorySummaryMapper.selectPage(memorySummaryPage, wrapper);
    }

    @Override
    public boolean delete(Long id) {
        int result = memorySummaryMapper.deleteById(id);
        // 清除缓存
        redisTemplate.delete(REDIS_KEY_PREFIX + id);
        redisTemplate.delete(REDIS_KEY_LIST);
        return result > 0;
    }

    @Override
    public List<MemorySummary> getAll() {
        // 先从缓存获取
        List<MemorySummary> summaries = (List<MemorySummary>) redisTemplate.opsForValue().get(REDIS_KEY_LIST);
        if (summaries != null) {
            return summaries;
        }

        // 从数据库查询
        QueryWrapper<MemorySummary> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        summaries = memorySummaryMapper.selectList(wrapper);

        // 设置缓存
        if (!summaries.isEmpty()) {
            redisTemplate.opsForValue().set(REDIS_KEY_LIST, summaries, 1, TimeUnit.HOURS);
        }

        return summaries;
    }
}