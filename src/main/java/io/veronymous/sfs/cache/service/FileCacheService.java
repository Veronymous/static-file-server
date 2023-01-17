package io.veronymous.sfs.cache.service;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.cache.store.FileCache;
import io.veronymous.sfs.cache.store.FileMetadataCache;
import io.veronymous.sfs.cache.store.model.FileMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class FileCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileCacheService.class);

    private static final String JSON = "json";

    private final FileCache fileCache;
    private final FileMetadataCache metadataCache;

    public FileCacheService(FileCache fileCache, FileMetadataCache metadataCache) {
        this.fileCache = fileCache;
        this.metadataCache = metadataCache;
    }

    public void cacheFile(File file) {
        LOGGER.info("Caching file - {}", file.getName());

        if (file.isDirectory()) {
            LOGGER.info("File is directory, ignoring...");
            return;
        }

        // Ignore none-json
        if (!JSON.equals(getFileExtension(file))) {
            LOGGER.info("File is not a json file, ignoring...");
            return;
        }

        try (FileInputStream inputStream = new FileInputStream(file)) {
            String contents = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            FileMetadata metadata = new FileMetadata(
                    createDigest(contents)
            );

            this.metadataCache.setMetadata(file.getName(), metadata);
            this.fileCache.setFileContents(file.getName(), contents);

        } catch (IOException e) {
            LOGGER.error("Could not read file.", e);
        }
    }

    public void removeFile(String filename) {
        this.fileCache.remove(filename);
        this.metadataCache.remove(filename);
    }

    public FileMetadata getMetadata(String filename) throws NotFoundException {
        return this.metadataCache.getMetadata(filename);
    }

    public String getFileContents(String filename) throws NotFoundException {
        return this.fileCache.getFileContents(filename);
    }

    private static String createDigest(String contents) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // This should never happen
            throw new RuntimeException(e);
        }

        digest.update(contents.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(digest.digest());
    }

    private static String getFileExtension(File file) {
        String filename = file.getName();
        int index = filename.lastIndexOf(".");

        if (index < 0)
            return null;

        return filename.substring(index + 1);
    }
}
