package com.epam.resource.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SongDto {
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}
