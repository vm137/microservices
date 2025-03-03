package com.epam.resource.service;

import com.epam.resource.exceptions.ResourceException;
import com.epam.resource.model.entity.ResourceEntity;
import com.epam.resource.repository.ResourceRepository;
import com.epam.resource.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository repository;

    private final SongService songService;

    public Long saveResource(InputStream dataStream) {
        byte[] byteArray = FileUtils.streamToByteArray(dataStream);
        ResourceEntity re = new ResourceEntity();
        re.setContent(byteArray);
        re.setTimestamp(LocalDateTime.now().toString());
        ResourceEntity savedEntity = repository.save(re);

        songService.storeMetadata(savedEntity.getId(), byteArray);
        return savedEntity.getId();
    }

    public byte[] getResource(long id) {
        ResourceEntity entity = repository.findById(id).orElseThrow(
                () -> new ResourceException("Resource not found, id: " + id)
        );
        return entity.getContent();
    }

    public void deleteResource(long[] ids) {
        for (long id : ids) {
            Optional<ResourceEntity> entity = repository.findById(id);
            entity.ifPresent(repository::delete);
        }
    }
}
