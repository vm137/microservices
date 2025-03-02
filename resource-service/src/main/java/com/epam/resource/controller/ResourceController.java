package com.epam.resource.controller;

import com.epam.resource.exceptions.ResourceException;
import com.epam.resource.model.dto.SongDto;
import com.epam.resource.model.http.response.ResourceResponse;
import com.epam.resource.service.ResourceService;
import com.epam.resource.service.SongService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    private final SongService songService;

    @PostMapping(value = "/resources", consumes = "audio/mpeg")
    public ResponseEntity<ResourceResponse> saveResource(InputStream dataStream) {
        byte[] byteArray;
        try {
            byteArray = IOUtils.toByteArray(dataStream);
        } catch (IOException e) {
            throw new ResourceException("Cannot convert audio/mpeg file to bytes");
        }

        Long savedId = resourceService.saveResource(byteArray);
        SongDto songDto = songService.parseTags(byteArray);
        songDto.setId(savedId);
//        songService.saveSongData(songDto);

        ResourceResponse response = new ResourceResponse(savedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/resources/{id}")
    public void getResource(@PathVariable long id, HttpServletResponse response) throws IOException {
        byte[] content = resourceService.getResource(id);
        InputStream inputStream = new ByteArrayInputStream(content);
        IOUtils.copy(inputStream, response.getOutputStream());

        response.setContentType("audio/mpeg");
        response.addHeader("Content-disposition", "attachment;filename=filename.mp3");
        response.setHeader("Content-Length", String.valueOf(content.length));
        response.flushBuffer();
    }

    @DeleteMapping("/resources/{ids}")
    public void deleteResource(@PathVariable long[] ids) {
        resourceService.deleteResource(ids);
    }
}
