package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int upload(File file, String username) {
        User user = userService.getUser(username);
        if (user != null) {
            file.setUserId(user.getUserId());
            return fileMapper.insert(file);
        }
        return 0;
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
