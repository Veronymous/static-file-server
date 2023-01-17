package io.veronymous.sfs.cache.store;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.cache.store.model.FileMetadata;

public interface FileMetadataCache {

    void setMetadata(String filename, FileMetadata metadata);

    FileMetadata getMetadata(String filename) throws NotFoundException;

    void remove(String filename);
}
