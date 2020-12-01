package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public String upload(@RequestParam("fileUpload") MultipartFile file, Authentication authentication) {
        try {
            File newFile = new File(null, file.getOriginalFilename(), file.getContentType(), file.getSize(), null, file.getBytes());
            fileService.upload(newFile, authentication.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "home";
    }

    @GetMapping
    public String listUploadedFiles(Model model, Authentication authentication) {
        model.addAttribute("files", fileService.getAll(authentication.getName()));

        return "home";
    }

}
