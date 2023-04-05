package com.ramsay.gordon.service;

import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.web.model.RecipeRequest;
import com.ramsay.gordon.web.model.SpecialFilter;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Mono<String> addRecipe(Recipe recipeRequest);
    Mono<Void>   removeRecipe(String id);
    Mono<Recipe> updateRecipe(String id, RecipeRequest recipeRequest);
    Mono<Recipe> getRecipeWithId(String id);
    Flux<Recipe> getAllRecipeWithFoodType(Recipe.FoodType foodType, PageRequest pageRequest);
    Flux<Recipe> getAllRecipeWithServes(Integer numberOfServings, PageRequest pageRequest);
    Flux<Recipe> getAllRecipeWithSpecialFilter(SpecialFilter specialFilter);
 }
