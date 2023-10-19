package com.projetoSquad6.ApiReceitas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class HandleWhenNoHasRecipe extends RuntimeException{
  public HandleWhenNoHasRecipe(String message) {
    super(message);
  }
}
