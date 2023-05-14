package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.repo.ImageRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileStorageLocation;
    private final ImageRepo imageRepo;

    @Autowired
    public FileService(Environment env, ImageRepo imageRepo) {
        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./upload"))
                .toAbsolutePath().normalize();
        this.imageRepo = imageRepo;
        try {
                Files.createDirectories(this.fileStorageLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public String saveFile(MultipartFile file)  {
            String filename = file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(filename);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }


    private String getFileData(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(resultFilename)
                .toUriString();
        return uri;

    }
    public void deleteFile(Long id) throws IOException {
        Image image = imageRepo.getReferenceById(id);
        Path filepath = this.fileStorageLocation.resolve(image.getName());
        FileSystemUtils.deleteRecursively(filepath);

    }

}
