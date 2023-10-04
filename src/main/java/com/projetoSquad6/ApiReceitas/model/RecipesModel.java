package com.projetoSquad6.ApiReceitas.model;

import com.projetoSquad6.ApiReceitas.enums.RecipeEnum;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "tb_recipes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipesModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "ingredients", length = 5000)
  private String ingredients;
  @Column(name = "methodPreparation", nullable = false)
  private String methodPreparation;
  @Enumerated(EnumType.STRING)
  @NotNull
  private RecipeEnum classification;

  public void setIngredients(List<String> ingredients) {
    this.ingredients = String.join(", ", ingredients);
  }

  public List<String> getIngredients() {
    return Arrays.asList(ingredients.split(", "));
  }

}
