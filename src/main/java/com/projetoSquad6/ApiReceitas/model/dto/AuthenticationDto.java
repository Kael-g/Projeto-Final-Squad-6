package com.projetoSquad6.ApiReceitas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationDto {
    private String login;
    private String password;
}
