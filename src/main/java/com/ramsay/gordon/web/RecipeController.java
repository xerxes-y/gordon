package com.ramsay.gordon.web;


import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.domain.RecipeNotFoundException;
import com.ramsay.gordon.service.RecipeService;
import com.ramsay.gordon.web.model.RecipeRequest;
import com.ramsay.gordon.web.model.SpecialFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;

@RestController()
@RequestMapping(value = "/v1/recipes")
@RequiredArgsConstructor
@Validated
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping("")
    public Mono<ResponseEntity<Void>> create(
            @RequestBody @Valid Mono<RecipeRequest> recipeRequest) {
        return recipeRequest.map(request -> Recipe.builder()
                        .name(request.getName())
                        .type(Recipe.FoodType.valueOf(request.getFoodType()))
                        .ingredients(request.getIngredients())
                        .instruction(request.getInstruction())
                        .serves(request.getServes())
                        .build())
                .flatMap(recipeService::addRecipe)
                .map(s -> created(URI.create("/v1/recipes/".concat(String.valueOf(s)))).build());
    }

    @GetMapping("/{id}")
    public Mono<Recipe> get(@PathVariable("id") String id) {
        return this.recipeService.getRecipeWithId(id)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException(id)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> update(@PathVariable("id") String id,
                               @RequestBody @Valid RecipeRequest recipeRequest) {
        return recipeService.updateRecipe(id, recipeRequest)
                .switchIfEmpty(Mono.error(new RecipeNotFoundException(id))).flatMap(recipe -> Mono.empty());
    }

    @GetMapping("/foodtype")
    public Flux<Recipe> getRecipesWiThFoodType(@RequestParam(value = "food_type", defaultValue = "VEGETARIAN") String foodType, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return recipeService.getAllRecipeWithFoodType(Recipe.FoodType.valueOf(foodType), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    @GetMapping("/serves")
    public Flux<Recipe> getRecipeWithNumberOfServes(@RequestParam(value = "serves", defaultValue = "4") Integer serves, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return recipeService.getAllRecipeWithServes(serves, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id) {
        return recipeService.removeRecipe(id);
    }

    @GetMapping("/specials")
    public Flux<Recipe> getRecipeWithMultipleFilter(@RequestParam(value = "include_ingredients", required = false, defaultValue = "") List<String> include,
                                                    @RequestParam(value = "exclude_ingredients", required = false, defaultValue = "") List<String> exclude,
                                                    @RequestParam(value = "serves", required = false, defaultValue = "0") int serves,
                                                    @RequestParam(value = "instruction_search", required = false, defaultValue = "") String instructionSearch) {
        return recipeService.getAllRecipeWithSpecialFilter(SpecialFilter.builder()
                .includeIngredients(include)
                .excludeIngredients(exclude)
                .serves(serves)
                .instructionSearch(instructionSearch)
                .build());
    }
}
