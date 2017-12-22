package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.converters.RecipeCommandToRecipe;
import de.rieckpil.recipewebapp.converters.RecipeToRecipeCommand;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Service called");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> getRecipeById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> getCommandById(String id) {

        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
                    recipeCommand.getIngredients().forEach(rc -> {
                        rc.setRecipeId(recipeCommand.getId());
                    });

                    return recipeCommand;
                });
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {

        return recipeReactiveRepository
                .save(recipeCommandToRecipe.convert(recipeCommand))
                .map(recipeToRecipeCommand::convert);

//        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
//        recipeReactiveRepository.save(new Recipe()).block();
//
//        Recipe savedRecipe = recipeReactiveRepository.save(detachedRecipe).block();
//        log.info("Saved Recipe with Id: " + savedRecipe.getId());
//        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(String id) {
        recipeReactiveRepository.deleteById(id).block();
    }
}
