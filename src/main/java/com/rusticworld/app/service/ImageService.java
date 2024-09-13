package com.rusticworld.app.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private String getImageDirectory() {
        return "C:/Users/donpe/OneDrive/Escritorio/RusticWorld/rustic-world-app/public/image";
    }

    public String getUrlImage(MultipartFile image, String nameImage) {
        String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = nameImage + extension;
        Path imagePath = Paths.get(getImageDirectory(), fileName);

        try {
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            String formattedPath = "/image/" + fileName.replace(" ", "%20");
            return formattedPath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving the image", e);
        }
    }
}
