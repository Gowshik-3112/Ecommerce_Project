package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadServer(String path, MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();


        //Generate a unique filename
        String randomID = UUID.randomUUID().toString();
        // mat.jpg --> 1234 --> 1234.jpg
        String fileName = randomID.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator +  fileName;

        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory created: " + file.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + file.getAbsolutePath());
            }
        }


        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
