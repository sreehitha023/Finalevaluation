package com.msil.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class WatchListGroupDto {
    private Long id;
    private String groupName;
    private List<String> symbols;

}