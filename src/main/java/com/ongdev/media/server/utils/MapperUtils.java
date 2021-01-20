package com.ongdev.media.server.utils;

import com.ongdev.media.server.controller.FileController;
import com.ongdev.media.server.exception.CouldNotResizeException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MapperUtils {

    public static Path toDestinationFile(String fileName, Path rootLocation) {
        return rootLocation.resolve(
                Paths.get(fileName))
                .normalize().toAbsolutePath();
    }

    public static String toFullFileName(MultipartFile file, String name) {
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

    public static boolean canResize(Path rootLocation, String fullFileName, String category) {
//                profile 180*180
//                cover 820*312
//                link 1200*630
        try {
            String fileLocation = MapperUtils.toAbsolutePath(rootLocation, fullFileName);
            switch (category) {
                case "profile":
                    Thumbnails.of(fileLocation).width(180).toFile(fileLocation);
                    break;
                case "cover":
                    Thumbnails.of(fileLocation).width(820).toFile(fileLocation);
                    break;
                case "link":
                    Thumbnails.of(fileLocation).width(1200).toFile(fileLocation);
                    break;
                default:
                    return false;
            }
            return true;
        } catch (IOException ex) {
            throw new CouldNotResizeException(ex.getMessage());
        }
    }
}
