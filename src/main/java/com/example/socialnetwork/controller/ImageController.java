package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.service.ImageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    @GetMapping("{id}")
    public Optional<Image> getById(@PathVariable(name = "id") Long id){
        return imageService.getById(id);
    }

    @GetMapping
    public List<Image> getImages() {
        return imageService.getImages();
    }

    @DeleteMapping("{id}")
    public void deleteImage(@PathVariable(name = "id")Image image)  {
            imageService.deleteImage(image);

    }
}
