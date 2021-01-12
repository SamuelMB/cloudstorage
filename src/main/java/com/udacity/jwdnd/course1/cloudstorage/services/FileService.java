package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxUploadSize;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int upload(File file, String username) throws BusinessException {
        User user = userService.getUser(username);
        if (user != null) {
            file.setUserId(user.getUserId());
            if(fileExists(file.getFileName(), user.getUserId())) {
               throw new BusinessException("File with this name already exists.");
            }
            return fileMapper.insert(file);
        } else {
            throw new BusinessException("User not found!");
        }
    }

    private boolean fileExists(String fileName, Long userId) {
        File file = fileMapper.getByName(fileName, userId);
        if(file == null) {
            return false;
        }
        return fileName.equals(file.getFileName());
    }

    public List<File> getAll(String username) {
        User user = userService.getUser(username);
        if(user != null) {
            return fileMapper.getAll(user.getUserId());
        }
        return new ArrayList<>();
    }

    public File getById(Long fileId) {
        return fileMapper.getById(fileId);
    }

    public int delete(Long fileId) {
        return fileMapper.delete(fileId);
    }
}
