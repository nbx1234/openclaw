package com.openclaw.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersistentMemoryFieldConfigRequest {
    private String title;
    private List<PersistentMemoryFieldItem> fields;
}
