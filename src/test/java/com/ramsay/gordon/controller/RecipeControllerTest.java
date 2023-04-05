
package com.ramsay.gordon.controller;

import com.ramsay.gordon.domain.Ingredient;
import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.repository.RecipeRepository;
import com.ramsay.gordon.service.impl.RecipeServiceImpl;
import com.ramsay.gordon.web.RecipeController;
import com.ramsay.gordon.web.model.RecipeRequest;
import com.ramsay.gordon.web.model.SpecialFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebFluxTest(
        controllers = RecipeController.class,
        excludeAutoConfiguration = {
                ReactiveUserDetailsServiceAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class
        }
)
@Slf4j
@DisplayName("API endpoints /recipes")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class RecipeControllerTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private RecipeRepository recipeRepository;


    @MockBean
    private RecipeServiceImpl recipeService;

    @BeforeAll
    public static void beforeAll() {
        log.debug("before all...");
    }

    @AfterAll
    public static void afterAll() {
        log.debug("after all...");
    }
    @BeforeEach
    void beforeEach() {
        log.debug("before each...");
    }
    @AfterEach
    void afterEach() {
        log.debug("after each...");
        reset(recipeRepository);
    }
    @Nested
    @DisplayName("/recipe GET")
    class GettingAllRecipe {
        @Test
        @DisplayName("should return 200 when getting recipe with food type")
        void shouldBeOkWhenGettingRecipeWithFoodType() {
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
            given(recipeService.getAllRecipeWithFoodType(Recipe.FoodType.VEGETARIAN, pageRequest))
                    .willReturn(Flux.just(
                                    Recipe.builder()
                                            .id("1")
                                            .name("this is apple")
                                            .instruction("step 1 wash it.step 2 eat it")
                                            .ingredients(List.of(Ingredient.builder().name("apple").amount("1").build()))
                                            .createdDate(LocalDateTime.now())
                                            .type(Recipe.FoodType.VEGETARIAN)
                                            .build()
                            )
                    );
            client.get().uri(uriBuilder -> uriBuilder.path("/v1/recipes/foodtype").queryParam("food_type", "VEGETARIAN").build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$[0].name").isEqualTo("this is apple")
                    .jsonPath("$[0].id").isEqualTo("1")
                    .jsonPath("$[0].instruction").isEqualTo("step 1 wash it.step 2 eat it");
            verify(recipeService, times(1)).getAllRecipeWithFoodType(Recipe.FoodType.valueOf("VEGETARIAN"), pageRequest);
            verifyNoMoreInteractions(recipeService);
        }
        @Test
        @DisplayName("should return 200 when getting recipe with SpecialSearch")
        void shouldBeOkWhenGettingRecipeWithSpecialSearch() {
            SpecialFilter filter = SpecialFilter.builder()
                    .instructionSearch("wash")
                    .excludeIngredients(List.of("potato"))
                    .includeIngredients(List.of("apple"))
                    .serves(4)
                    .build();
            given(recipeService.getAllRecipeWithSpecialFilter(filter))
                    .willReturn(Flux.just(
                                    Recipe.builder()
                                            .id("1")
                                            .name("this is apple")
                                            .serves(4)
                                            .instruction("step 1 wash it.step 2 eat it")
                                            .ingredients(List.of(Ingredient.builder().name("apple").amount("1").build()))
                                            .createdDate(LocalDateTime.now())
                                            .type(Recipe.FoodType.VEGETARIAN)
                                            .build()
                            )
                    );
            client.get().uri(uriBuilder -> uriBuilder.path("/v1/recipes/specials")
                            .queryParam("serves", "4")
                            .queryParam("include_ingredients","apple")
                            .queryParam("exclude_ingredients","potato")
                            .queryParam("instruction_search","wash").build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$[0].name").isEqualTo("this is apple")
                    .jsonPath("$[0].id").isEqualTo("1")
                    .jsonPath("$[0].instruction").isEqualTo("step 1 wash it.step 2 eat it");
            verify(recipeService, times(1)).getAllRecipeWithSpecialFilter(filter);
            verifyNoMoreInteractions(recipeService);
        }
        @Test
        @DisplayName("should return 200 when getting recipe with serves")
        void shouldBeOkWhenGettingRecipeWithServes() {
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
            given(recipeService.getAllRecipeWithServes(4, pageRequest))
                    .willReturn(
                            Flux.just(
                                    Recipe.builder()
                                            .id("1")
                                            .name("this is apple for 4")
                                            .instruction("step 1 wash it.step 2 eat it")
                                            .serves(4)
                                            .ingredients(List.of(Ingredient.builder().name("apple").amount("4").build()))
                                            .createdDate(LocalDateTime.now())
                                            .type(Recipe.FoodType.VEGETARIAN)
                                            .build()
                            )
                    );
            client.get().uri("/v1/recipes/serves")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$[0].name").isEqualTo("this is apple for 4")
                    .jsonPath("$[0].id").isEqualTo("1")
                    .jsonPath("$[0].instruction").isEqualTo("step 1 wash it.step 2 eat it");
            verify(recipeService, times(1)).getAllRecipeWithServes(4, pageRequest);
            verifyNoMoreInteractions(recipeService);
        }
        @Nested
        @DisplayName("/v1/recipes/:id GET")
        class GettingRecipeById {
            @Test
            @DisplayName("should return 200 when getting recipe by id")
            void shouldBeOkWhenGettingRecipeById() {
                given(recipeService.getRecipeWithId("1")).willReturn(
                        Mono.just(Recipe.builder().id("1").name("my first recipe").instruction("content of my first recipe")
                                .serves(3)
                                .ingredients(List.of(Ingredient.builder().name("my first ingredient").build())).build()));
                client.get()
                        .uri("/v1/recipes/1")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.name").isEqualTo("my first recipe")
                        .jsonPath("$.id").isEqualTo("1")
                        .jsonPath("$.instruction").isEqualTo("content of my first recipe");
                verify(recipeService, times(1)).getRecipeWithId(anyString());
                verifyNoMoreInteractions(recipeService);
            }
            @Test
            @DisplayName("should return 404 when getting recipe by a none existing id")
            void shouldReturn404WhenGettingRecipeByNonExistedId() {
                given(recipeService.getRecipeWithId("1")).willReturn(Mono.empty());
                client.get().uri("/v1/recipes/1")
                        .exchange()
                        .expectStatus()
                        .isNotFound();
                verify(recipeService, times(1)).getRecipeWithId(anyString());
                verifyNoMoreInteractions(recipeService);
            }
        }

        @Nested
        @DisplayName("/v1/recipes POST")
        class CreatingRecipe {

            @Test
            @DisplayName("should return 400 when creating recipe with invalid body")
            void shouldReturn400WhenCreatingRecipeWithInvalidBody() {
                RecipeRequest recipeRequest = RecipeRequest.builder().build();
                client.post()
                        .uri("/v1/recipes")
                        .body(BodyInserters.fromValue(recipeRequest))
                        .exchange().expectStatus()
                        .isEqualTo(HttpStatus.BAD_REQUEST);
                verifyNoInteractions(recipeRepository);
            }

            @Test
            @DisplayName("should return 201 when creating recipe")
            void shouldReturn201WhenCreatingRecipe() {
                RecipeRequest formData = RecipeRequest.builder()
                        .name("my first recipe")
                        .serves(3)
                        .foodType("VEGETARIAN")
                        .ingredients(List.of(Ingredient.builder().name("my first ingredient").build()))
                        .instruction("content of my first recipe")
                        .build();
                Recipe recipe = Recipe.builder()
                        .id("1")
                        .serves(3)
                        .type(Recipe.FoodType.VEGETARIAN)
                        .name("my first recipe")
                        .ingredients(List.of(Ingredient.builder().name("my first ingredient").build()))
                        .instruction("content of my first recipe")
                        .createdDate(LocalDateTime.now())
                        .build();
                given(recipeRepository.save(any(Recipe.class))).willReturn(Mono.just(recipe));
                given(recipeService.addRecipe(recipe)).willReturn(Mono.just("1"));
                client.post()
                        .uri("/v1/recipes")
                        .body(BodyInserters.fromValue(formData))
                        .exchange().expectHeader()
                        .value("Location", containsString("/v1/recipes/1"))
                        .expectStatus().isCreated()
                        .expectBody().isEmpty();
                verify(recipeService, times(1)).addRecipe(recipe);
                verifyNoMoreInteractions(recipeService);
            }
        }

        @Nested
        @DisplayName("/v1/recipes/:id PUT")
        class UpdatingRecipe {

            @Test
            @DisplayName("should return 204 when updating recipe")
            void shouldBeOkWhenUpdatingRecipe() {
                RecipeRequest recipeRequest = RecipeRequest.builder()
                        .serves(2)
                        .ingredients(List.of(Ingredient.builder().name("my first ingredient").build()))
                        .name("my first recipe")
                        .foodType("VEGETARIAN")
                        .instruction("content of my first recipe")
                        .build();
                Recipe recipe = Recipe.builder()
                        .id("1")
                        .name("updated name")
                        .instruction("updated content")
                        .serves(2)
                        .type(Recipe.FoodType.VEGETARIAN)
                        .createdDate(LocalDateTime.now())
                        .build();
                Recipe updateRecipe = Recipe.builder()
                        .id("1")
                        .name("updated name")
                        .instruction("updated content")
                        .serves(3)
                        .type(Recipe.FoodType.VEGETARIAN)
                        .createdDate(LocalDateTime.now())
                        .build();
                recipeRequest.setName("updated title");
                recipeRequest.setInstruction("updated content");
                given(recipeRepository.findById("1")).willReturn(Mono.just(recipe));
                given(recipeRepository.save(updateRecipe)).willReturn(Mono.just(updateRecipe));
                given(recipeService.updateRecipe("1",recipeRequest)).willReturn(Mono.just(updateRecipe));
                client.put()
                        .uri("/v1/recipes/1")
                        .body(BodyInserters.fromValue(recipeRequest))
                        .exchange()
                        .expectStatus()
                        .isNoContent()
                        .expectBody()
                        .isEmpty();
                verify(recipeService, times(1)).updateRecipe("1",recipeRequest);
                verifyNoMoreInteractions(recipeService);
            }
        }

        @Nested
        @DisplayName("/vi/recipes/:id DELETE")
        class DeletingRecipe {
            @Test
            @DisplayName("should return 204 when deleting recipe")
            void shouldReturn204WhenDeletingRecipe() {
                Recipe recipe = Recipe.builder()
                        .id("1")
                        .name("my first recipe")
                        .instruction("content of my first recipe")
                        .createdDate(LocalDateTime.now())
                        .serves(1)
                        .type(Recipe.FoodType.VEGETARIAN)
                        .ingredients(List.of(Ingredient.builder().name("apple").build()))
                        .build();
                Mono<Void> mono = Mono.empty();
                given(recipeRepository.findById("1")).willReturn(Mono.just(recipe));
                given(recipeRepository.delete(recipe)).willReturn(mono);
                given(recipeService.removeRecipe("1")).willReturn(mono);
                client.delete().uri("/v1/recipes/1").exchange().expectStatus().isNoContent();
                verify(recipeService, times(1)).removeRecipe("1");
                verifyNoMoreInteractions(recipeService);
            }

        }
    }

}

