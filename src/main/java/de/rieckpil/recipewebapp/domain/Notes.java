package de.rieckpil.recipewebapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(exclude = {"recipe"})
@Data
public class Notes {

    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
