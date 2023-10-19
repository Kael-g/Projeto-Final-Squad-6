package com.projetoSquad6.ApiReceitas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  @org.springframework.web.bind.annotation.ExceptionHandler(HandleRecipeExistsByName.class)
  public ResponseEntity<Object> handleRecipeExistsByName(HandleRecipeExistsByName e) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
  
  @org.springframework.web.bind.annotation.ExceptionHandler(HandleRecipeNoExistsByName.class)
  public ResponseEntity<Object>handleRecipeNoExistsByName(HandleRecipeNoExistsByName e){

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }
  @org.springframework.web.bind.annotation.ExceptionHandler(HandleNoFoundIngredients.class)
  public ResponseEntity<Object> handleNoFoundIngredients(HandleNoFoundIngredients e) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(HandleWhenNoHasRecipe.class)
  public ResponseEntity<Object> handleWhenNoHasRecipe(HandleWhenNoHasRecipe e) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", new Date());
    body.put("message", e.getMessage());

    return new ResponseEntity<>(body, HttpStatus.OK);
  }


}
