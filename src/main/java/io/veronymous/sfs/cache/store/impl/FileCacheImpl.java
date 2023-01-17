package io.veronymous.sfs.cache.store.impl;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.cache.store.FileCache;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FileCacheImpl implements FileCache {

    private final Map<String, String> cache;

    public FileCacheImpl(Map<String, String> cache) {
        this.cache = cache;
    }

    public FileCacheImpl() {
        this(new HashMap<>());
    }

    @Override
    public String getFileContents(String filename) throws NotFoundException {
        String contents = this.cache.get(filename);

        if (contents == null)
            throw new NotFoundException("Could not find file contents for " + filename);

        return contents;
    }

    @Override
    public void setFileContents(String filename, String contents) {
        synchronized (this.cache) {
            this.cache.put(filename, contents);
        }
    }

    @Override
    public void remove(String filename) {
        synchronized (this.cache) {
            this.cache.remove(filename);
        }
    }
}
