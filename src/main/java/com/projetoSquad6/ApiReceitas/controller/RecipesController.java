package com.projetoSquad6.ApiReceitas.controller;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/recipes")
public class recipesController {

    @PostMapping
    public ResponseEntity<RecipesModel> recipieDatabase(@RequestBody RecipesDto recipesDto) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<RecipesModel>> displayAllRecipes() {
        return null;
    }
  
    @GetMapping(path = "/findByName")
    public ResponseEntity<List<RecipesModel>>searchByNameRecipies(@RequestParam("nome") String nome){
        return null;
    }

    @GetMapping(path = "/findByIngredient")
    public ResponseEntity<List<RecipesModel>>searchByNameRecipies(@RequestParam("nome") List<String> nomes){
        return null;
    }

    @GetMapping(path = "/findByIRestriction")
    public ResponseEntity<List<RecipesModel>>searchByRestriction(@RequestParam("nome") List<String> nomes){
        return null;
    }

    @DeleteMapping(path = "/deleteByName")
    public ResponseEntity<Objects>deleteByName(@RequestParam("name") String name){
        return null;
    }

    @PutMapping(path = "/updateByName")
    public ResponseEntity<RecipesModel>updateRecipies(@RequestParam("name") String name ,
                                                     @RequestBody RecipesDto recipesDto){
        return null;
    }
}
