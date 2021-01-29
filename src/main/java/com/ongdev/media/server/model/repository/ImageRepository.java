package com.ongdev.media.server.model.repository;

import com.ongdev.media.server.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<Image> findByName(String name);

    Optional<Image> findByLinkDisplay(String link_display);

    Optional<Image> findByLinkDownload(String link_download);

    Boolean existsByName(String name);
}
