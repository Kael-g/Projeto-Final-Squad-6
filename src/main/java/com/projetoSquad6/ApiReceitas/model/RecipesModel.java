package com.projetoSquad6.ApiReceitas.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  @Column(name = "ingredients", length = 5000)
  private String ingredients;
  @Column(name = "methodPreparation", nullable = false)
  private String methodPreparation;
  @Enumerated(EnumType.STRING)
  @ManyToMany
  @JoinTable(name = "tb_recipes_classifications",
          joinColumns = @JoinColumn (name = "id_recipe"),
          inverseJoinColumns = @JoinColumn(name = "id_classification"))
  private List<ClassificationModel> classifications;

  public void setIngredients(List<String> ingredients) {
    this.ingredients = String.join(", ", ingredients);
  }

  public List<String> getIngredients() {
    return Arrays.asList(ingredients.split(", "));
  }

}
