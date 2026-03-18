package com.openclaw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclaw.dto.MemoryOptionItem;
import com.openclaw.dto.PersistentMemoryDisplayConfig;
import com.openclaw.dto.PersistentMemoryFieldItem;
import com.openclaw.dto.PersistentMemoryPayload;
import com.openclaw.dto.PersistentMemoryRecord;
import com.openclaw.entity.MemorySummary;
import com.openclaw.mapper.MemorySummaryMapper;
import com.openclaw.service.MemorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MemorySummaryServiceImpl implements MemorySummaryService {
    private static final String DEFAULT_MEMORY_TYPE = "context";
    private static final String DEFAULT_PRIORITY_LEVEL = "medium";
    private static final String DEFAULT_RETENTION_LEVEL = "long_term";
    private static final String DEFAULT_SOURCE_TYPE = "user_explicit";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String REDIS_KEY_FIELD_OPTIONS = "memory:summary:field-options";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    private MemorySummaryMapper memorySummaryMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "memory:summary:";
    private static final String REDIS_KEY_LIST = "memory:summary:list";

    @Override
    public MemorySummary save(MemorySummary memorySummary) {
        applyDefaults(memorySummary);
        memorySummary.setCreateTime(LocalDateTime.now());
        memorySummary.setUpdateTime(LocalDateTime.now());
        memorySummaryMapper.insert(memorySummary);
        // 清除列表缓存
        redisTemplate.delete(REDIS_KEY_LIST);
        return memorySummary;
    }

    @Override
    public MemorySummary update(MemorySummary memorySummary) {
        applyDefaults(memorySummary);
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
        Object cached = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id);
        MemorySummary summary = convertToMemorySummary(cached);
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
        List<MemorySummary> summaries = convertToMemorySummaryList(redisTemplate.opsForValue().get(REDIS_KEY_LIST));
        if (!summaries.isEmpty()) {
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

    @Override
    public PersistentMemoryPayload getPersistentMemoryPayload() {
        List<MemorySummary> summaries = getAll();
        List<PersistentMemoryRecord> records = summaries.stream()
                .map(this::toPersistentRecord)
                .collect(Collectors.toList());

        long pinnedCount = summaries.stream()
                .filter(summary -> Boolean.TRUE.equals(summary.getPinned()))
                .count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", summaries.size());
        stats.put("pinned", pinnedCount);
        stats.put("highPriority", summaries.stream()
                .filter(summary -> "critical".equals(summary.getPriorityLevel()) || "high".equals(summary.getPriorityLevel()))
                .count());
        stats.put("types", summaries.stream()
                .collect(Collectors.groupingBy(
                        summary -> normalize(summary.getMemoryType(), DEFAULT_MEMORY_TYPE),
                        LinkedHashMap::new,
                        Collectors.counting()
                )));

        return new PersistentMemoryPayload(getPersistentMemoryFieldOptions(), buildRecordFieldLabels(), records, stats);
    }

    @Override
    public PersistentMemoryDisplayConfig getPersistentMemoryFieldOptions() {
        Object cached = redisTemplate.opsForValue().get(REDIS_KEY_FIELD_OPTIONS);
        PersistentMemoryDisplayConfig cachedOptions = convertToFieldOptions(cached);
        if (cachedOptions != null
                && !isBlank(cachedOptions.getTitle())
                && cachedOptions.getFields() != null
                && !cachedOptions.getFields().isEmpty()) {
            return cachedOptions;
        }

        PersistentMemoryDisplayConfig defaults = buildDefaultFieldOptions();
        redisTemplate.opsForValue().set(REDIS_KEY_FIELD_OPTIONS, defaults, 30, TimeUnit.DAYS);
        return defaults;
    }

    @Override
    public PersistentMemoryDisplayConfig updatePersistentMemoryFieldOptions(String title, List<PersistentMemoryFieldItem> fields) {
        PersistentMemoryDisplayConfig defaults = buildDefaultFieldOptions();
        PersistentMemoryDisplayConfig payload = new PersistentMemoryDisplayConfig(
                normalize(title, defaults.getTitle()),
                normalizeFieldItems(fields, defaults.getFields())
        );
        redisTemplate.opsForValue().set(REDIS_KEY_FIELD_OPTIONS, payload, 30, TimeUnit.DAYS);
        return payload;
    }

    private void applyDefaults(MemorySummary memorySummary) {
        memorySummary.setMemoryType(normalize(memorySummary.getMemoryType(), DEFAULT_MEMORY_TYPE));
        memorySummary.setPriorityLevel(normalize(memorySummary.getPriorityLevel(), DEFAULT_PRIORITY_LEVEL));
        memorySummary.setRetentionLevel(normalize(memorySummary.getRetentionLevel(), DEFAULT_RETENTION_LEVEL));
        memorySummary.setSourceType(normalize(memorySummary.getSourceType(), DEFAULT_SOURCE_TYPE));
        memorySummary.setTags(normalizeTags(memorySummary.getTags()));
        if (memorySummary.getPinned() == null) {
            memorySummary.setPinned(Boolean.FALSE);
        }
    }

    private PersistentMemoryRecord toPersistentRecord(MemorySummary summary) {
        Map<String, String> classifications = new LinkedHashMap<>();
        classifications.put("memoryType", normalize(summary.getMemoryType(), DEFAULT_MEMORY_TYPE));
        classifications.put("priorityLevel", normalize(summary.getPriorityLevel(), DEFAULT_PRIORITY_LEVEL));
        classifications.put("retentionLevel", normalize(summary.getRetentionLevel(), DEFAULT_RETENTION_LEVEL));
        classifications.put("sourceType", normalize(summary.getSourceType(), DEFAULT_SOURCE_TYPE));

        Map<String, Object> timeline = new LinkedHashMap<>();
        timeline.put("createdAt", formatDateTime(summary.getCreateTime()));
        timeline.put("updatedAt", formatDateTime(summary.getUpdateTime()));

        return new PersistentMemoryRecord(
                summary.getId(),
                summary.getTitle(),
                summary.getContent(),
                buildPreview(summary.getContent()),
                classifications,
                splitTags(summary.getTags()),
                Boolean.TRUE.equals(summary.getPinned()),
                timeline
        );
    }

    private PersistentMemoryDisplayConfig buildDefaultFieldOptions() {
        return new PersistentMemoryDisplayConfig(
                "家庭陪伴记忆字段",
                Arrays.asList(
                        new PersistentMemoryFieldItem("family_members", "家庭成员", "爸爸、妈妈、孩子"),
                        new PersistentMemoryFieldItem("member_preferences", "各成员喜好", "爸爸喜欢拿香烟，妈妈喜欢吃零食，孩子喜欢积木"),
                        new PersistentMemoryFieldItem("member_dislikes", "各成员忌讳", "爸爸不喜欢，妈妈不喜欢带毛的玩具，孩子不喜欢遥控"),
                        new PersistentMemoryFieldItem("learned_skills", "已经学会的技能", "语音识别、视觉识别、路径规划、")
                )
        );
    }

    private Map<String, List<MemoryOptionItem>> buildRecordFieldLabels() {
        Map<String, List<MemoryOptionItem>> options = new LinkedHashMap<>();
        options.put("memoryTypes", Arrays.asList(
                new MemoryOptionItem("identity", "身份信息", "稳定的人物身份、角色和背景事实"),
                new MemoryOptionItem("preference", "偏好习惯", "长期有效的喜好、风格和使用习惯"),
                new MemoryOptionItem("goal", "长期目标", "持续追踪的目标、计划和预期结果"),
                new MemoryOptionItem("constraint", "约束规则", "不能违反的限制、禁忌和边界条件"),
                new MemoryOptionItem("relationship", "关系人物", "与他人、组织、项目的持续关系"),
                new MemoryOptionItem("context", "上下文事实", "需要跨会话记住的重要背景信息")
        ));
        options.put("priorityLevels", Arrays.asList(
                new MemoryOptionItem("critical", "关键", "必须优先保留和优先召回"),
                new MemoryOptionItem("high", "高", "重要且应频繁使用"),
                new MemoryOptionItem("medium", "中", "常规保留，按需召回"),
                new MemoryOptionItem("low", "低", "仅作为补充背景参考")
        ));
        options.put("retentionLevels", Arrays.asList(
                new MemoryOptionItem("permanent", "永久", "长期有效，除非用户明确覆盖"),
                new MemoryOptionItem("long_term", "长期", "跨多次会话保留"),
                new MemoryOptionItem("temporary", "临时", "阶段性有效，后续可归档")
        ));
        options.put("sourceTypes", Arrays.asList(
                new MemoryOptionItem("user_explicit", "用户明确说明", "用户直接表达，可直接持久化"),
                new MemoryOptionItem("assistant_inferred", "系统推断", "基于上下文归纳，需要谨慎使用"),
                new MemoryOptionItem("imported", "外部导入", "来自外部系统、文件或同步任务")
        ));
        return options;
    }

    private MemorySummary convertToMemorySummary(Object cached) {
        if (cached == null) {
            return null;
        }
        if (cached instanceof MemorySummary) {
            return (MemorySummary) cached;
        }
        if (cached instanceof Map) {
            return objectMapper.convertValue(cached, MemorySummary.class);
        }
        return null;
    }

    private List<MemorySummary> convertToMemorySummaryList(Object cached) {
        if (!(cached instanceof List)) {
            return Collections.emptyList();
        }
        List<?> rawList = (List<?>) cached;
        return rawList.stream()
                .map(this::convertToMemorySummary)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private PersistentMemoryDisplayConfig convertToFieldOptions(Object cached) {
        if (cached == null) {
            return null;
        }
        if (cached instanceof PersistentMemoryDisplayConfig) {
            return (PersistentMemoryDisplayConfig) cached;
        }
        if (cached instanceof Map) {
            try {
                return objectMapper.convertValue(cached, PersistentMemoryDisplayConfig.class);
            } catch (IllegalArgumentException ignored) {
                Object legacyFields = ((Map<?, ?>) cached).get("memoryFields");
                if (legacyFields instanceof List) {
                    return new PersistentMemoryDisplayConfig(
                            "家庭陪伴记忆字段",
                            ((List<?>) legacyFields).stream()
                                    .map(this::convertToPersistentFieldItem)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList())
                    );
                }
            }
        }
        return null;
    }

    private List<PersistentMemoryFieldItem> normalizeFieldItems(List<PersistentMemoryFieldItem> fields, List<PersistentMemoryFieldItem> defaults) {
        if (fields == null || fields.isEmpty()) {
            return defaults;
        }
        List<PersistentMemoryFieldItem> normalized = fields.stream()
                .map(this::convertToPersistentFieldItem)
                .filter(Objects::nonNull)
                .filter(item -> !isBlank(item.getKey()) && !isBlank(item.getLabel()))
                .map(item -> new PersistentMemoryFieldItem(
                        item.getKey().trim(),
                        item.getLabel().trim(),
                        normalize(item.getValue(), "")
                ))
                .collect(Collectors.toList());
        return normalized.isEmpty() ? defaults : normalized;
    }

    private PersistentMemoryFieldItem convertToPersistentFieldItem(Object rawItem) {
        if (rawItem == null) {
            return null;
        }
        if (rawItem instanceof PersistentMemoryFieldItem) {
            return (PersistentMemoryFieldItem) rawItem;
        }
        if (rawItem instanceof Map) {
            try {
                Map<?, ?> rawMap = (Map<?, ?>) rawItem;
                Object key = rawMap.get("key");
                Object label = rawMap.get("label");
                Object value = rawMap.get("value");
                if (label == null) {
                    label = rawMap.get("value");
                }
                if (value == null) {
                    value = rawMap.get("description");
                }
                String normalizedKey = key == null ? normalize(String.valueOf(label), "field") : String.valueOf(key);
                return new PersistentMemoryFieldItem(
                        normalizedKey,
                        label == null ? normalizedKey : String.valueOf(label),
                        value == null ? "" : String.valueOf(value)
                );
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalize(String value, String defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private String normalizeTags(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return "";
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .collect(Collectors.joining(","));
    }

    private List<String> splitTags(String tags) {
        if (tags == null || tags.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    private String buildPreview(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }
        String plainText = content.replaceAll("\\s+", " ").trim();
        return plainText.length() > 96 ? plainText.substring(0, 96) + "..." : plainText;
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.format(DATE_TIME_FORMATTER);
    }
}
