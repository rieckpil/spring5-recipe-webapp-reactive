package de.rieckpil.recipewebapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Notes {

    @Id
    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
