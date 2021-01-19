package com.ongdev.media.server.service;

import com.ongdev.media.server.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {

    void init();

    Image store(MultipartFile file, String name);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    Page<Image> getAllFiles(Pageable pageable);

    Image getFileById(String id);

    Image getFileByLink(String link);

    Image getFileByName(String name);

    Image updateFileById(String id);

    void deleteFileById(String id);
}
