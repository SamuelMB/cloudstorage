package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public int createNote(Note note, String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user != null) {
            note.setUserId(user.getUserId());
            return noteMapper.insert(note);
        } else {
            throw new BusinessException("User not found!");
        }
    }

    public int updateNote(Note note, String username) throws BusinessException {
        User user = userService.getUser(username);
        Note noteRecovered = noteMapper.getByNoteId(note.getNoteId());
        if(user != null && noteRecovered != null) {
            note.setNoteId(noteRecovered.getNoteId());
            return noteMapper.update(note);
        } else {
            throw new BusinessException("User not found!");
        }
    }

    public List<Note> getAll(String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user != null) {
            return noteMapper.getAllByUserId(user.getUserId());
        } else {
            throw new BusinessException("User not found!");
        }
    }

    public int deleteByNoteId(Long noteId, String username) throws BusinessException {
        User user = userService.getUser(username);
        if(user != null) {
            return noteMapper.deleteByNoteId(noteId);
        } else {
            throw new BusinessException("User not found!");
        }
    }

}
