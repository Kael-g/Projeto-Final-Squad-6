package com.projetoSquad6.ApiReceitas.service;


import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleNoFoundIngredients;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.mapper.ClassificationMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecipesService {
    @Autowired
    RecipesRepository recipesRepository;


    @Autowired
    RecipesMapper recipesMapper;

    @Autowired
    ClassificationMapper classificationMapper;


    public List<RecipesDto> findAll() {
        List<RecipesModel> recipes = recipesRepository.findAll();

        if (recipes.isEmpty()) {
            throw new HandleRecipeNoExistsByName("Nenhuma receita cadastrada!");
        }

        List<RecipesDto> recipesDtos = new ArrayList<>();

        for (RecipesModel recipesModel : recipes) {
            recipesDtos.add(recipesMapper.toRecipesDto(recipesModel));
        }
        return recipesDtos;
    }

    public RecipesDto createRecipe(RecipesDto recipesDto) {
        RecipesModel recipesModel = new RecipesModel();

        if(isNameValid(recipesDto.getName())) {
            recipesModel.setName(recipesDto.getName());
        }

        if(isIngredientsValid(recipesDto.getIngredients())) {
            recipesModel.setIngredients(recipesDto.getIngredients());
        }

        if(isMethodPreparationValid(recipesDto.getMethodPreparation())) {
            recipesModel.setMethodPreparation(recipesDto.getMethodPreparation());
        }

        recipesModel.setClassifications(classificationsValidation(recipesDto.getClassifications()));

        return recipesMapper.toRecipesDto(recipesRepository.save(recipesModel));
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
        recipesRepository.deleteById(recipesModelOptional.get().getId());
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
        if (recipesDto.getClassifications() != null) {
            recipe.setClassifications(recipesDto.getClassifications().stream()
                    .map(classificationMapper::toEnum)
                    .collect(Collectors.toList()));
        }
        recipesRepository.save(recipe);
        return recipesMapper.toRecipesDto(recipe);
    }

    public List<RecipesDto> findByClassification(List<String> classifications) {
        if (classifications.isEmpty()){
            throw new HandleRecipeExistsByName("Busca por restrições deve conter ao menos uma restrição alimentar");
        }
        List<ClassificationEnum> classificationEnums = classifications.stream().map(classificationMapper::toEnum).collect(Collectors.toList());
        boolean matchesClassification;
        List<RecipesModel> recipes = recipesRepository.findAll();
        List<RecipesModel> recipesMatchingClassifications = new ArrayList<>();

        for (RecipesModel recipe : recipes) {
            matchesClassification = false;
            for (ClassificationEnum classification : classificationEnums) {
                if (recipe.getClassifications().contains(classification)) {
                    matchesClassification = true;
                } else {
                    matchesClassification = false;
                    break;
                }
            }
            if (matchesClassification){
                recipesMatchingClassifications.add(recipe);
            }
        }
        if (!recipesMatchingClassifications.isEmpty()) {
            return recipesMatchingClassifications.stream()
                    .map(recipesMapper::toRecipesDto)
                    .collect(Collectors.toList());
        }
        throw new HandleRecipeNoExistsByName("Não existem receitas compatíveis com a busca");
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
    private boolean isNameValid(String name){
        if (recipesRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new HandleRecipeExistsByName("Já existe uma receita com esse nome: " + name);
        }

        if (name == null || name.isEmpty() || name.isBlank()){
            throw new HandleRecipeExistsByName("Nome da receita não pode ser vazio");
        }

        return true;
    }

    private boolean isIngredientsValid(List<String> ingredients){
        if (ingredients == null || ingredients.isEmpty()){
            throw new HandleRecipeExistsByName("Receita deve conter ao menos um ingrediente");
        }

        boolean isIngredientBlank = false;

        for (String ingredient : ingredients){
            if (ingredient.isBlank()){
                isIngredientBlank = true;
                break;
            }
        }

        if (isIngredientBlank){
            throw new HandleRecipeExistsByName("Ingredientes devem conter ao menos um caractere");
        }

        return true;
    }

    private boolean isMethodPreparationValid(String methodPreparation){
        if (methodPreparation == null || methodPreparation.isEmpty()){
            throw new HandleRecipeExistsByName("Método de preparo da receita não pode ser vazio");
        }

        return true;
    }

    private List<ClassificationEnum> classificationsValidation(List<String> classificationsString){
        List<ClassificationEnum> classificationsEnum = new ArrayList<>();
        if (classificationsString == null || classificationsString.isEmpty()){
            classificationsEnum.add(ClassificationEnum.NO_CLASSIFICATION);
            return classificationsEnum;
        }
        return classificationsString.stream()
                    .map(classificationMapper::toEnum)
                    .collect(Collectors.toList());
    }

}
