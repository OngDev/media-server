package com.ongdev.media.server.service.impl;

import com.ongdev.media.server.controller.config.FileProperties;
import com.ongdev.media.server.controller.exception.FileException;
import com.ongdev.media.server.controller.exception.FileNotFoundException;
import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.model.repository.ImageRepository;
import com.ongdev.media.server.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    private final ImageRepository imageRepository;

    private final Path rootLocation;

    @Autowired
    public FileServiceImpl(FileProperties fileProperties, ImageRepository imageRepository) {
        this.rootLocation = Paths.get(fileProperties.getLocation());
        this.imageRepository = imageRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ex) {
            throw new FileException("Could not initialize", ex);
        }
    }

    @Override
    public Image store(MultipartFile file, String name) {
        try {
            if (file.isEmpty()) {
                throw new FileException("Failed to store empty file");
            }

            String renameTo = name + "." + file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(renameTo))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new FileException("Can not store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile
                        , StandardCopyOption.REPLACE_EXISTING);
                Image image = new Image();
                image.setName(renameTo);
                image.setLink(destinationFile.toString());
                return imageRepository.save(image);
            }
        } catch (IOException ex) {
            throw new FileException("Failed to store file ", ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException ex) {
            throw new FileException("Failed to read stored files", ex);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Could not read file " + filename);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
