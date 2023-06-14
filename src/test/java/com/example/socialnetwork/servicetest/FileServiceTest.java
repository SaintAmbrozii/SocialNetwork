package com.example.socialnetwork.servicetest;

import com.example.socialnetwork.domain.Image;
import com.example.socialnetwork.repo.ImageRepo;
import com.example.socialnetwork.service.FileService;
import lombok.val;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.example.socialnetwork.Const.IMAGE_MOCK_MULTIPART_FILE;

@RunWith(SpringRunner.class)
public class FileServiceTest {

    @Value("${app.file.upload-dir}")
    Path fileStorageLocation;

    @Mock
    ImageRepo imageRepo;

    @MockBean
    FileService fileService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void addFile() throws IOException {
        MultipartFile file = IMAGE_MOCK_MULTIPART_FILE;

        String fileName = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
              Assert.assertNotNull(Files.exists(fileStorageLocation.resolve(file.getName())));

    }

    private String getFileData(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(resultFilename)
                .toUriString();
        return uri;

    }
}
