package com.ramsay.gordon;


import com.ramsay.gordon.domain.Ingredient;
import com.ramsay.gordon.domain.Recipe;
import com.ramsay.gordon.jwt.JwtTokenProvider;
import com.ramsay.gordon.repository.RecipeRepository;
import com.ramsay.gordon.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.Collection;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@DisplayName("API endpoints integration tests")
class IntegrationTests extends BaseIt {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public void before() {
        userRepository.saveAll(List.of(
                com.ramsay.gordon.domain.User.builder().username("user").roles(List.of("USER")).password("password").email("user.test@gmail.com")
                .build(),
                com.ramsay.gordon.domain.User.builder().username("admin").roles(List.of("admin")).password("password").email("admin.test@gmail.com").build()));
        List<Recipe> recipes = List.of(
                Recipe.builder().name("apple").type(Recipe.FoodType.VEGETARIAN).instruction("Test").serves(1)
                        .ingredients(List.of(Ingredient.builder().name("Apple").amount("1").build())).build(),
                Recipe.builder().name("apple3").type(Recipe.FoodType.VEGETARIAN).instruction("Test2").serves(2)
                        .ingredients(List.of(Ingredient.builder().name("Apple").amount("2").build())).build(),
                Recipe.builder().name("apple3").type(Recipe.FoodType.VEGETARIAN).instruction("Test3").serves(3)
                        .ingredients(List.of(Ingredient.builder().name("Apple").amount("3").build())).build(),
                Recipe.builder().name("apple4").type(Recipe.FoodType.VEGETARIAN).instruction("Test4").serves(4)
                        .ingredients(List.of(Ingredient.builder().name("Potato").amount("4").build())).build());
        recipeRepository.saveAll(recipes);
    }


    @BeforeEach
    void setup() {
        this.client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port).build();
        before();
    }

    @Nested
    @DisplayName("if user is not logged in")
    class NotLoggedIn {

        @Test
        @DisplayName("should be ok when getting all recipes with special filter")
        void shouldBeOkWhenGettingAllRecipeWithSpecialFilter() {
            String jwt = generateToken("user", "ROLE_USER");
            client.get().uri(uriBuilder ->
                    uriBuilder
                            .path("/v1/recipes/specials")
                            .queryParam("includeIngredient", List.of("potato"))
                            .queryParam("excludeIngredient", List.of("coffee"))
                            .queryParam("serves", 4)
                            .queryParam("instruction", "oven")
                            .build()).headers(headers -> headers.setBearerAuth(jwt)).exchange().expectStatus().isOk();
        }

        @Test
        @DisplayName("should return 404 when getting a none existing recipe")
        void shouldReturn404WhenGettingNoneExistingRecipe() {

            String jwt = generateToken("user", "ROLE_USER");
            client.get().uri("v1/recipes/100").headers(headers -> headers.setBearerAuth(jwt)).exchange().expectStatus()
                    .isNotFound();
        }

        @Test
        @DisplayName("should return 401 when trying to create a new recipe")
        void shouldBe401WhenCreatingRecipe() {

            client.post().uri("v1/recipes/")
                    .body(BodyInserters.fromValue(Recipe.builder()
                            .name("Muchi")
                            .instruction("content of Muchi test")
                            .build()))
                    .exchange().expectStatus()
                    .isEqualTo(HttpStatus.UNAUTHORIZED);

        }

        @Test
        @DisplayName("should return 401 when trying to update a recipe")
        void shouldReturn401WhenUpdatingRecipe() {
            before();
            client.put().uri("v1/recipes/1")
                    .body(BodyInserters.fromValue(Recipe.builder()
                            .name("updated title")
                            .instruction("updated content")
                            .build()))
                    .exchange().expectStatus()
                    .isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        @DisplayName("should return 401 when trying to delete a recipe")
        void shouldReturn401WhenDeletingRecipe() {
            client.delete().uri("/v1/recipes/1").exchange().expectStatus()
                    .isEqualTo(HttpStatus.UNAUTHORIZED);

        }
    }

    @Nested
    @DisplayName("if user is logged in as (USER)")
    class LoggedInAsUser {

        @BeforeEach
        void setup() {
            client = client.mutate().filter(userJwtAuthentication()).build();
        }

        @Test
        @DisplayName("should return 403 when trying to delete a recipe")
        void shouldReturn403WhenUpdatingRecipe() {
            client.delete().uri("/v1/recipes/1").exchange().expectStatus()
                    .isEqualTo(HttpStatus.FORBIDDEN);
        }


    }

    @Nested
    @DisplayName("if user is logged in as (ADMIN)")
    class LoggedInAsAdmin {

        @BeforeEach
        void setup() {
            client = client.mutate().filter(adminJwtAuthentication()).build();
        }

        @Test
        @DisplayName("should return 404 when trying to delete a none exiting recipe")
        void shouldReturn404WhenDeletingNoneExistingRecipe() {
            client.delete().uri("/v1/recipes/20").exchange().expectStatus()
                    .isEqualTo(HttpStatus.NOT_FOUND);
        }
    }


    private ExchangeFilterFunction userJwtAuthentication() {
        String jwt = generateToken("user", "ROLE_USER");
        return (request, next) -> next.exchange(ClientRequest.from(request)
                .headers(headers -> headers.setBearerAuth(jwt)).build());
    }

    private ExchangeFilterFunction adminJwtAuthentication() {
        String jwt = generateToken("admin", "ROLE_ADMIN");
        return (request, next) -> next.exchange(ClientRequest.from(request)
                .headers(headers -> headers.setBearerAuth(jwt)).build());
    }

    private String generateToken(String username, String... roles) {
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(roles);
        var principal = new User(username, "password", authorities);
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                principal, null, authorities);

        var jwt = jwtTokenProvider
                .createToken(usernamePasswordAuthenticationToken);
        log.debug("generated jwt token::" + jwt);

        return jwt;
    }
}
