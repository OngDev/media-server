package com.ongdev.media.server.controller;

import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<Image> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name") String name) {
        return new ResponseEntity<>(fileService.saveImage(file, name), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Image>> getAllFiles(
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return new ResponseEntity<>(fileService.getAllFiles(pageable), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Image> updateFileById(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name") String name
    ) {
        return new ResponseEntity<>(fileService.updateFileById(file, name, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFileById(@PathVariable String id) {
        fileService.deleteFileById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("id")
    public ResponseEntity<Image> getFileById(@RequestParam String id) {
        return new ResponseEntity<>(fileService.getFileById(id), HttpStatus.OK);
    }

    @GetMapping("link")
    public ResponseEntity<Image> getFileByLink(@RequestParam String link) {
        return new ResponseEntity<>(fileService.getFileByLink(link), HttpStatus.OK);
    }

    @GetMapping("name")
    public ResponseEntity<Image> getFileByName(@RequestParam String name) {
        return new ResponseEntity<>(fileService.getFileByName(name), HttpStatus.OK);
    }
}
