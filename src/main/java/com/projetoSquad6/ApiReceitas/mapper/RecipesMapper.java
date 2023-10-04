package com.projetoSquad6.ApiReceitas.mapper;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import org.springframework.stereotype.Component;

@Component
public class RecipesMapper {
  public RecipesDto toRecipesDto(RecipesModel recipesModel) {
    RecipesDto recipesDto = new RecipesDto();

    recipesDto.setName(recipesModel.getName());
    recipesDto.setIngredients(recipesModel.getIngredients());
    recipesDto.setMethodPreparation(recipesModel.getMethodPreparation());
    recipesDto.setClassification(recipesModel.getClassification());

    return recipesDto;
  }
}
