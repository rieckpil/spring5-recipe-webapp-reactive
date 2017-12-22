package de.rieckpil.recipewebapp.repositories.reactive;

import de.rieckpil.recipewebapp.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
