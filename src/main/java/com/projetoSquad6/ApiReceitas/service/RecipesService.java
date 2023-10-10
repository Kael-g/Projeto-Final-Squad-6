package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
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

import java.util.ArrayList;
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
        recipesRepository.deleteByName(name);
    }

    public RecipesDto updateRecipe(String name, RecipesDto recipesDto) {
        Optional<RecipesModel> recipesModelOptional = recipesRepository.findByNameIgnoreCase(name);
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
        if (recipesDto.getClassifications() != null) {
            recipe.setClassifications(recipesDto.getClassifications());
        }
        recipesRepository.save(recipe);
        return recipesMapper.toRecipesDto(recipe);
    }

    public List<RecipesDto> findByClassification(List<String> classifications) {
        if (classifications.isEmpty()){
            throw new HandleRecipeExistsByName("Busca por restrições deve conter ao menos uma restrição alimentar");
        }
        List<RecipesModel> recipes = recipesRepository.findAll();
        List<RecipesModel> recipesMatchingClassifications = new ArrayList<>();
        List<ClassificationEnum> classificationEnums = classifications.stream().map(classificationMapper::toEnum).collect(Collectors.toList());
        boolean matchesClassification;

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
}
