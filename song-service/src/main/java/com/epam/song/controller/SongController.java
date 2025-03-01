package com.epam.song.controller;

import com.epam.song.model.entity.Song;
import com.epam.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongController {

    @Autowired
    SongRepository repository;

    @PostMapping("/songs/{id}")
    public Song saveSong(@RequestBody Song song) {

        Song saved = new Song();

        return null;
    }

    @GetMapping("/songs/{id}")
    public Song getSong(@PathVariable int id) {

        return null;
    }
}
