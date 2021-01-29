package com.ongdev.media.server.service;

import com.ongdev.media.server.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ImageService {

    Image store(MultipartFile file, String fullFileName, String category, Image imageResponse);

    Path load(String filename);

    Resource loadAsResource(String filename);

    Image saveImage(MultipartFile file, String name, String category);

    void deleteAllImages();

    Page<Image> getAllFiles(Pageable pageable);

    Image getImageById(String id);

    Image getImageByLinkDownload(String link);

    Image getImageByLinkDisplay(String link);

    Image getImageByName(String name);

    Image updateFileById(MultipartFile file, String name, String category, String id);

    void deleteImageById(String id);

    byte[] displayImageByName(String name);
}
