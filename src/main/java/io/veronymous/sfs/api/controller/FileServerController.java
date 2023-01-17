package io.veronymous.sfs.api.controller;

import io.veronymous.sfs.api.exceptions.NotFoundException;
import io.veronymous.sfs.api.service.FileServerApiService;
import io.veronymous.sfs.api.types.Paths;
import io.veronymous.sfs.cache.store.model.FileMetadata;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
public class FileServerController {

    private final FileServerApiService service;

    public FileServerController(FileServerApiService service) {
        this.service = service;
    }

    @GetMapping(
            path = Paths.FILE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getFile(@PathVariable("filename")
                                          @Valid
                                          @NotNull
                                          @NotBlank String filename)
            throws NotFoundException {
        return this.service.getFile(filename);
    }

    @GetMapping(
            path = Paths.FILE_METADATA,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable("filename")
                                                        @Valid
                                                        @NotNull
                                                        @NotBlank String filename)
            throws NotFoundException {
        return this.service.getFileMetadata(filename);
    }
}
