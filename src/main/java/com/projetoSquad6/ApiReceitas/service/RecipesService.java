package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    @Autowired
    RecipesRepository recipesRepository;


    @Autowired
    RecipesMapper recipesMapper;


    public List<RecipesDto> findAll(){
        List<RecipesModel> recipesModel = recipesRepository.findAll();
        return null;
    }

    public RecipesDto createRecipe(RecipesModel recipesModel){

        if (recipesRepository.findByNameValidation(recipesModel.getName().toLowerCase()).isPresent() ) {
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
        recipesRepository.deleteByName(name);
    }
}
