package lk.malindu.fileStorage.service;

import lk.malindu.fileStorage.exception.FileNotFoundException;
import lk.malindu.fileStorage.model.File;
import lk.malindu.fileStorage.repository.FileRepository;
import lk.malindu.fileStorage.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private FileRepository fileRepository;

    public boolean saveFile(MultipartFile file){

//      creating a new Path
        String location=("/Uploads");
//      Saving and getting storage url
        String url= fileStorage.fileSave(file,location);
//      Checking Is File Saved ?
        if (url.equals("Failed")) {
            return false;
        }else {

            File file1=new File(0,url,file.getContentType());
            return fileRepository.save(file1)!=null;
        }
    }


    public Resource searchFile(int id) {
        Optional<File> optional= fileRepository.findById(id);
        if (optional.isPresent()){
            return fileStorage.getFile(optional.get().getUrl());
        }else {
            throw new FileNotFoundException("File Not Found");
        }

    }

    public boolean deleteFile(int id){

        Optional<File> optional= fileRepository.findById(id);
        if (optional.isPresent()){

           fileRepository.deleteById(id);

//          Deleting Exist file
            boolean deleted=fileStorage.deleteFile(optional.get().getUrl());
            if (!deleted){
                return false;
            }
            return true;
        }else {
            throw new FileNotFoundException("File Not Found");
        }


    }

}
