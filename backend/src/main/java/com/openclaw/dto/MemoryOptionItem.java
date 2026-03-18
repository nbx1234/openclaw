package com.openclaw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryOptionItem {
    private String value;
    private String label;
    private String description;
}
