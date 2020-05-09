package co.proarea.services;

import co.proarea.models.DBFile;
import co.proarea.models.ProductUnit;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    DBFile store(MultipartFile file, ProductUnit productUnit, String servletPath);

    DBFile getFileObject(long fileID);

    ResponseEntity<Resource> getFile(long fileID);

    List<DBFile> getFileObjects();

    boolean delete(long fileID);

}