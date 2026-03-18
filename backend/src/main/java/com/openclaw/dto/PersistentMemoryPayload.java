package com.openclaw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PersistentMemoryPayload {
    private PersistentMemoryDisplayConfig options;
    private Map<String, List<MemoryOptionItem>> fieldLabelOptions;
    private List<PersistentMemoryRecord> records;
    private Map<String, Object> stats;
}
