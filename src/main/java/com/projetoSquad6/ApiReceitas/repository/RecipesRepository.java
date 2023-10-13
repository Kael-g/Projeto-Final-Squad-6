package com.projetoSquad6.ApiReceitas.repository;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<RecipesModel, Long> {

    @Query("SELECT r FROM RecipesModel r WHERE LOWER(r.name) = LOWER(?1)")
    Optional<RecipesModel> findByNameIgnoreCase(String name);

    @Query("SELECT r FROM RecipesModel r WHERE LOWER(r.name) IN :names")
    List<RecipesModel> findByName(@Param("names") List<String> names);

    @Transactional
    @Modifying
    @Query("DELETE FROM RecipesModel r WHERE LOWER(r.name) = :name")
    void deleteByName(@Param("name") String name);

    @Query("SELECT r FROM RecipesModel r WHERE LOWER(r.ingredients) LIKE %:ingredient%")
    List<RecipesModel> findByIngredients(@Param("ingredient") String ingredient);

}
