package de.rieckpil.recipewebapp.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
