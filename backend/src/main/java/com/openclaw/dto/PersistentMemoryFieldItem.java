package com.openclaw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersistentMemoryFieldItem {
    private String key;
    private String label;
    private String value;
}
