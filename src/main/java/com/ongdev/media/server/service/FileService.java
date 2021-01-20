package com.ongdev.media.server.service;

import com.ongdev.media.server.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {

    void init();

    Image store(MultipartFile file, String name, Image image);

    Path load(String filename);

    Resource loadAsResource(String filename);

    Image saveImage(MultipartFile file, String name);

    void deleteAll();

    Page<Image> getAllFiles(Pageable pageable);

    Image getFileById(String id);

    Image getFileByLink(String link);

    Image getFileByName(String name);

    Image updateFileById(MultipartFile file, String name, String id);

    void deleteFileById(String id);
}
