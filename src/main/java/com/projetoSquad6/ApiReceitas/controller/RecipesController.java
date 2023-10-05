package com.projetoSquad6.ApiReceitas.controller;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;

import com.projetoSquad6.ApiReceitas.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/recipes")
public class RecipesController {
    @Autowired
    RecipesService recipesService;

    @PostMapping
    public ResponseEntity<?> recipieDatabase(@RequestBody @Validated RecipesModel recipesModel) {
        RecipesDto newRecipe = recipesService.createRecipe(recipesModel);

        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipesDto>> displayAllRecipes() {
        return ResponseEntity.ok(recipesService.findAll());
    }

    @GetMapping(path = "/findByIngredient")
    public ResponseEntity<List<RecipesModel>>searchByIngredients(@RequestParam("nome") String nome){
        return null;
    }

    @GetMapping(path = "/findByName")
    public ResponseEntity<List<RecipesDto>>searchByNameRecipies(@RequestParam("name") List<String> name){
        List<RecipesDto> recipes = recipesService.findByName(name);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping(path = "/findByRestriction")
    public ResponseEntity<List<RecipesModel>>searchByRestriction(@RequestParam("nome") String nome){
        return null;
    }

    @DeleteMapping(path = "/deleteByName")
    public ResponseEntity deleteByName(@RequestParam("name") String name){
       recipesService.deleteByName(name);
       return ResponseEntity.ok("Deletado com sucesso");
    }


    @PutMapping(path = "/updateByName")
    public  ResponseEntity<?>updateRecipies(@RequestParam("name") String name ,
                                                     @RequestBody RecipesDto recipesDto){
        return ResponseEntity.ok(recipesService.updateRecipe(name, recipesDto));
    }

}
