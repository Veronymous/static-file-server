package io.veronymous.sfs.file;

import io.veronymous.sfs.cache.service.FileCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Service
public class FileWatcherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWatcherService.class.getName());

    private final Path filesDir;
    private final TaskExecutor taskExecutor;
    private final FileCacheService fileCache;

    public FileWatcherService(@Value("${files-dir:/etc/static-file-server/files}") Path filesDir,
                              TaskExecutor taskExecutor,
                              FileCacheService fileCache) {
        this.filesDir = filesDir;
        this.taskExecutor = taskExecutor;
        this.fileCache = fileCache;
    }

    @PostConstruct
    public void setup() throws IOException {
        this.loadFiles();

        this.startWatcher();
    }

    private void loadFiles() {
        LOGGER.info("Loading files...");

        File[] files = this.filesDir.toFile().listFiles();

        if (files == null)
            return;

        for (File file : files) {
            this.fileCache.cacheFile(file);
        }
    }

    private void startWatcher() throws IOException {
        WatchService watcher = FileSystems.getDefault().newWatchService();

        this.filesDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        this.taskExecutor.execute(() -> {
            // Watch for files
            WatchKey key = null;

            boolean exit = false;

            while (!exit) {
                // Listen for an event
                try {
                    key = watcher.take();
                } catch (InterruptedException e) {
                    if (key != null)
                        key.cancel();
                    exit = true;
                    continue;
                }

                // Get the events
                for (WatchEvent<?> event: key.pollEvents()) {
                    if (ENTRY_CREATE.equals(event.kind()))
                        this.processEntryCreate((WatchEvent<Path>) event);
                    else if (ENTRY_MODIFY.equals(event.kind()))
                        this.processEntryModify((WatchEvent<Path>) event);
                    else if (ENTRY_DELETE.equals(event.kind()))
                        this.processEntryDelete((WatchEvent<Path>) event);
                }
                key.reset();
            }
        });
    }

    private void processEntryCreate(WatchEvent<Path> event) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Created entry: {}", event.context());

        Path filePath = this.filesDir.resolve(event.context());

        this.fileCache.cacheFile(filePath.toFile());
    }

    private void processEntryModify(WatchEvent<Path> event) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Modified entry: {}", event.context());

        Path filePath = this.filesDir.resolve(event.context());

        this.fileCache.cacheFile(filePath.toFile());
    }

    private void processEntryDelete(WatchEvent<Path> event) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Deleted entry: {}", event.context());

        this.fileCache.removeFile(event.context().toFile().getName());
    }
}
