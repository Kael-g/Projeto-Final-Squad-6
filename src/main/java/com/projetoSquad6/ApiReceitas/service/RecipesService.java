package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.exceptions.HandleNoFoundIngredients;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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


    public List<RecipesDto> findAll() {
        List<RecipesModel> recipes = recipesRepository.findAll();
        List<RecipesDto> recipesDtos = new ArrayList<>();

        for (RecipesModel recipesModel : recipes) {
            recipesDtos.add(recipesMapper.toRecipesDto(recipesModel));
        }
        return recipesDtos;
    }

    public RecipesDto createRecipe(RecipesModel recipesModel) {

        if (recipesRepository.findByNameIgnoreCase(recipesModel.getName()).isPresent()) {
            throw new HandleRecipeExistsByName("Já existe uma receita com esse nome: " + recipesModel.getName());
        }
        recipesRepository.save(recipesModel);
        return recipesMapper.toRecipesDto(recipesModel);
    }


    public List<RecipesDto> findByName(List<String> name) {
        List<String> ignoreCaseName = name.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        List<RecipesModel> recipes = recipesRepository.findByName(ignoreCaseName);

        if (recipes.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }
        return recipes.stream()
                .map(recipesMapper::toRecipesDto)
                .collect(Collectors.toList());
    }

    public void deleteByName(String name) {
        Optional<RecipesModel> recipesModelOptional = recipesRepository.findByNameIgnoreCase(name);
        if (recipesModelOptional.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }
        recipesRepository.deleteByName(name);
    }

    public RecipesDto updateRecipe(String name, RecipesDto recipesDto) {
        Optional<RecipesModel> recipesModelOptional = recipesRepository.findByNameIgnoreCase(name);
        if (recipesModelOptional.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Não existe receita com esse nome ");
        }

        RecipesModel recipe = recipesModelOptional.get();
        if (recipesDto.getName() != null) {
            if (recipesRepository.findByNameIgnoreCase(recipesDto.getName()).isPresent()) {
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

    public List<RecipesDto> searchByIngredient(List<String> ingredients) {
        if (ingredients.size() > 0) {
            List<RecipesModel> recipesModels = recipesRepository.findAll();
            List<RecipesModel> filteredRecipes = recipesModels.stream().filter(recipesModel ->
                            containAllingredients(recipesModel, ingredients))
                    .collect(Collectors.toList());
            if (filteredRecipes.isEmpty()) {
                throw new HandleNoFoundIngredients("Não existe receita somente com esses ingredientes " + ingredients);
            }

            return filteredRecipes.stream().map(recipesMapper::toRecipesDto).collect(Collectors.toList());
        }
        throw new HandleRecipeExistsByName("A busca está vazia, favor insira os ingredientes para busca");
    }

    private boolean containAllingredients(RecipesModel recipesModel, List<String> ingredients) {
        List<String> ingredientsToLowerCase = ingredients.stream().map(String::toLowerCase).collect(Collectors.toList());
        List<String> ingredientsIn = recipesModel.getIngredients().stream().map(String::toLowerCase).collect(Collectors.toList());
        return ingredientsToLowerCase.containsAll(ingredientsIn);
    }

    public List<RecipesDto> findByIngredients(List<String> ingredients) {
        List<RecipesDto> recipes = new ArrayList<>();
        if (ingredients.size() > 0) {

            for (String ingredient : ingredients) {
                List<RecipesModel> ingredientsModels = recipesRepository.findByIngredients(ingredient.toLowerCase());

                if (!ingredientsModels.isEmpty()) {
                    recipes.addAll(ingredientsModels.stream()
                            .map(recipesMapper::toRecipesDto)
                            .collect(Collectors.toList()));
                }
            }

            if (recipes.isEmpty()) {
                throw new HandleNoFoundIngredients("Não existe receita com esse ingrediente ");
            }
            return recipes;
        }

            throw new HandleRecipeExistsByName("A busca está vazia, favor insira os ingredientes para busca");
        }



}
