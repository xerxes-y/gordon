package com.ramsay.gordon.repository;
import com.ramsay.gordon.domain.Recipe;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RecipeRepository extends ReactiveMongoRepository<Recipe, String> {

    Flux<Recipe> findAllByType(Recipe.FoodType type, PageRequest pageRequest);
    Flux<Recipe> findAllByServes(Integer serves,PageRequest pageRequest);

}
