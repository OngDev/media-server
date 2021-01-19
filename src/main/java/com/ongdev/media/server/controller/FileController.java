package com.ongdev.media.server.controller;

import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<List<Object>> listUploadedFiles() {

        Object[] fileName = fileService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .toArray();

        List<Object> toResponse = Arrays.asList(fileName);
        return ResponseEntity.ok(toResponse);
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<Image> handleFileUpload
            (@RequestParam("file") MultipartFile file,
            @RequestParam("name") String name) {
        return ResponseEntity.ok(fileService.store(file, name));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleStorageFileNotFound(Exception exc) {
        return ResponseEntity.notFound().build();
    }

}
