package com.ramsay.gordon.web.handler;

import com.ramsay.gordon.domain.RecipeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle Controller Exception
     * @param ex
     * @return
     */
    @ExceptionHandler(RecipeNotFoundException.class)
    public ProblemDetail handleRecipeNotFoundException(RecipeNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recipe Not Found");
        return problemDetail;
    }

}
