package de.rieckpil.recipewebapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;

    @ManyToOne
    private Recipe recipe;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure unitOfMeasure;

    public Ingredient() {
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure unitOfMeasure) {
        this.description = description;
        this.amount = amount;
        this.unitOfMeasure = unitOfMeasure;
    }

    public Ingredient(String description, BigDecimal amount, Recipe recipe, UnitOfMeasure unitOfMeasure) {
        this.description = description;
        this.amount = amount;
        this.recipe = recipe;
        this.unitOfMeasure = unitOfMeasure;
    }
}
