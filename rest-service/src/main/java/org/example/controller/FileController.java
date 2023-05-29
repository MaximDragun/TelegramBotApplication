package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.model.ApplicationDocument;
import org.example.model.ApplicationPhoto;
import org.example.model.BinaryContent;
import org.example.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/download_file")
@RestController
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-document")
    public void getDocument(@RequestParam("id") String id, HttpServletResponse response) {
        Optional<ApplicationDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ApplicationDocument applicationDocument = doc.get();
        response.setContentType(MediaType.parseMediaType(applicationDocument.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + applicationDocument.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        BinaryContent binaryContent = applicationDocument.getBinaryContent();
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(binaryContent.getFileAsArrayOfBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-photo")
    public void getPhoto(@RequestParam("id") String id, HttpServletResponse response) {
        Optional<ApplicationPhoto> photo = fileService.getPhoto(id);
        if (photo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ApplicationPhoto applicationPhoto = photo.get();
        String randomPhotoName = RandomStringUtils.randomAlphanumeric(11, 11);
        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment; filename=" + randomPhotoName + ".jpg");
        response.setStatus(HttpServletResponse.SC_OK);
        BinaryContent binaryContent = applicationPhoto.getBinaryContent();
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

