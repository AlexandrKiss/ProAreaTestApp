package co.proarea.controllers;

import co.proarea.models.DBFile;
import co.proarea.services.ProductService;
import co.proarea.services.StorageService;
import co.proarea.services.exception.StorageFileNotFoundException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/file/")
@Api(value="onlinestore", description="Operations pertaining to files in Online Store")
@Slf4j
public class FileController {
    private final ProductService productService;
    private final StorageService storageService;

    public FileController(ProductService productService, StorageService storageService) {
        this.productService = productService;
        this.storageService = storageService;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("upload")
    public DBFile fileUpload(@RequestParam("file") MultipartFile file,
                             @RequestParam("product_unit_ean") long ean) {
        return storageService.store(file, productService.getProductUnit(ean), "/api/v1/file/download/");
    }

    @GetMapping("get/{fileID}")
    public DBFile getFileObject(@PathVariable("fileID") long fileID) {
        return storageService.getFileObject(fileID);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("all")
    public List<DBFile> getFileObjects() {
        return storageService.getFileObjects();
    }

    @GetMapping("download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long fileId) {
        return storageService.getFile(fileId);
    }

    @DeleteMapping("delete/{fileId}")
    public boolean deleteFile(@PathVariable long fileId) {
        return storageService.delete(fileId);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
