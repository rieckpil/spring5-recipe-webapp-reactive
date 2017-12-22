package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.exceptions.NotFoundException;
import de.rieckpil.recipewebapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private WebDataBinder webDataBinder;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @RequestMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("recipe", recipeService.getRecipeById(id));

        return "recipe/show";
    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.getCommandById(id).block());

        return "recipe/recipeform";
    }


    @PostMapping("/recipe")
    public String saveOrUpdate(@ModelAttribute("recipe") RecipeCommand recipeCommand) {

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "recipe/recipeform";

        }

        RecipeCommand saveCommand = recipeService.saveRecipeCommand(recipeCommand).block();
        return "redirect:/recipe/" + saveCommand.getId() + "/show/";
    }

    @RequestMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){

        log.debug("deleting recipe by id: " + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class, TemplateInputException.class})
    public String handleNotFoundException(Exception exception, Model model) {

        log.error("Handling not found exception");
        log.error(exception.getMessage());

        model.addAttribute("exception", exception);

        return "404error";
    }

}

