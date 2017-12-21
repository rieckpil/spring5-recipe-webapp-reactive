package de.rieckpil.recipewebapp.commands;

import de.rieckpil.recipewebapp.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class RecipeCommand {

    private String id;

    @NotBlank
    @Size(min=3, max=255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(100)
    private Integer servings;
    private String source;

    @URL
    private String url;

    @NotBlank
    private String directions;
    private List<IngredientCommand> ingredients = new ArrayList<>();
    private Difficulty difficulty;
    private NotesCommand notes;
    private List<CategoryCommand> categories = new ArrayList<>();
    private Byte[] image;
}
