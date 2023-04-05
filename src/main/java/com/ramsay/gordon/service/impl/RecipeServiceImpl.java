package com.ramsay.gordon.service.impl;


import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.domain.RecipeNotFoundException;
import com.ramsay.gordon.repository.RecipeRepository;
import com.ramsay.gordon.repository.dao.RecipeDao;
import com.ramsay.gordon.service.RecipeService;
import com.ramsay.gordon.web.model.RecipeRequest;
import com.ramsay.gordon.web.model.SpecialFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor

public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeDao recipeDao;

    /**
     * Add Recipe
     * @param recipe with transactional support
     * @return
     */
    @Transactional
    @Override
    public Mono<String> addRecipe(Recipe recipe) {
        return recipeRepository.save(
                recipe).map(Recipe::getId);
    }

    /**
     * remove recipe with transactional support
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Mono<Void> removeRecipe(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException(id)))
                .flatMap(recipeRepository::delete);
    }

    /**
     * update recipe with transactional support
     * @param id
     * @param recipe
     * @return
     */
    @Transactional
    @Override
    public Mono<Recipe> updateRecipe(String id, RecipeRequest recipe) {
        return recipeRepository.findById(id).flatMap(result -> {
            result.setName(recipe.getName());
            result.setInstruction(recipe.getInstruction());
            result.setIngredients(recipe.getIngredients());
            result.setServes(recipe.getServes());
            return recipeRepository.save(result);
        });
    }

    /**
     * get one recipe
     * @param id
     * @return
     */
    @Override
    public Mono<Recipe> getRecipeWithId(String id) {
        return recipeRepository.findById(id);
    }

    /**
     *  get recipes with food type
     * @param foodType
     * @param pageRequest
     * @return
     */
    @Override
    public Flux<Recipe> getAllRecipeWithFoodType(Recipe.FoodType foodType, PageRequest pageRequest) {
        return recipeRepository.findAllByType(foodType, pageRequest);
    }

    /**
     * get all recipes with serves filter
     * @param numberOfServings
     * @param pageRequest
     * @return
     */
    @Override
    public Flux<Recipe> getAllRecipeWithServes(Integer numberOfServings, PageRequest pageRequest) {
        return recipeRepository.findAllByServes(numberOfServings, pageRequest);
    }

    /**
     * get recipes wtih multiple filter (text search,include ingredient,exclude ingredient,serves)
     * @param specialFilter
     * @return
     */
    @Override
    public Flux<Recipe> getAllRecipeWithSpecialFilter(SpecialFilter specialFilter) {
        return recipeDao.getAllRecipeWithSpecialFilter(specialFilter);
    }
}
