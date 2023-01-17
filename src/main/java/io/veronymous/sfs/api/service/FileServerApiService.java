package io.veronymous.sfs.api.service;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.cache.service.FileCacheService;
import io.veronymous.sfs.cache.store.model.FileMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FileServerApiService {

    private static final String DIGEST = "Digest";
    private static final String SHA_256 = "sha-256";

    private final FileCacheService fileCache;

    public FileServerApiService(FileCacheService fileCache) {
        this.fileCache = fileCache;
    }

    public ResponseEntity<String> getFile(String filename) throws NotFoundException {
        FileMetadata metadata = this.fileCache.getMetadata(filename);
        String contents = this.fileCache.getFileContents(filename);

        return ResponseEntity.ok()
                .header(DIGEST, SHA_256 + "=" + metadata.getDigest())
                .body(contents);
    }

    public ResponseEntity<FileMetadata> getFileMetadata(String filename) throws NotFoundException {
        FileMetadata metadata = this.fileCache.getMetadata(filename);

        return ResponseEntity.ok(metadata);
    }
}
