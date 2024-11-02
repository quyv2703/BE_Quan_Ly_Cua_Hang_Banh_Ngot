package com.henrytran1803.BEBakeManage.recipe.repository;

import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetail;
import com.henrytran1803.BEBakeManage.recipe.entity.RecipeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, RecipeDetailId> {
    void deleteByRecipeId(int id);
    @Modifying
    @Query("DELETE FROM RecipeDetail rd WHERE rd.recipe.id = :recipeId")
    void deleteAllByRecipeId(int recipeId);
}
