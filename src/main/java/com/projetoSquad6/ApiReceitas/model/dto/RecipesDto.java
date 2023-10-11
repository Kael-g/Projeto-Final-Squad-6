package com.projetoSquad6.ApiReceitas.model.dto;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipesDto {
  private String name;
  private List<String> ingredients;
  private String methodPreparation;
  private List<String> classifications;
}
