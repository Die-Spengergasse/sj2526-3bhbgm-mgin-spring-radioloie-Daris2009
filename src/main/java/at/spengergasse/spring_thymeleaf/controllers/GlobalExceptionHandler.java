package at.spengergasse.spring_thymeleaf.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataAccessException.class, SQLException.class})
    public String handleDatabaseError(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Cant connect to Database please try again");
        return "error-page";
    }
}
