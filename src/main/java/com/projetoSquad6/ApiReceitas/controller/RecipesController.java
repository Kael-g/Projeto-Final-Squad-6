package com.projetoSquad6.ApiReceitas.controller;

import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.service.RecipesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipes")
@Tag(name = "CookBookAPI")
public class RecipesController {
    @Autowired
    RecipesService recipesService;

    @Operation(summary = "Registro de receitas",
        description = "Esse endpoint é responsável pelo cadastro de receitas, onde deve ser passado pelo" +
        " body o nome, a lista de ingredientes, o modo de preparo e a classificação da receita. Em caso de " +
        "já existir uma receita com o mesmo nome cadastrada, uma mensagem será retornada.", method = "POST")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receita cadastrada com sucesso", content = {
            @Content(examples = {
                @ExampleObject(name = "Receita cadastrada",
                summary = "Response após cadastrar uma receita",
                value = "[{\"name\": \"nome\", \"ingredients\": [\"str1\", \"str2\", \"str3\"], " +
                    "\"methodPreparation\": \"string\", \"classification\": \"vegan\"}]")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE)
        }),
        @ApiResponse(responseCode = "400", description = "Já existe uma receita com esse nome", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Já existe uma " +
                "receita com esse nome"))
        })
    })
    @PostMapping
    public ResponseEntity<?> recipieDatabase(@RequestBody RecipesDto recipesDto) {
        RecipesDto newRecipe = recipesService.createRecipe(recipesDto);

        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar receitas cadastradas",
        description = "Esse endpoint é o responsável por listar todas as receitas cadastradas, " +
        "retornando todas as informações das receitas.", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Nenhuma receita cadastrada.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Nenhuma receita " +
                "cadastrada."))
        })
    })
    @GetMapping
    public ResponseEntity<List<RecipesDto>> displayAllRecipes() {
        return ResponseEntity.ok(recipesService.findAll());
    }

    @Operation(summary = "Busca através de ingredientes",
    description = "Com esse endpoint é possível fazer dois tipos de busca, por receitas que contenham uma " +
        "lista de ingredientes específicos, ou por receitas que possam conter mais ingredientes além " +
        "daqueles buscados, para isso é preciso passar a lista de ingredientes como parâmetro na url da " +
        "requisição, e caso seja a busca por uma receita que possa contem mais ingredientes além dos " +
        "buscados, é preciso adicionar o parâmetro 'exact' no searchType da url.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Não existe receita somente com esses " +
            "ingredientes", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Não existe " +
                "receita somente com esses ingredientes."))
        }),
        @ApiResponse(responseCode = "400", description = "A busca está vazia, favor insira os ingredientes " +
            "para busca", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: A busca está " +
                "vazia, por favor insira os ingredientes para efetuar a busca."))
        })
    })
    @GetMapping(path ="/ingredients")
    public ResponseEntity<List<RecipesDto>> searchIngredients(
            @RequestParam("ingredients") List<String> ingredients,
            @RequestParam(name = "searchType", defaultValue = "contains") String searchType) {
        List<RecipesDto> recipes;

        if ("exact".equalsIgnoreCase(searchType)) {
            recipes = recipesService.findByIngredients(ingredients);
        } else {
            recipes = recipesService.searchByIngredient(ingredients);
        }
        return ResponseEntity.ok(recipes);
    }

    @Operation(summary = "Buscar uma ou mais receitas",
        description = "Nesse endpoint é possível procurar por uma ou mais receitas apenas passando o(s) " +
        "nome(s) como um parametro na url, e ele irá retornar as receitas correspondentes.", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Não existe receita com esse nome.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Não existe " +
                "receita com esse nome."))
        })
    })
    @GetMapping(path = "/")
    public ResponseEntity<List<RecipesDto>>searchByNameRecipies(@RequestParam("name") List<String> name){
        List<RecipesDto> recipes = recipesService.findByName(name);
        return ResponseEntity.ok(recipes);
    }

    @Operation(summary = "Buscar receitas pela restrição alimentar",
    description = "Nesse endpoint é possível buscar receitas utilizando como parâmetro a restrição " +
        "alimentar, que deve ser passada como um parâmetro na url e irá retornar as receitas correspondetes" +
        ".", method = "GET")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Busca por restrições deve conter ao menos uma " +
            "restrição alimentar", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: A busca por " +
                "restrições deve conter ao menos uma restrição alimentar."))
        }),
        @ApiResponse(responseCode = "404", description = "Não existem receitas compatíveis com a busca", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Não existem " +
                "receitas compativeis com a busca."))
        })
    })
    @GetMapping(path = "/classifications")
    public ResponseEntity<List<RecipesDto>> serchByClassification(@RequestParam("classification") List<String> classifications){
        List<RecipesDto> recipes = recipesService.findByClassification(classifications);
        return ResponseEntity.ok(recipes);
    }

    @Operation(summary = "Deletar uma receita",
        description = "Este endpoint é responsável por deletar uma receita, bastando passar o nome da " +
        "receita como um parâmetro pela url. Caso não exista uma receita com esse nome, uma mensagem será " +
        "retornada.", method = "DELETE")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Deletado com sucesso.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "Deletado com sucesso."))
        }),
        @ApiResponse(responseCode = "404", description = "Não existe receita com esse nome.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Não existe " +
                "receita com esse nome."))
        }),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "message: Acesso não autorizado "
                    ))
            })
            ,
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "message: Acesso proibido. "
                    ))
            })
    })
    @DeleteMapping(path = "/")
    public ResponseEntity deleteByName(@RequestParam("name") String name){
       recipesService.deleteByName(name);
       return ResponseEntity.ok("Deletado com sucesso");
    }

    @Operation(summary = "Editar/atualizar uma receita",
        description = "Nesse endpoint é possível editar/atualizar qualquer informação de uma receita, " +
        "para isso, o nome da receita deve ser passado como um parâmetro na url, e a informação que deseja " +
        "atualizar, no corpo da requisição, caso tente atualizar o nome da receita para outro nome ja " +
        "existente no catálogo, uma mensagem será retornada", method = "PUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receita atualizada com sucesso.", content = {
            @Content(examples = {
                @ExampleObject(name = "Receita atualizada",
                    summary = "Response após atualizar uma receita",
                    value = "[{\"name\": \"nome\", \"ingredients\": [\"str1\", \"str2\", \"str3\"], " +
                        "\"methodPreparation\": \"string\", \"classification\": \"vegan\"}]")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE)
        }),
        @ApiResponse(responseCode = "400", description = "Já existe uma receita com esse nome.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Já existe uma " +
                "receita com esse nome"))
        }),
        @ApiResponse(responseCode = "404", description = "Não existe receita com esse nome.", content = {
            @Content(mediaType = "application/json", schema = @Schema(example = "message: Não existe " +
                "receita com esse nome."))
        }),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "message: Acesso não autorizado "
                    ))

            }),
            @ApiResponse(responseCode = "403", description = "Acesso proibido.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = "message: Acesso proibido. "
                    ))
            })

    })
    @PutMapping(path = "/")
    public  ResponseEntity<?>updateRecipies(@RequestParam("name") String name ,
                                                     @RequestBody RecipesDto recipesDto){
        return ResponseEntity.ok(recipesService.updateRecipe(name, recipesDto));
    }

}
