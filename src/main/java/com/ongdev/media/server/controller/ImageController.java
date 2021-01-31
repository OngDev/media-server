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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadImageByName(@PathVariable String filename) {
        Resource image = imageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + image.getFilename() + "\"").body(image);
    }

    @PostMapping
    public ResponseEntity<Image> saveImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("category") String category
    ) {
        return new ResponseEntity<>(imageService.saveImage(image, name, category), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Image>> getAllImages(
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return new ResponseEntity<>(imageService.getAllFiles(pageable), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Image> updateImageById(
            @PathVariable String id,
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("category") String category
    ) {
        return new ResponseEntity<>(imageService.updateFileById(image, name, category, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteImageById(@PathVariable String id) {
        imageService.deleteImageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteAllImages() {
        imageService.deleteAllImages();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Image> getImageById(@PathVariable String id) {
        return new ResponseEntity<>(imageService.getImageById(id), HttpStatus.OK);
    }

    @GetMapping("link-download")
    public ResponseEntity<Image> getImageByLinkDownload(@RequestParam String link) {
        return new ResponseEntity<>(imageService.getImageByLinkDownload(link), HttpStatus.OK);
    }

    @GetMapping("link-display")
    public ResponseEntity<Image> getImageByLinkDisplay(@RequestParam String link) {
        return new ResponseEntity<>(imageService.getImageByLinkDisplay(link), HttpStatus.OK);
    }

    @GetMapping("name")
    public ResponseEntity<Image> getImageByName(@RequestParam String name) {
        return new ResponseEntity<>(imageService.getImageByName(name), HttpStatus.OK);
    }

    @GetMapping(value = "display/{name:.+}")
    public ResponseEntity<byte[]> displayImageByName(@PathVariable String name) {
        byte[] image = imageService.displayImageByName(name);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
