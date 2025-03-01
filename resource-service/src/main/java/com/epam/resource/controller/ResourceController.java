package com.epam.resource.controller;

import com.epam.resource.model.dto.SongDto;
import com.epam.resource.model.entity.ResourceEntity;
import com.epam.resource.model.http.response.ResourceResponse;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.service.ResourceService;
import com.epam.resource.service.SongService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    private final SongService songService;

    private final ResourceRepository repository;

    @PostMapping(value = "/resources", consumes = "audio/mpeg")
    public ResponseEntity<ResourceResponse> saveFile(InputStream dataStream) {
        SongDto songDto = songService.parseTags(dataStream);
        Long savedId = resourceService.saveResource(dataStream);

        songDto.setId(savedId);
        songService.saveSongData(songDto);

        ResourceResponse response = new ResourceResponse(savedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/resources/{id}", produces = "audio/mpeg")
    public @ResponseBody Resource getFile(@PathVariable long id) {
//        ResourceEntity entity = repository.getReferenceById(id);
        ResourceEntity entity = repository.findById(id).get();
        byte[] content = entity.getContent();

        ByteArrayResource resource = new ByteArrayResource(content);
        return resource;

//        return IOUtils.toByreArray()
    }

    @DeleteMapping("/resources/{ids}")
    public void deleteFile(@PathVariable int[] ids) {
    }
}
