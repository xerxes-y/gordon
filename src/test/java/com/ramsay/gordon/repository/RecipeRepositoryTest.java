package com.ramsay.gordon.repository;

import com.ramsay.gordon.BaseIt;
import com.ramsay.gordon.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class RecipeRepositoryTest extends BaseIt {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void testGetAllRecipesByPagination() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        List<Recipe> data = IntStream.range(1, 11)// 10 recipe will be created.
                .mapToObj(n -> Recipe.builder()
                        .id("" + n)
                        .name("my " + n + " first recipe")
                        .instruction("content of my " + n + " first recipe")
                        .type(Recipe.FoodType.NON_VEGETARIAN)
                        .createdDate(LocalDateTime.now())
                        .build()
                )
                .collect(toList());

        List<Recipe> data2 = IntStream.range(11, 16)// 5 recipe will be created.
                .mapToObj(n -> Recipe.builder()
                        .id("" + n)
                        .name("my " + n + " first test recipe")
                        .instruction("content of my " + n + " first recipe")
                        .type(Recipe.FoodType.VEGETARIAN)
                        .createdDate(LocalDateTime.now())
                        .build()
                )
                .collect(toList());

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        this.recipeRepository.saveAll(data)
                .thenMany(this.recipeRepository.saveAll(data2))
                .doOnTerminate(latch::countDown)
                .subscribe();
        latch.await(8000, TimeUnit.MILLISECONDS);

        this.recipeRepository.findAllByType(Recipe.FoodType.NON_VEGETARIAN, pageRequest)
                .as(StepVerifier::create)
                .expectNextCount(10)
                .verifyComplete();

        this.recipeRepository.findAll(pageRequest.getSort())
                .as(StepVerifier::create)
                .expectNextCount(15)
                .verifyComplete();

        this.recipeRepository.count().as(StepVerifier::create)
                .consumeNextWith(c -> assertThat(c).isEqualTo(15L)).verifyComplete();

    }

}
