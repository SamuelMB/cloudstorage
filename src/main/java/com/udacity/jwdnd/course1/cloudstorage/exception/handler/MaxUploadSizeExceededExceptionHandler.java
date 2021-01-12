package com.udacity.jwdnd.course1.cloudstorage.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MaxUploadSizeExceededExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException exception,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("result");
        modelAndView.getModel().put("errorMessage", "Maximum upload size exceeded (limit 10MB). ");
        return modelAndView;
    }


}
