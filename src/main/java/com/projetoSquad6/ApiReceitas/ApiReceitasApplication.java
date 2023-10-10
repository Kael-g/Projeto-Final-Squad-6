package com.projetoSquad6.ApiReceitas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "CookBookAPI", version = "1.0.0", description = "Api para cadastro " +
		"de receitas, funcionando como um livro de receitas"))
public class ApiReceitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiReceitasApplication.class, args);
	}

}
