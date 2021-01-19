package com.ongdev.media.server;

import com.ongdev.media.server.controller.config.FileProperties;
import com.ongdev.media.server.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(FileProperties.class)
public class MediaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaServerApplication.class, args);
    }

    @Bean
    CommandLineRunner init(FileService fileService) {
        return (args) -> {
            fileService.deleteAll();
            fileService.init();
        };
    }
}
