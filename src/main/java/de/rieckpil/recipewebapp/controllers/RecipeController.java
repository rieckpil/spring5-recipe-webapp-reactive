package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.commands.RecipeCommand;
import de.rieckpil.recipewebapp.exceptions.NotFoundException;
import de.rieckpil.recipewebapp.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.FileNotFoundException;

@Slf4j
@Controller
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){

        model.addAttribute("recipe", recipeService.getRecipeById(new Long(id)));

        return "recipe/show";
    }

    @RequestMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());

        return "recipe/recipeform";
    }

    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.getCommandById(Long.valueOf(id)));

        return "recipe/recipeform";
    }


    @PostMapping
    @RequestMapping("/recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult){

        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "recipe/recipeform";

        }

        RecipeCommand saveCommand = recipeService.saveRecipeCommand(recipeCommand);

        return "redirect:/recipe/" + saveCommand.getId() + "/show/";
    }

    @RequestMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable String id){

        log.debug("deleting recipe by id: " + id);
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception exception){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("400error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public ModelAndView handleFileNotFoundException(Exception exception){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }
}

