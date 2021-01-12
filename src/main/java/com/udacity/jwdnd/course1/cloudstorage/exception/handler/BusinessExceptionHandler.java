package com.udacity.jwdnd.course1.cloudstorage.exception.handler;

import com.udacity.jwdnd.course1.cloudstorage.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessExceptionHandler(BusinessException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return "redirect:/result";
    }
}
