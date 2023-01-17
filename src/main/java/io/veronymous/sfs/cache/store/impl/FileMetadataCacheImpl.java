package io.veronymous.sfs.cache.store.impl;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.cache.store.FileMetadataCache;
import io.veronymous.sfs.cache.store.model.FileMetadata;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FileMetadataCacheImpl implements FileMetadataCache {

    private final Map<String, FileMetadata> metadata;

    public FileMetadataCacheImpl(Map<String, FileMetadata> metadata) {
        this.metadata = metadata;
    }

    public FileMetadataCacheImpl() {
        this(new HashMap<>());
    }

    @Override
    public void setMetadata(String filename, FileMetadata metadata) {
        synchronized (this.metadata) {
            this.metadata.put(filename, metadata);
        }
    }

    @Override
    public FileMetadata getMetadata(String filename) throws NotFoundException {
        FileMetadata metadata = this.metadata.get(filename);

        if (metadata == null)
            throw new NotFoundException("Could not find file for name: " + filename);

        return metadata;
    }

    @Override
    public void remove(String filename) {
        synchronized (this.metadata) {
            this.metadata.remove(filename);
        }
    }
}
