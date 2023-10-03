package com.projetoSquad6.ApiReceitas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class handleRecipeExistsByName extends RuntimeException {
  public handleRecipeExistsByName(String message) {
    super(message);
  }

}
