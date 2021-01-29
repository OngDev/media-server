package com.ongdev.media.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", unique = true, length = 100)
    private String name;

    @Column(name = "category", length = 10)
    private String category;

    @Column(name = "linkDownload", unique = true, length = 150)
    private String linkDownload;

    @Column(name = "linkDisplay", unique = true, length = 150)
    private String linkDisplay;
}
