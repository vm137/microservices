package com.epam.song.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Song {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}
