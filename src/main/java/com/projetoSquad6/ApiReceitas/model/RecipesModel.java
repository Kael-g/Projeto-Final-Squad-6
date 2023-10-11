package com.projetoSquad6.ApiReceitas.model;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "tb_recipes")
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class RecipesModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name", nullable = false , unique = true, columnDefinition = "text")
  private String name;
  @Column(name = "ingredients", nullable = false, length = 5000)
  private String ingredients;
  @Column(name = "methodPreparation", nullable = false)
  private String methodPreparation;
  @ElementCollection(targetClass = ClassificationEnum.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "classifications", joinColumns = @JoinColumn(name = "idRecipe"))
  private List<ClassificationEnum> classifications;

  public void setIngredients(List<String> ingredients) {
    this.ingredients = String.join(", ", ingredients);
  }

  public List<String> getIngredients() {
    return Arrays.asList(ingredients.split(", "));
  }

}
