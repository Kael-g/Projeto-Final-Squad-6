package com.projetoSquad6.ApiReceitas.service;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.ClassificationMapper;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;
import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.RecipesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.projetoSquad6.ApiReceitas.enums.ClassificationEnum.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
  @Mock
  private RecipesRepository recipesRepository;
  @Mock
  private RecipesMapper recipesMapper;
  @Mock
  private ClassificationMapper classificationMapper;
  @InjectMocks
  private RecipesService recipesService;

  @BeforeEach
  void config() {
    MockitoAnnotations.openMocks(this);
  }

  private RecipesDto recipesDto;


  @DisplayName("Should return a list of recipes")
  @Test
  public void testListAllRecipes() {
    RecipesModel recipe1 = new RecipesModel();
    RecipesModel recipe2 = new RecipesModel();
    List<RecipesModel> recipesModelList = Arrays.asList(recipe1, recipe2);

    when(recipesRepository.findAll()).thenReturn(recipesModelList);

    List<RecipesDto> recipesDtoList = recipesService.findAll();

    assertEquals(2, recipesDtoList.size());
  }

  @DisplayName("Should register a new recipe")
  @Test
  public void testRegisterRecipe() {
    List<String> ingredients = new ArrayList<>();
    ingredients.add("ingred1");
    ingredients.add("ingred2");
    ingredients.add("ingred3");

    List<ClassificationEnum> classification = new ArrayList<>();
    classification.add(VEGAN);

    List<String> classificationToString = classificationMapper.enumsToString(classification);

    RecipesDto recipesDto = new RecipesDto("ReceitaUm", ingredients, "Method preparation", classificationToString);
    recipesService.createRecipe(recipesDto);
    RecipesDto result = recipesDto;

    assertEquals(recipesDto, result);

  }

  @DisplayName("Should return a recipe search by name")
  @Test
  public void testFindByName() {
    List<String> names = Arrays.asList("ReceitaUm", "ReceitaDois", "Receita3");

    RecipesModel recipesModel = new RecipesModel();
    RecipesDto recipesDto = new RecipesDto();

    List<RecipesModel> recipesModelList = Arrays.asList(recipesModel);
    List<RecipesDto> expectedRecipesDto = Arrays.asList(recipesDto);

    when(recipesRepository.findByName(Mockito.anyList())).thenReturn(recipesModelList);
    when(recipesMapper.toRecipesDto(recipesModel)).thenReturn(recipesDto);

    List<RecipesDto> result = recipesService.findByName(names);

    assertEquals(expectedRecipesDto, result);
  }

  @DisplayName("Should delete a recipe search by name")
  @Test
  public void testDeleteByName() {
   String name = "receita";
   RecipesModel recipeExist = new RecipesModel();

   Optional<RecipesModel> recipeExistOptional = Optional.of(recipeExist);

   when(recipesRepository.findByNameIgnoreCase(name)).thenReturn(recipeExistOptional);

   recipesService.deleteByName(name);

   verify(recipesRepository).deleteById(recipeExistOptional.get().getId());
  }

  @DisplayName("It should return an exception stating that there is no recipe with that name")
  @Test
  public void testRecipeNoExistToDelete() {
    String name = "receita";

    when(recipesRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

    assertThrows(HandleRecipeNoExistsByName.class, () -> {
      recipesService.deleteByName(name);
    });
  }

  @DisplayName("Should update a recipe")
  @Test
  public void testUpdateRecipeByName() {
    String name = "receita";
    RecipesDto updateRecipeDTO = new RecipesDto();
    RecipesModel recipesModel = new RecipesModel();
    Optional<RecipesModel> recipesModelOptional = Optional.of(recipesModel);

    when(recipesRepository.findByNameIgnoreCase(name)).thenReturn(recipesModelOptional);
    when(recipesMapper.toRecipesDto(recipesModel)).thenReturn(updateRecipeDTO);

    RecipesDto result = recipesService.updateRecipe(name, updateRecipeDTO);

    verify(recipesRepository).save(recipesModel);

    assertEquals(updateRecipeDTO, result);
  }

  @DisplayName("Should return a message when trying to update a recipe that doesn't exist")
  @Test
  public void testUpdateRecipeNoExistByName() {
    String name = "receita";
    RecipesDto recipesDto = new RecipesDto();

    when(recipesRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

    assertThrows(HandleRecipeNoExistsByName.class, () -> {
      recipesService.updateRecipe(name, recipesDto);
    });
  }

  @DisplayName("Should return a message when trying to update a recipe with a duplicate name")
  @Test
  public void testUpdateRecipeWithDuplicateName() {
    List<String> ingredients = new ArrayList<>();
    ingredients.add("ingred1");
    ingredients.add("ingred2");
    ingredients.add("ingred3");

    List<String> classification = new ArrayList<>();
    classification.add("VEGAN");
    String name = "receita";
    RecipesDto recipeDtoUpdate = new RecipesDto(name, ingredients, "modo de preparo", classification);
    RecipesModel recipesExist = new RecipesModel();
    Optional<RecipesModel> recipesModelOptional = Optional.of(recipesExist);

    when(recipesRepository.findByNameIgnoreCase(name)).thenReturn(recipesModelOptional);

    assertThrows(HandleRecipeExistsByName.class, () -> {
      recipesService.updateRecipe(name, recipeDtoUpdate);
    });
  }

  @DisplayName("Should return a recipe searched through a dietary restriction")
  @Test
  public void testFindRecipeByRestriction() {
    RecipesModel newRecipeModel = new RecipesModel();
    newRecipeModel.setName("receita");
    newRecipeModel.setClassifications(Arrays.asList(VEGAN, VEGETARIAN));

    RecipesDto newRecipeDto = new RecipesDto();
    newRecipeDto.setName("Outra receita");
    newRecipeDto.setClassifications(Arrays.asList("VEGAN", "VEGETARIAN"));

    List<String> classifications = Arrays.asList("VEGAN", "VEGETARIAN");
    List<RecipesModel> recipesModelList = Arrays.asList(newRecipeModel);
    List<RecipesDto> recipesDtoList = Arrays.asList(newRecipeDto);

    when(classificationMapper.toEnum("VEGAN")).thenReturn(VEGAN);
    when(classificationMapper.toEnum("VEGETARIAN")).thenReturn(VEGETARIAN);
    when(recipesRepository.findAll()).thenReturn(recipesModelList);
    when(recipesMapper.toRecipesDto(newRecipeModel)).thenReturn(newRecipeDto);

    List<RecipesDto> result = recipesService.findByClassification(classifications);

    assertEquals(recipesDtoList, result);
  }

  @DisplayName("It should return a message warning about an empty search")
  @Test
  public void testFindByRestrictionWithEmptySearch() {
    List<String> classification = Arrays.asList();

    assertThrows(HandleRecipeExistsByName.class, () -> {
      recipesService.findByClassification(classification);
    });
  }

  @DisplayName("It should return a message warning about an empty search")
  @Test
  public void testFindByRestrictionNoExists() {
    RecipesModel newRecipeModel = new RecipesModel();
    newRecipeModel.setId(1L);
    newRecipeModel.setClassifications(Arrays.asList(LACTOSE_FREE, GLUTEN_FREE));

    List<String> classification = Arrays.asList("VEGAN", "VEGETARIAN");
    List<RecipesModel> recipesModelList = Arrays.asList(newRecipeModel);

    when(classificationMapper.toEnum("VEGAN")).thenReturn(VEGAN);
    when(classificationMapper.toEnum("VEGETARIAN")).thenReturn(VEGETARIAN);
    when(recipesRepository.findAll()).thenReturn(recipesModelList);

    assertThrows(HandleRecipeNoExistsByName.class, () -> {
      recipesService.findByClassification(classification);
    });
  }

  @DisplayName("Should return a recipe searched for ingredients")
  @Test
  public void testFindRecipeByIngredients() {
    RecipesModel recipe = new RecipesModel();
    recipe.setIngredients(Arrays.asList("ingrediente1", "ingrediente2"));
    RecipesDto recipesDto = new RecipesDto();
    recipesDto.setIngredients(Arrays.asList("ingrediente1", "ingrediente2"));

    List<String> ingredients = Arrays.asList("ingrediente1", "ingrediente2");
    List<RecipesModel> recipesModelList = Arrays.asList(recipe);
    List<RecipesDto> recipesDtoList = Arrays.asList(recipesDto);

    when(recipesRepository.findAll()).thenReturn(recipesModelList);
    when(recipesMapper.toRecipesDto(recipe)).thenReturn(recipesDto);

    List<RecipesDto> result = recipesService.searchByIngredient(ingredients);

    assertEquals(recipesDtoList, result);
  }

  @DisplayName("should return a message if the search is empty")
  @Test
  public void testFindRecipeByIngredientsSearchEmpty() {
    List<String> ingredients = Arrays.asList();

    assertThrows(HandleRecipeExistsByName.class, () -> {
      recipesService.searchByIngredient(ingredients);
    });
  }

}
