package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping()
    public String createNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) throws BusinessException {
        if (note.getNoteId() == null) {
            noteService.createNote(note, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Note created!");
        } else {
            noteService.updateNote(note, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Note updated!");
        }
        return "redirect:/result";
    }

    @GetMapping("/update/{noteId}")
    public String editNote(@PathVariable("noteId") Long id, Model model, Authentication authentication) throws BusinessException {
        Object noteTitle = model.getAttribute("noteTitle");
        Object noteDescription = model.getAttribute("noteDescription");
        Note note = new Note();
        note.setNoteId(id);
        note.setNoteTitle((String) noteTitle);
        note.setNoteDescription((String) noteDescription);
        noteService.updateNote(note, authentication.getName());

        return "redirect:/result";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Long noteId, Authentication authentication, RedirectAttributes redirectAttributes) throws BusinessException {
        String username = authentication.getName();
        int deleted = noteService.deleteByNoteId(noteId, username);
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Note deleted!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Problem when deleting note");
        }
        return "redirect:/result";
    }
}
