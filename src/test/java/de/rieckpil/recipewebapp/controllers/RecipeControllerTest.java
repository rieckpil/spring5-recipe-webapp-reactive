package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.services.RecipeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class RecipeControllerTest {

    @Mock
    private RecipeService mockedRecipeService;

    private RecipeController cut;

    @Before
    public void setUp() {
        cut = new RecipeController(mockedRecipeService);
    }

    @Test
    public void testShowById() throws Exception {

        Recipe serviceReturn = new Recipe();
        serviceReturn.setId("1");

        when(mockedRecipeService.getRecipeById("1")).thenReturn(Mono.just(serviceReturn));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(cut).build();

        mockMvc.perform(get("/recipe/1/show")).andExpect(status().isOk()).andExpect(view().name("recipe/show")).andExpect(model().attributeExists("recipe"));

    }

}