package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    @Autowired
    RecipesRepository recipesRepository;


    @Autowired
    RecipesMapper recipesMapper;


    public List<RecipesDto> findAll(){
        List<RecipesModel> recipes = recipesRepository.findAll();
        List<RecipesDto> recipesDtos = new ArrayList<>();

        for(RecipesModel recipesModel: recipes) {
            recipesDtos.add(recipesMapper.toRecipesDto(recipesModel));
        }
        return recipesDtos;
    }

    public RecipesDto createRecipe(RecipesModel recipesModel){

        if (recipesRepository.findByNameIgnoreCase(recipesModel.getName()).isPresent()) {
            throw new HandleRecipeExistsByName("Já existe uma receita com esse nome: " + recipesModel.getName());
        }
        recipesRepository.save(recipesModel);
        return recipesMapper.toRecipesDto(recipesModel);
    }


    public List<RecipesDto> findByName(List<String> name){
        List<String> ignoreCaseName = name.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        List<RecipesModel> recipes = recipesRepository.findByName(ignoreCaseName);

        if (recipes.isEmpty()){
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }
        return recipes.stream()
                .map(recipesMapper::toRecipesDto)
                .collect(Collectors.toList());
    }

    public void deleteByName(String name){
        Optional<RecipesModel> recipesModelOptional = recipesRepository.findByNameValidation(name);
        if (recipesModelOptional.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }
        recipesRepository.deleteByName(name);
    }

    public RecipesDto updateRecipe(String name, RecipesDto recipesDto) {
        Optional<RecipesModel> recipesModelOptional = recipesRepository.findByNameValidation(name);
        if (recipesModelOptional.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }

        RecipesModel recipe = recipesModelOptional.get();
        if (recipesDto.getName() != null) {
            if (recipesDto.getName().equalsIgnoreCase(recipe.getName())) {
                throw new HandleRecipeExistsByName("Já existe uma receita com esse nome");
            }
            recipe.setName(recipesDto.getName());
        }

        if (recipesDto.getIngredients() != null) {
            recipe.setIngredients(recipesDto.getIngredients());
        }
        if (recipesDto.getMethodPreparation() != null) {
            recipe.setMethodPreparation(recipesDto.getMethodPreparation());
        }
        if (recipesDto.getClassification() != null) {
            recipe.setClassification(recipesDto.getClassification());
        }
        recipesRepository.save(recipe);
        return recipesMapper.toRecipesDto(recipe);
    }
}
