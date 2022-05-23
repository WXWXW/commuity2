package com.work.commuity2.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDto {
    private String  categoryTag;
    private List<String> tags;
}
