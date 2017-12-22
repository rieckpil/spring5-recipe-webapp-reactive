package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.commands.IngredientCommand;
import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import de.rieckpil.recipewebapp.services.IngredientService;
import de.rieckpil.recipewebapp.services.RecipeService;
import de.rieckpil.recipewebapp.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    private WebDataBinder webDataBinder;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {

        log.debug("Getting ingredient list for id: " + recipeId);

        model.addAttribute("recipe", recipeService.getCommandById(recipeId));

        return "recipe/ingredient/list";

    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id));

        return "recipe/ingredient/show";

    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String createNewIngredient(@PathVariable String recipeId, Model model){

        RecipeCommand recipeCommand = recipeService.getCommandById(recipeId).block();

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);

        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.getAllUnitOfMeasures());

        return "recipe/ingredient/ingredientform";

    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand ingredientCommand) {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "recipe/ingredient/ingredientform";
        }

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand).block();

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() +  "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id){

        log.debug("deleteing ingredient with id: " + id + " from recipe with id: " + recipeId);
        ingredientService.deleteById(recipeId, id).block();

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

    @ModelAttribute("uomList")
    public Flux<UnitOfMeasureCommand> populateUnitOfMeasureList() {
        return unitOfMeasureService.getAllUnitOfMeasures();
    }
}
