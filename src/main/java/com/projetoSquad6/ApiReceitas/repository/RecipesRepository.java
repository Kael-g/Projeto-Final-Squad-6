package com.projetoSquad6.ApiReceitas.repository;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<RecipesModel, Long> {

@Query("SELECT r FROM RecipesModel r WHERE LOWER(r.name) IN :names")
List<RecipesModel> findByName(@Param("names") List<String> names);

    void deleteByName(String name);
}
