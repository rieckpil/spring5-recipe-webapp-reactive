package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> getAllUnitOfMeasures();
}
