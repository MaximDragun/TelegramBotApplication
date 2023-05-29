package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/download_file")
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-document")
    public ResponseEntity<?> getDocument(@RequestParam("id") String id) {
        Optional<ApplicationDocument> doc = fileService.getDocument(id);

        if (doc.isPresent()) {
            ApplicationDocument appDoc = doc.get();
            BinaryContent binaryContent = appDoc.getBinaryContent();

            FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);

            if (fileSystemResource == null) {

                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(appDoc.getMimeType()))
                        .header("Content-disposition", "attachment; filename=" + appDoc.getDocName())
                        .body(fileSystemResource);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        Optional<ApplicationPhoto> photo = fileService.getPhoto(id);
        if (photo.isPresent()) {
            ApplicationPhoto appPhoto = photo.get();
            BinaryContent binaryContent = appPhoto.getBinaryContent();
            FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
            if (fileSystemResource == null) {
                return ResponseEntity.internalServerError().build();
            } else {
                String randomPhotoName = RandomStringUtils.randomAlphanumeric(11, 11);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header("Content-disposition", "attachment; filename=" + randomPhotoName + ".jpg")
                        .body(fileSystemResource);
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

