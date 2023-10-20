package com.msil.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailsDto {
    private Long id;
    private String userName;
    private List<WatchListGroupDto> watchListGroups;
}
