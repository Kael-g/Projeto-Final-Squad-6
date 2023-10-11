package com.projetoSquad6.ApiReceitas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HandleRecipeNoExistsByName extends RuntimeException{

    public HandleRecipeNoExistsByName(String message){
        super(message);
    }
}
