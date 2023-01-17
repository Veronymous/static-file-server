package io.veronymous.sfs.cache.store;

import io.veronymous.sfs.api.exceptions.NotFoundException;

public interface FileCache {

    String getFileContents(String filename) throws NotFoundException;

    void setFileContents(String filename, String contents);

    void remove(String filename);
}
