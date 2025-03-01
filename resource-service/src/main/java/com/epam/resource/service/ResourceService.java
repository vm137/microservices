package com.epam.resource.service;

import com.epam.resource.exceptions.ResourceException;
import com.epam.resource.model.entity.ResourceEntity;
import com.epam.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;

//    public ResourceDto convertToResource(InputStream dataStream) {
//
//
//
//        return null;
//    }

    @SneakyThrows
    public Long saveResource(InputStream dataStream) {
        byte[] byteArray = null;
        try {
        byteArray = dataStream.readAllBytes();
        } catch (IOException e) {
            throw new ResourceException("Failed to read bytes from file");
        }

        ResourceEntity re = new ResourceEntity();
        re.setContent(byteArray);
        re.setTimestamp(LocalDateTime.now().toString());
        repository.saveAndFlush(re);
        ResourceEntity saved = repository.save(re);
        return saved.getId();
    }
}
