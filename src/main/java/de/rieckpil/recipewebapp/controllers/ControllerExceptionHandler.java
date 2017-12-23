package de.rieckpil.recipewebapp.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String handleNumberFormatException(Exception exception, Model model) {

        log.error("Handling error for NumberFormatExcpetion");
        log.error(exception.getMessage());

        model.addAttribute("exception", exception);

        return "400error";

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public String handleWebExchangeBindException(Exception exception, Model model) {

        log.error("Handling error for WebExchangeBindException");
        log.error(exception.getMessage());

        model.addAttribute("exception", exception);

        return "400error";

    }

}
