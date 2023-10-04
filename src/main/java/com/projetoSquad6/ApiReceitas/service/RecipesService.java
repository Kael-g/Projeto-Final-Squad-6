package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        if (recipesRepository.findByNameValidation(recipesModel.getName().toLowerCase()).isPresent()) {
            throw new HandleRecipeExistsByName("Já existe uma receita com esse nome: " + recipesModel.getName());
        }
        recipesRepository.save(recipesModel);
        return recipesMapper.toRecipesDto(recipesModel);
    }


    public RecipesDto findByName(String name){
        recipesRepository.findByName(name);
        return null;
    }

    public void deleteByName(String name){
        recipesRepository.deleteByName(name);
    }
}
