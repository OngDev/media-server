package com.ongdev.media.server.controller.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
@Getter
@Setter
public class FileProperties {
    private String location = "archive";
}
