package com.ongdev.media.server.service.impl;

import com.ongdev.media.server.config.FileProperties;
import com.ongdev.media.server.exception.EntityCreateFailedException;
import com.ongdev.media.server.exception.EntityDeleteFailedException;
import com.ongdev.media.server.exception.EntityNotFoundException;
import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.model.repository.ImageRepository;
import com.ongdev.media.server.service.FileService;
import com.ongdev.media.server.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.UUID;

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
            throw new EntityCreateFailedException("Could not initialize");
        }
    }

    @Override
    public Image store(MultipartFile file, String name, Image image) {
        try {
            if (file.isEmpty()) {
                throw new EntityCreateFailedException("Failed to store empty file");
            }
            String fullFileName = MapperUtils.toFullNameFile(file, name);
            if (imageRepository.existsByName(fullFileName)) {
                throw new EntityCreateFailedException("Name is existed");
            }
            Path destinationFile = MapperUtils.toDestinationFile(fullFileName, this.rootLocation);
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new EntityCreateFailedException("Can not store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                image.setName(fullFileName);
                String link = MapperUtils.toLink(destinationFile);
                if (imageRepository.existsByLink(link)) {
                    throw new EntityCreateFailedException("Link is existed");
                }
                image.setLink(link);
                return imageRepository.save(image);
            }
        } catch (IOException ex) {
            throw new EntityCreateFailedException("Failed to store file ");
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
                throw new EntityNotFoundException("Could not read file " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new EntityNotFoundException("Could not read file " + filename);
        }
    }

    @Override
    public Image saveImage(MultipartFile file, String name) {
        return store(file, name, new Image());
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Page<Image> getAllFiles(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    @Override
    public Image getFileById(String id) {
        return imageRepository.findById(UUID.fromString(id))
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Image getFileByLink(String link) {
        return imageRepository.findByLink(link)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Image getFileByName(String name) {
        return imageRepository.findByName(name)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Image updateFileById(MultipartFile file, String name, String id) {
        Image image = imageRepository.findById(UUID.fromString(id))
                .orElseThrow(EntityNotFoundException::new);
        deleteFileById(id);
        return store(file, name, image);
    }

    @Override
    public void deleteFileById(String id) {
        Image image = imageRepository.findById(UUID.fromString(id))
                .orElseThrow(EntityNotFoundException::new);
        String path = MapperUtils.toAbsolutePath(rootLocation, image.getName());
        try {
            Files.delete(Paths.get(path));
            imageRepository.deleteById(UUID.fromString(id));
        } catch (IOException | IllegalArgumentException ex) {
            throw new EntityDeleteFailedException(ex.getMessage());
        }
    }
}
