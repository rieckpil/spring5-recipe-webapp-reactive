package de.rieckpil.recipewebapp.repositories;

import de.rieckpil.recipewebapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository  extends CrudRepository<Recipe, String>{

}
