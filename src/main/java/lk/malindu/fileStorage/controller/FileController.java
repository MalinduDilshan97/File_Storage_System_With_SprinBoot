package lk.malindu.fileStorage.controller;

import lk.malindu.fileStorage.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping("/File")
public class FileController {

    @Autowired
    private FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/upload")
    public ResponseEntity fileUpload(@RequestParam MultipartFile file){
        boolean result=fileService.saveFile(file);
        if (result){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/{id:.+}")
    public ResponseEntity<Resource> download(@PathVariable int id, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.searchFile(id);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        // crop description return with eTag inside HTTP Header
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @DeleteMapping("/remove/{id:.+}")
    public ResponseEntity delete(@PathVariable int id, HttpServletRequest request) {
        boolean result=fileService.deleteFile(id);
        if (result){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }

    }

}
