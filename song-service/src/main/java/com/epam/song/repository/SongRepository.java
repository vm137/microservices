package com.epam.song.repository;

import com.epam.song.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
        Optional<Song> findByName(String fileName);
}
