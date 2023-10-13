package com.projetoSquad6.ApiReceitas.mapper;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassificationMapper {
    public ClassificationEnum toEnum(String restriction) {
        try {
            return ClassificationEnum.valueOf(restriction);
        } catch (Exception e){
            throw new HandleRecipeExistsByName("Restrição inválida");
        }
    }

    public List<String> enumsToString(List<ClassificationEnum> classificationEnums){
        List<String> classificationsString = new ArrayList<>();
        for (ClassificationEnum classificationEnum : classificationEnums){
            classificationsString.add(classificationEnum.toString());
        }
        return classificationsString;
    }
}
