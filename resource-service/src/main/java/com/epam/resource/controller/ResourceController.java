package com.epam.resource.controller;

import com.epam.resource.model.http.response.ResourceResponse;
import com.epam.resource.service.ResourceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping(value = "/resources", consumes = "audio/mpeg")
    public ResponseEntity<ResourceResponse> saveResource(InputStream dataStream) {
        Long savedId = resourceService.saveResource(dataStream);
        ResourceResponse response = new ResourceResponse(savedId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/resources/{id}")
    public void getResource(@PathVariable long id, HttpServletResponse response) throws IOException {
        byte[] content = resourceService.getResource(id); // we need this to get content length
        InputStream inputStream = new ByteArrayInputStream(content);
        IOUtils.copy(inputStream, response.getOutputStream());

        response.setContentType("audio/mpeg");
        response.addHeader("Content-disposition", "attachment;filename=filename.mp3");
        response.setHeader("Content-Length", String.valueOf(content.length));
        response.flushBuffer();
    }

    @DeleteMapping(path ="/resources")
    public void deleteResource(@RequestParam long[] id) {
        resourceService.deleteResource(id);
    }
}
