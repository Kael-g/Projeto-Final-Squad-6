package com.projetoSquad6.ApiReceitas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HandleRecipeNoExistsByName extends RuntimeException{

    public HandleRecipeNoExistsByName(String message){
        super(message);
    }
}
