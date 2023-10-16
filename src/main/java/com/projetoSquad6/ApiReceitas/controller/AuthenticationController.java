package com.projetoSquad6.ApiReceitas.controller;

import com.projetoSquad6.ApiReceitas.model.dto.LoginResponseDto;
import com.projetoSquad6.ApiReceitas.model.UserModel;
import com.projetoSquad6.ApiReceitas.model.dto.AuthenticationDto;
import com.projetoSquad6.ApiReceitas.repository.UserRepository;
import com.projetoSquad6.ApiReceitas.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Login do usuário",
            description = "Esse endpoint é responsável pelo Login do usuario, onde deve ser passado o" +
                    " username e a senha. Em caso de " +
                    "inserir um usuário ou senha errado, uma mensagem será retornada.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Usuário logado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "usuário logado "))
            }),
            @ApiResponse(responseCode = "401", description = "Usuario não cadastrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "usuário não cadastrado "))
            })
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((UserModel) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não cadastrado");
        }
    }

    @Operation(summary = "Cadastro de usuário",
            description = "Esse endpoint é responsável pelo cadastro de usuário, onde deve ser passado pelo" +
                    " body o login e a senha. Em caso de " +
                    "já existir um usuário com o mesmo nome cadastrado, uma mensagem será retornada.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "usuário cadastrado com sucesso "))
            }),
            @ApiResponse(responseCode = "400", description = "Usuário já existe", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "message:Usuário já existe "))
            })
    })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthenticationDto data) {
        if (this.userRepository.findByLogin(data.getLogin()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        UserModel newUser = new UserModel(data.getLogin(), encryptedPassword);

        this.userRepository.save(newUser);

        return ResponseEntity.ok("Usuário Cadastrado com sucesso.");

    }
}
