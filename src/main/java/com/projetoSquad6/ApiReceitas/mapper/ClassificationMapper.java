package com.projetoSquad6.ApiReceitas.mapper;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeNoExistsByName;
import org.springframework.stereotype.Component;

@Component
public class ClassificationMapper {
    public ClassificationEnum toEnum(String restriction) {
        try {
            return ClassificationEnum.valueOf(restriction);
        } catch (Exception e){
            throw new HandleRecipeNoExistsByName("Restrição inválida");
        }
    }
}
