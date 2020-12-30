package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public String createCredential(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            credentialService.createCredential(credential, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Credential Created!");
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteNote(@PathVariable Long credentialId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        try {
            int deleted = credentialService.deleteByCredentialId(credentialId, username);
            if(deleted > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "Credential deleted!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Problem when deleting credential");
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/home";
    }
}
