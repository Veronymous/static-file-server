package io.veronymous.sfs.cache.store.model;

public class FileMetadata {

    // Sha256 hash
    private final String digest;

    public FileMetadata(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }
}
