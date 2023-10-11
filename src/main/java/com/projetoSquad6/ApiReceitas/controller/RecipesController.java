package com.projetoSquad6.ApiReceitas.controller;

import com.projetoSquad6.ApiReceitas.exceptions.HandleNoFoundIngredients;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;

import com.projetoSquad6.ApiReceitas.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/recipes")
public class RecipesController {
    @Autowired
    RecipesService recipesService;

    @PostMapping
    public ResponseEntity<?> recipieDatabase(@RequestBody RecipesDto recipesDto) {
        RecipesDto newRecipe = recipesService.createRecipe(recipesDto);

        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipesDto>> displayAllRecipes() {
        return ResponseEntity.ok(recipesService.findAll());
    }

    @GetMapping("/ingredients")
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

    @GetMapping("/")
    public ResponseEntity<List<RecipesDto>> searchByNameRecipies(@RequestParam("name") List<String> name) {
        List<RecipesDto> recipes = recipesService.findByName(name);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping(path = "/classification")
    public ResponseEntity<List<RecipesDto>> serchByClassification(@RequestParam("classification") List<String> classifications){
        List<RecipesDto> recipes = recipesService.findByClassification(classifications);
        return ResponseEntity.ok(recipes);
    }

    @DeleteMapping("/")
    public ResponseEntity deleteByName(@RequestParam("name") String name) {
        recipesService.deleteByName(name);
        return ResponseEntity.ok("Deletado com sucesso");
    }

    @PutMapping("/")
    public ResponseEntity<?> updateRecipies(@RequestParam("name") String name,
                                            @RequestBody RecipesDto recipesDto) {
        return ResponseEntity.ok(recipesService.updateRecipe(name, recipesDto));
    }

}
