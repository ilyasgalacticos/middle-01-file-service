package kz.bitlab.minio.fileservice.controllers;

import kz.bitlab.minio.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file){
        return fileService.uploadFile(file);
    }

    @GetMapping(value = "/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String fileName){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                .body(fileService.downloadFile(fileName));
    }
}