package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipies();

    Recipe getRecipeById(Long id);

    RecipeCommand getCommandById(Long id);

    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);

    void deleteById(Long id);
}
