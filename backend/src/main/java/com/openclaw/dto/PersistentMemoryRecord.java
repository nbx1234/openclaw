package com.openclaw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PersistentMemoryRecord {
    private Long id;
    private String title;
    private String content;
    private String preview;
    private Map<String, String> classifications;
    private List<String> tags;
    private Boolean pinned;
    private Map<String, Object> timeline;
}
