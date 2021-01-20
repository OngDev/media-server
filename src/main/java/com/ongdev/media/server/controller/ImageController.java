package com.ongdev.media.server.controller;

import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.service.ImageService;
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
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("filename/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = imageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping
    public ResponseEntity<Image> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("category") String category
    ) {
        return new ResponseEntity<>(imageService.addImage(file, name, category), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Image>> getAllFiles(
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return new ResponseEntity<>(imageService.getAllFiles(pageable), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Image> updateFileById(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("category") String category
    ) {
        return new ResponseEntity<>(imageService.updateFileById(file, name, category, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFileById(@PathVariable String id) {
        imageService.deleteFileById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteAllFiles() {
        imageService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Image> getFileById(@PathVariable String id) {
        return new ResponseEntity<>(imageService.getFileById(id), HttpStatus.OK);
    }

    @GetMapping("link")
    public ResponseEntity<Image> getFileByLink(@RequestParam String link) {
        return new ResponseEntity<>(imageService.getFileByLink(link), HttpStatus.OK);
    }

    @GetMapping("name")
    public ResponseEntity<Image> getFileByName(@RequestParam String name) {
        return new ResponseEntity<>(imageService.getFileByName(name), HttpStatus.OK);
    }
}
