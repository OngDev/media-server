package com.ongdev.media.server.service.impl;

import com.ongdev.media.server.exception.*;
import com.ongdev.media.server.model.Image;
import com.ongdev.media.server.model.repository.ImageRepository;
import com.ongdev.media.server.service.ImageService;
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
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final Path rootLocation = Paths.get("archive");

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image store(MultipartFile file, String fullFileName, String category, Image imageResponse) {
        try {
            if (file.isEmpty()) {
                throw new EntityCreateFailedException("Failed to store empty file");
            }
            Path destinationFile = MapperUtils.toDestinationFile(fullFileName, this.rootLocation);
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new EntityCreateFailedException("Can not store file outside current directory");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                boolean canResize = MapperUtils.canResize(this.rootLocation, fullFileName, category);
                if (!canResize) {
                    String path = MapperUtils.toAbsolutePath(rootLocation, fullFileName);
                    Files.delete(Paths.get(path));
                    throw new CouldNotResizeException("Category is not valid\nThere are three categories: profile, cover and link");
                }
                imageResponse.setName(fullFileName);
                imageResponse.setCategory(category);
                String link = MapperUtils.toLink(destinationFile);
                imageResponse.setLink(link);
                return imageRepository.save(imageResponse);
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
    public Image addImage(MultipartFile file, String name, String category) {
        Image imageResponse = new Image();
        String fullFileName = MapperUtils.toFullFileName(file, name);
        if (imageRepository.existsByName(fullFileName)) {
            throw new EntityCreateFailedException("Name is existed");
        }
        return store(file, fullFileName, category, imageResponse);
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
            Files.createDirectory(rootLocation);
        } catch (IOException ex) {
            throw new EntityDeleteFailedException(ex.getMessage());
        }
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
    public Image updateFileById(MultipartFile file, String name, String category, String id) {
        Image imageEntity = imageRepository.findById(UUID.fromString(id))
                .orElseThrow(EntityNotFoundException::new);
        String fullFileName = MapperUtils.toFullFileName(file, name);
        if (!imageEntity.getName().equals(fullFileName) && imageRepository.existsByName(fullFileName)) {
            throw new EntityUpdateFailedException("Name is existed");
        }
        String path = MapperUtils.toAbsolutePath(rootLocation, imageEntity.getName());
        Image imageResponse = store(file, fullFileName, category, imageEntity);
        try {
            Files.delete(Paths.get(path));
            return imageResponse;
        } catch (IOException ex) {
            throw new EntityDeleteFailedException(ex.getMessage());
        }
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
