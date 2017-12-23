package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.config.WebConfig;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService mockedRecipeService;

    @Before
    public void setUp() {

        WebConfig webConfig = new WebConfig();

        RouterFunction<?> routerFunction = webConfig.routes(mockedRecipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }


    @Test
    public void testGetRecipesWithNoData() {

        when(mockedRecipeService.getRecipes()).thenReturn(Flux.just());

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetRecipesWithData() {

        when(mockedRecipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }
}
