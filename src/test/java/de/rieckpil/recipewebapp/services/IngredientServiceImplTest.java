package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.IngredientCommand;
import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import de.rieckpil.recipewebapp.converters.IngredientCommandToIngredient;
import de.rieckpil.recipewebapp.converters.IngredientToIngredientCommand;
import de.rieckpil.recipewebapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import de.rieckpil.recipewebapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import de.rieckpil.recipewebapp.domain.Ingredient;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.reactive.RecipeReactiveRepository;
import de.rieckpil.recipewebapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngredientServiceImplTest {

    @Mock
    RecipeReactiveRepository mockedRecipeRepository;

    @Mock
    UnitOfMeasureReactiveRepository mockedUnitOfMeasureRepository;

    private IngredientService cut;
    private IngredientToIngredientCommand ingredientToIngredientCommand;
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImplTest() {
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Before
    public void setUp() {
        cut = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient, mockedRecipeRepository, mockedUnitOfMeasureRepository);
    }

    @Test
    public void testFindByRecipeIdAndIngredientId() {

        Recipe  recipe = new Recipe();
        recipe.setId("1");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(mockedRecipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        IngredientCommand ingredientCommand = cut.findByRecipeIdAndIngredientId("1", "3").block();

        assertEquals("3", ingredientCommand.getId());
        assertEquals("1", ingredientCommand.getRecipeId());

        verify(mockedRecipeRepository, times(1)).findById(anyString());

    }

    @Test
    public void testSaveIngredientCommand() {

        IngredientCommand commandToSave = new IngredientCommand();
        commandToSave.setId("3");
        commandToSave.setRecipeId("2");
        commandToSave.setUnitOfMeasure(new UnitOfMeasureCommand());
        commandToSave.getUnitOfMeasure().setId("3");

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");

        when(mockedRecipeRepository.findById(anyString())).thenReturn(Mono.just(new Recipe()));
        when(mockedRecipeRepository.save(any())).thenReturn(Mono.just(savedRecipe));

        IngredientCommand result = cut.saveIngredientCommand(commandToSave).block();

        assertEquals("3", result.getId());
        verify(mockedRecipeRepository, times(1)).findById(anyString());
        verify(mockedRecipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    public void testDeleteById() {

        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(mockedRecipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(mockedRecipeRepository.save(any())).thenReturn(Mono.just(recipe));

        cut.deleteById("1", "3");

        verify(mockedRecipeRepository, times(1)).findById(anyString());
        verify(mockedRecipeRepository, times(1)).save(any(Recipe.class));

    }
}