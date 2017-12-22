package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.IngredientCommand;
import de.rieckpil.recipewebapp.converters.IngredientCommandToIngredient;
import de.rieckpil.recipewebapp.converters.IngredientToIngredientCommand;
import de.rieckpil.recipewebapp.domain.Ingredient;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.RecipeRepository;
import de.rieckpil.recipewebapp.repositories.UnitOfMeasureRepository;
import de.rieckpil.recipewebapp.repositories.reactive.RecipeReactiveRepository;
import de.rieckpil.recipewebapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeReactiveRepository recipeReactiveRepository, RecipeRepository recipeRepository, UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository
                .findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(ingredient);
                    ingredientCommand.setRecipeId(recipeId);
                    return ingredientCommand;
                });

//        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
//
//        if (!recipeOptional.isPresent()) {
//            log.error("no recipe found for id: " + recipeId);
//        }
//
//        Recipe recipe = recipeOptional.get();
//
//        Optional<IngredientCommand> optionalIngredientCommand = recipe.getIngredients().stream()
//                .filter(ingredient -> ingredient.getId().equals(ingredientId))
//                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();
//
//        if (!optionalIngredientCommand.isPresent()) {
//            log.error("no ingredient with id: " + ingredientId + " found in recipe with id: " + recipeId);
//        }
//
//        return Mono.just(optionalIngredientCommand.get());
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand ingredientCommand) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (!recipeOptional.isPresent()) {
            log.error("no recipe found for id: " + ingredientCommand.getRecipeId());
            return Mono.just(new IngredientCommand());
        } else {

            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                    .findById(ingredientCommand.getUnitOfMeasure().getId()).block());

//                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
//                        .orElseThrow(() -> new RuntimeException("UOM not found!")));
                if(ingredientFound.getUnitOfMeasure() == null){
                    new RuntimeException("Unit of Measure not found!");
                }

            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();


            if(!savedIngredientOptional.isPresent()){
                savedIngredientOptional = savedRecipe
                        .getIngredients()
                        .stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUnitOfMeasure().getId().equals(ingredientCommand.getUnitOfMeasure().getId()))
                        .findFirst();
            }

            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return Mono.just(ingredientCommandSaved);
        }
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {

        Recipe recipe = recipeRepository.findById(recipeId).get();

        if(recipe != null){

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(ingredientOptional.isPresent()){

                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        }
        return Mono.empty();
    }
}
