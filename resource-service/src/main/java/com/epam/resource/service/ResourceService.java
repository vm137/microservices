package com.epam.resource.service;

import com.epam.resource.exceptions.ResourceException;
import com.epam.resource.model.entity.ResourceEntity;
import com.epam.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;

    public Long saveResource(byte[] byteArray) {
        ResourceEntity re = new ResourceEntity();
        re.setContent(byteArray);
        re.setTimestamp(LocalDateTime.now().toString());
        repository.saveAndFlush(re);
        ResourceEntity saved = repository.save(re);
        return saved.getId();
    }

    public byte[] getResource(long id) {
        ResourceEntity entity = repository.findById(id).orElseThrow(
                () -> new ResourceException("Resource not found, id: " + id)
        );
        return entity.getContent();
    }

    public void deleteResource(long[] ids) {
        for (long id : ids) {
            ResourceEntity entity = repository.findById(id).orElseThrow(
                    () -> new ResourceException("Resource not found, id: " + id)
            );
            repository.delete(entity);
        }
    }
}
