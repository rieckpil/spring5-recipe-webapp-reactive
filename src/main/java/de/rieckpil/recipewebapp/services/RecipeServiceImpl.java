package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.converters.RecipeCommandToRecipe;
import de.rieckpil.recipewebapp.converters.RecipeToRecipeCommand;
import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.exceptions.NotFoundException;
import de.rieckpil.recipewebapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
    private final RecipeCommandToRecipe recipeCommandToRecipe;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeToRecipeCommand recipeToRecipeCommand, RecipeCommandToRecipe recipeCommandToRecipe) {
        this.recipeRepository = recipeRepository;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
    }

    @Override
    public Set<Recipe> getRecipies() {
        log.debug("Service called");
        Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe getRecipeById(Long id) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if(!recipeOptional.isPresent()){
            throw new NotFoundException("Recipe with id: " + id + " not found!");
        }

        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {

        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe with Id: " + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand getCommandById(Long id) {
        return recipeToRecipeCommand.convert(getRecipeById(id));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
