package com.projetoSquad6.ApiReceitas.repository;

import com.projetoSquad6.ApiReceitas.model.RecipesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipesRepository extends JpaRepository<RecipesModel, Long> {
    Optional<RecipesModel> findByName(String name);

    void deleteByName(String name);
}
