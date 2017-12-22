package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.converters.RecipeCommandToRecipe;
import de.rieckpil.recipewebapp.converters.RecipeToRecipeCommand;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    private RecipeService cut;

    @Mock
    private RecipeReactiveRepository mockedRepository;

    @Mock
    private RecipeCommandToRecipe mockedRecipeCommandToRecipe;

    @Mock
    private RecipeToRecipeCommand mockedRecipeToRecipeCommand;

    @Before
    public void setUp() {
        cut = new RecipeServiceImpl(mockedRepository, mockedRecipeToRecipeCommand, mockedRecipeCommandToRecipe);
    }

    @Test
    public void testGetRecipes() {

        Recipe recipe = new Recipe();

        when(mockedRepository.findAll()).thenReturn(Flux.just(recipe));

        List<Recipe> result = cut.getRecipes().collectList().block();

        assertEquals(1, result.size());
        verify(mockedRepository, times(1)).findAll();
        verify(mockedRepository, never()).findById(anyString());


    }
}