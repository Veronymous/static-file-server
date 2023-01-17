package io.veronymous.sfs.api.types;

public class Paths {

    public static final String FILE = "/{filename}";

    public static final String FILE_METADATA = "/{filename}/metadata";

    private Paths() {
        throw new IllegalStateException("Static class.");
    }
}
