package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.repo.ImageRepo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final FileService fileService;
    private final ImageRepo imageRepo;

    public ImageService(FileService fileService, ImageRepo imageRepo) {
        this.fileService = fileService;
        this.imageRepo = imageRepo;
    }

    public void deleteImage(Image image) {
        imageRepo.delete(image);
    }
    public List<Image> getImages() {
        return imageRepo.findAll();
    }
    public Optional<Image> getById(Long id) {

        return imageRepo.findById(id);
    }
}
