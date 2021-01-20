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

    Image addImage(MultipartFile file, String name, String category);

    void deleteAll();

    Page<Image> getAllFiles(Pageable pageable);

    Image getFileById(String id);

    Image getFileByLink(String link);

    Image getFileByName(String name);

    Image updateFileById(MultipartFile file, String name, String category, String id);

    void deleteFileById(String id);
}
