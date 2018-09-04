package lk.malindu.fileStorage.util;



import lk.malindu.fileStorage.exception.FileNotFoundException;
import lk.malindu.fileStorage.exception.FileStorageException;
import lk.malindu.fileStorage.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
public class FileStorage {

    private Path path;
    private Path fileStorageLocation;

    @Autowired
    public FileStorage(FileStorageProperties fileStorageProperties) {

        path = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        this.fileStorageLocation=Paths.get(path.toString());

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String fileSave(MultipartFile file,String location){

        this.fileStorageLocation=Paths.get(path.toString(),location);

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();

        } catch (IOException ex) {
            return "Failed";
        }

    }

    public Resource getFile(String location){

        String path = location;
        String[] parts = path.split("/");
        String fileName = parts[parts.length-1];

        this.fileStorageLocation=Paths.get(location);

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }

    public boolean deleteFile(String url){

        Path filePath = Paths.get(url);
        File file = new File(String.valueOf(filePath));
        file.delete();
        return true;

    }

}
