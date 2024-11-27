package com.henrytran1803.BEBakeManage.recipe.repository;

import com.henrytran1803.BEBakeManage.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    boolean existsByName(String name);
    @EntityGraph(attributePaths = {"recipeDetails"})
    List<Recipe> findAll();

    Recipe findByName(String name);

    @EntityGraph(attributePaths = {"recipeDetails"})
    Optional<Recipe> findById(int id);


}
