package com.projetoSquad6.ApiReceitas.service;

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

    public List<RecipesDto> findAll(){
        List<RecipesModel> recipesModel = recipesRepository.findAll();
        return null;
    }

    public boolean createRecipe(RecipesModel recipesModel){
        recipesRepository.save(recipesModel);
        return true;
    }

    public RecipesDto findByName(String name){
        recipesRepository.findByName(name);
        return null;
    }

    public void deleteByName(String name){
        recipesRepository.deleteByName(name);
    }
}
