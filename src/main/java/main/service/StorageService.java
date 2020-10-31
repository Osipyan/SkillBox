package main.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class StorageService {
    private static final List<String> ALLOWED_FORMAT = Arrays.asList(".jpg", ".png");

    static {
        File folder = new File("upload-dir");
        if(!folder.exists()) folder.mkdir();
    }

    public String upload(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        checkExtension(fileExtension);
        Path path = Path.of("upload-dir").resolve(Paths.get(UUID.randomUUID().toString() + fileExtension)).normalize();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path.toAbsolutePath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return "/" + path.toString();
    }

    public String uploadProfilePhoto(MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        checkExtension(fileExtension);
        BufferedImage image = resizeImage(file);
        Path path = Path.of("upload-dir").resolve(Paths.get(UUID.randomUUID().toString() + fileExtension)).normalize();
        if (!ImageIO.write(image, fileExtension.substring(1), path.toAbsolutePath().toFile())) {
            throw new BadRequestException(Map.of("image", "Не удалось обработать изображение"));
        }
        return "/" + path.toString();
    }

    private String getFileExtension(String path) {
        if (path != null && path.lastIndexOf('.') != -1) {
            return path.substring(path.lastIndexOf('.'));
        }
        return null;
    }

    private void checkExtension(String fileExtension) {
        if (!ALLOWED_FORMAT.contains(fileExtension)) {
            throw new ExtensionException("Допустимые форматы файлов: " + ALLOWED_FORMAT.toString());
        }
    }

    private BufferedImage resizeImage(MultipartFile photo) throws IOException {
        try (InputStream inputStream = photo.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if(image == null) {
                throw new BadRequestException(Map.of("image", "Не удалось обработать изображение"));
            }
            return Scalr.resize(image, 36, 36);
        }
    }
}
