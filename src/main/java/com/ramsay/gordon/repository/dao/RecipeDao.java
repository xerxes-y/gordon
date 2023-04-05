package com.ramsay.gordon.repository.dao;

import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.web.model.SpecialFilter;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecipeDao {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private static String INGREDIENT_NAME = "ingredients.name";
    private static String SERVES = "serves";
    private static String INSTRUCTION="instruction";
    private static String LANGUAGE ="en";

    /**
     * Create multiple criteria for text search and other searches
     * @param specialFilter
     * @return
     */
    public Flux<Recipe> getAllRecipeWithSpecialFilter(SpecialFilter specialFilter) {
        reactiveMongoTemplate.indexOps(Recipe.class)
                .ensureIndex(new TextIndexDefinition.TextIndexDefinitionBuilder().onFields(INSTRUCTION).build());
        Query query = new Query();
        /*SWITCH CASE*/
        if (!specialFilter.getIncludeIngredients().isEmpty() && !specialFilter.getExcludeIngredients().isEmpty())
            query.addCriteria(new Criteria().andOperator(Criteria.where(INGREDIENT_NAME).in(String.join(",", specialFilter.getIncludeIngredients())),
                    Criteria.where(INGREDIENT_NAME).nin(String.join(",", specialFilter.getExcludeIngredients()))));
        else if (!specialFilter.getIncludeIngredients().isEmpty())
            query.addCriteria(Criteria.where(INGREDIENT_NAME).in(String.join(",", specialFilter.getIncludeIngredients())));
        else if (!specialFilter.getExcludeIngredients().isEmpty())
            query.addCriteria(Criteria.where(INGREDIENT_NAME).in(String.join(",", specialFilter.getExcludeIngredients())));

        if (specialFilter.getServes() > 0)
            query.addCriteria(Criteria.where(SERVES).is(specialFilter.getServes()));
        if (!specialFilter.getInstructionSearch().isEmpty())
            query.addCriteria(TextCriteria.forLanguage(LANGUAGE).matchingAny(specialFilter.getInstructionSearch()));
        return reactiveMongoTemplate.find(query, Recipe.class);
    }
}
