package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public String upload(@RequestParam("fileUpload") MultipartFile file, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            File newFile = new File(null, file.getOriginalFilename(), file.getContentType(), file.getSize(), null, file.getBytes());
            fileService.upload(newFile, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "File uploaded!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading file!");
        } catch (BusinessException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        File file = fileService.getById(fileId);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"").body(resource);
    }

    @GetMapping("/delete/{fileId}")
    public String delete(@PathVariable Long fileId, RedirectAttributes redirectAttributes) {
        int deleted = fileService.delete(fileId);
        if(deleted == 1) {
            redirectAttributes.addFlashAttribute("successMessage", "File Deleted!");
        }
        return "redirect:/home";
    }
}
