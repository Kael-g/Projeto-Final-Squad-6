package com.projetoSquad6.ApiReceitas.mapper;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import com.projetoSquad6.ApiReceitas.exceptions.HandleRecipeExistsByName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ClassificationMapperTest {

  private final ClassificationMapper classificationMapper = new ClassificationMapper();

  @DisplayName("should convert a string to an enum")
  @Test
  public void testStringToEnum(){
    ClassificationEnum result = classificationMapper.toEnum("VEGAN");
    assertEquals(ClassificationEnum.VEGAN, result);
  }

  @DisplayName("Should return a message if a string is invalid")
  @Test
  public void testStringInvalidToEnum() {
    assertThrows(HandleRecipeExistsByName.class, () -> {
      classificationMapper.toEnum("NOT_ENUM");
    });
  }

  @DisplayName("Should convert a enum to an string")
  @Test
  public void testEnumToString() {
    List<ClassificationEnum> enumsList = Arrays.asList(ClassificationEnum.VEGAN,
        ClassificationEnum.VEGETARIAN);
    List<String> result = classificationMapper.enumsToString(enumsList);

    assertEquals(Arrays.asList("VEGAN", "VEGETARIAN"), result);
  }
}
