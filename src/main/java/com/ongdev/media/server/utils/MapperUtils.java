package com.ongdev.media.server.utils;

import com.ongdev.media.server.controller.FileController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MapperUtils {

    public static Path toDestinationFile(String fileName, Path rootLocation) {
        return rootLocation.resolve(
                Paths.get(fileName))
                .normalize().toAbsolutePath();
    }

    public static String toFullNameFile(MultipartFile file, String name) {
        String realFileName = name + "." + Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        return VNCharacterUtils.removeAccent(realFileName);
    }

    public static String toLink(Path destinationFile) {
        return MvcUriComponentsBuilder.fromMethodName(FileController.class,
                "serveFile", destinationFile.getFileName().toString())
                .build().toUri().toString();
    }

    public static String toAbsolutePath(Path rootLocation, String name) {
        return rootLocation.toFile().getAbsolutePath() + "\\" + name;
    }
}
