package com.projetoSquad6.ApiReceitas.enums;

import lombok.Getter;

@Getter
public enum ClassificationEnum {
  VEGAN("vegan"),
  VEGETARIAN("vegetarian"),
  LACTOSE_FREE("lactose free"),
  GLUTEN_FREE("gluten free"),
  NO_CLASSIFICATION("no classification");

  private String classification;

  ClassificationEnum(String classification){
    this.classification = classification;
  }

}