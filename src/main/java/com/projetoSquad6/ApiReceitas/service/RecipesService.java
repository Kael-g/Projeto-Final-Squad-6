package com.projetoSquad6.ApiReceitas.service;

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


    public List<RecipesDto> findAll(){
        List<RecipesModel> recipesModel = recipesRepository.findAll();
        return null;
    }

    public RecipesModel createRecipe(RecipesModel recipesModel){
        return recipesRepository.save(recipesModel);
    }

    public List<RecipesModel> findByName(List<String> name){
        List<String> ignoreCaseName = name.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return recipesRepository.findByName(ignoreCaseName);
    }

    public void deleteByName(String name){
        recipesRepository.deleteByName(name);
    }
}
