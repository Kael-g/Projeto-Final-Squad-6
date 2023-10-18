package com.projetoSquad6.ApiReceitas.mapper;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipesMapperTest {
  @InjectMocks
  private RecipesMapper recipesMapper;
  @Mock
  private ClassificationMapper classificationMapper;

  @DisplayName("Should convert a file to a dto")
  @Test
  public void testRecipeToDto() {
    List<ClassificationEnum> classifications = new ArrayList<>();
    classifications.add(ClassificationEnum.VEGAN);
    classifications.add(ClassificationEnum.VEGETARIAN);
    when(classificationMapper.enumsToString(classifications)).thenReturn(Arrays.asList("VEGAN", "VEGETARIAN"));

    RecipesModel recipe = new RecipesModel();
    recipe.setId(1L);
    recipe.setName("receita");
    recipe.setIngredients(Arrays.asList("ingre1", "ingre2", "ingre3"));
    recipe.setMethodPreparation("Modo de preparo");
    recipe.setClassifications(Arrays.asList(ClassificationEnum.VEGAN, ClassificationEnum.VEGETARIAN));

    RecipesDto recipesDto = recipesMapper.toRecipesDto(recipe);

    assertEquals("receita", recipesDto.getName());
    assertEquals(Arrays.asList("ingre1", "ingre2", "ingre3"), recipesDto.getIngredients());
    assertEquals("Modo de preparo", recipesDto.getMethodPreparation());
    assertEquals(Arrays.asList("VEGAN", "VEGETARIAN"), recipesDto.getClassifications());
  }
}
