package com.projetoSquad6.ApiReceitas.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetoSquad6.ApiReceitas.controller.RecipesController;
import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import com.projetoSquad6.ApiReceitas.mapper.RecipesMapper;

import com.projetoSquad6.ApiReceitas.model.dto.RecipesDto;
import com.projetoSquad6.ApiReceitas.repository.UserRepository;
import com.projetoSquad6.ApiReceitas.security.TokenService;

import com.projetoSquad6.ApiReceitas.service.RecipesService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(controllers = RecipesController.class)
public class RecipesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipesService recipesService;

    @MockBean
    private TokenService tokenService;


    @MockBean
    private RecipesMapper recipesMapper;

    @MockBean
    private UserRepository userRepository;





    @Test
    public void recipieDatabaseTest_Valid() throws Exception{
        RecipesDto validRecipe = new RecipesDto();

        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Ingrediente 1");
        ingredientsList.add("Ingrediente 2");

        List<String> classificationString = new ArrayList<>();
        classificationString.add(ClassificationEnum.VEGAN.name());

        validRecipe.setName("Nome da Receita");
        validRecipe.setIngredients(ingredientsList);
        validRecipe.setMethodPreparation("passo a passo do preparo");
        validRecipe.setClassifications(classificationString);

        when(recipesService.createRecipe(any())).thenReturn(validRecipe);

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validRecipe)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nome da Receita"))
                .andExpect(jsonPath("$.ingredients").value(ingredientsList))
                .andExpect(jsonPath("$.methodPreparation").value("passo a passo do preparo"))
                .andExpect(jsonPath("$.classifications").value(classificationString));

    }

    @Test
    public void findAllTest() throws Exception {
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Ingrediente 1");
        ingredientsList.add("Ingrediente 2");

        List<String> classificationString = new ArrayList<>();
        classificationString.add(ClassificationEnum.VEGAN.name());

        RecipesDto recipes1 = new RecipesDto("Receita 1",ingredientsList,
                "Bata tudo no liquificador",classificationString);
        RecipesDto recipes2 = new RecipesDto("Receita 2",ingredientsList,
                "passo a passo",classificationString);
        when(recipesService.findAll()).thenReturn(List.of(recipes1,recipes2));
        mockMvc.perform(get("/api/recipes")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Receita 1"))
                .andExpect(jsonPath("$[0].ingredients").value(ingredientsList))
                .andExpect(jsonPath("$[0].methodPreparation").value("Bata tudo no liquificador"))
                .andExpect(jsonPath("$[0].classifications").value(classificationString))
                .andExpect(jsonPath("$[1].name").value("Receita 2"))
                .andExpect(jsonPath("$[1].ingredients").value(ingredientsList))
                .andExpect(jsonPath("$[1].methodPreparation").value("passo a passo"))
                .andExpect(jsonPath("$[1].classifications").value(classificationString));

    }

    @Test
    public void searchIngredientsTest() throws Exception {
        List<String>ingredients = Arrays.asList("Ingrediente1","Ingrediente2");
        String searchType = "contains";
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Ingrediente1");
        ingredientsList.add("Ingrediente2");

        List<String> classificationString = new ArrayList<>();
        classificationString.add(ClassificationEnum.VEGAN.name());

        RecipesDto recipes1 = new RecipesDto("Receita 1",ingredientsList,
                "Bata tudo no liquificador",classificationString);

        when(recipesService.searchByIngredient(ingredients)).thenReturn(Collections.singletonList(recipes1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/ingredients")
                        .param("ingredients", "Ingrediente1", "Ingrediente2")
                        .param("searchType", searchType)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Receita 1"))
                .andExpect(jsonPath("$[0].ingredients").value(ingredientsList))
                .andExpect(jsonPath("$[0].methodPreparation").value("Bata tudo no liquificador"))
                .andExpect(jsonPath("$[0].classifications").value(classificationString));
    }

    @Test
    public void searchIngredientsTest_Exact() throws Exception {
        List<String>ingredients = Arrays.asList("Ingrediente1","Ingrediente2");
        String searchType = "exact";
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("Ingrediente1");
        ingredientsList.add("Ingrediente2");

        List<String> classificationString = new ArrayList<>();
        classificationString.add(ClassificationEnum.VEGAN.name());

        RecipesDto recipes1 = new RecipesDto("Receita 1",ingredientsList,
                "Bata tudo no liquificador",classificationString);

        when(recipesService.findByIngredients(ingredients)).thenReturn(Collections.singletonList(recipes1));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/ingredients")
                        .param("ingredients", "Ingrediente1", "Ingrediente2")
                        .param("searchType", searchType)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Receita 1"))
                .andExpect(jsonPath("$[0].ingredients").value(ingredientsList))
                .andExpect(jsonPath("$[0].methodPreparation").value("Bata tudo no liquificador"))
                .andExpect(jsonPath("$[0].classifications").value(classificationString));
    }

    @Test
    public void searchByNameRecipiesTest()throws Exception {
        String recipeName = "Receita 1";
        List<String> names = Arrays.asList(recipeName);
        RecipesDto recipe = new RecipesDto(recipeName, Arrays.asList("Ingrediente 1", "Ingrediente 2"), "Instruções", Arrays.asList("VEGAN", "VEGETARIAN"));
        List<RecipesDto> recipes = Arrays.asList(recipe);


        when(recipesService.findByName(names)).thenReturn(recipes);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/recipes/")
                        .param("name", names.toArray(new String[0]))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(recipes)));


    }
    @Test
    public void searchByName_NoRecipeFound() throws Exception {
        String recipeName = "NomeInexistente";
        doThrow(HandleRecipeNoExistsByName.class).when(recipesService).findAll();

        mockMvc.perform(get("/api/recipes?name=" + recipeName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteByNameTest() throws Exception {
        String recipeName = "Receita 1";

        doNothing().when(recipesService).deleteByName(recipeName);

        mockMvc.perform(delete("/api/recipes/")
                        .with(csrf())
                        .param("name", recipeName))
                .andExpect(status().isOk())
                .andExpect(content().string("Deletado com sucesso"));

        verify(recipesService).deleteByName(recipeName);
    }
    @Test
    public void serchByClassificationTest() throws Exception {
        List<String> classifications = Arrays.asList("VEGAN", "VEGETARIAN");

        List<String> classificationString = new ArrayList<>();
        classificationString.add(ClassificationEnum.VEGAN.name());
        classificationString.add(ClassificationEnum.VEGETARIAN.name());

        RecipesDto recipe1 = new RecipesDto("Receita 1", Arrays.asList("Ingrediente1", "Ingrediente2"),
                "Instruções de preparo", classificationString);

        when(recipesService.findByClassification(classifications)).thenReturn(Collections.singletonList(recipe1));

        mockMvc.perform(get("/api/recipes/classifications")
                        .param("classification", "VEGAN", "VEGETARIAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Receita 1"))
                .andExpect(jsonPath("$[0].ingredients").isArray())
                .andExpect(jsonPath("$[0].methodPreparation").value("Instruções de preparo"))
                .andExpect(jsonPath("$[0].classifications").isArray());
    }
    @Test
    public void updateRecipiesNameTest() throws Exception {
        String recipeName = "Receita 1";

        RecipesDto updatedRecipe = new RecipesDto(recipeName, Arrays.asList("Novo Ingrediente 1", "Novo Ingrediente 2")
                , "Novas instruções", Arrays.asList("VEGAN", "VEGETARIAN"));
        when(recipesService.updateRecipe(recipeName, updatedRecipe)).thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/recipes/")
                        .param("name", recipeName)
                        .with(csrf())
                        .content(asJsonString(updatedRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    @Test
    public void updateRecipiesTest_RecipeNotExists() throws Exception {
        String recipeName = "ReceitaInexistente";
        RecipesDto updatedRecipe = new RecipesDto(recipeName, Arrays.asList("Novo Ingrediente 1", "Novo Ingrediente 2")
                , "Novas instruções", Arrays.asList("VEGAN", "VEGETARIAN"));

        doThrow(HandleRecipeNoExistsByName.class).when(recipesService).updateRecipe(eq("ReceitaInexistente"), any(RecipesDto.class));

        mockMvc.perform(put("/api/recipes/")
                        .param("name", recipeName)
                        .with(csrf())
                        .content(asJsonString(updatedRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRecipiesTest_RecipeExistName() throws Exception {
        String recipeName = "Nome existente";
        RecipesDto updatedRecipe = new RecipesDto(recipeName, Arrays.asList("Novo Ingrediente 1", "Novo Ingrediente 2")
                , "Novas instruções", Arrays.asList("VEGAN", "VEGETARIAN"));

        doThrow(HandleRecipeExistsByName.class).when(recipesService).updateRecipe(eq("Nome existente"), any(RecipesDto.class));

        mockMvc.perform(put("/api/recipes/")
                        .param("name", recipeName)
                        .with(csrf())
                        .content(asJsonString(updatedRecipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
