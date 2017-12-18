package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.converters.RecipeCommandToRecipe;
import de.rieckpil.recipewebapp.converters.RecipeToRecipeCommand;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceImplTest {

    private RecipeServiceImpl cut;

    @Mock
    private RecipeRepository mockedRepository;

    @Mock
    private RecipeCommandToRecipe mockedRecipeCommandToRecipe;

    @Mock
    private RecipeToRecipeCommand mockedRecipeToRecipeCommand;

    @Before
    public void setUp() {
        cut = new RecipeServiceImpl(mockedRepository, mockedRecipeToRecipeCommand, mockedRecipeCommandToRecipe);
    }

    @Test
    public void getRecipes() {

        Recipe recipe = new Recipe();
        Set<Recipe> recipeSet = new HashSet<>();
        recipeSet.add(recipe);

        when(mockedRepository.findAll()).thenReturn(recipeSet);

        Set<Recipe> result = cut.getRecipies();

        assertEquals(1, result.size());
        verify(mockedRepository).findAll();
    }
}