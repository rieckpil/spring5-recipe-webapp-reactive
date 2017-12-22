package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> getAllUnitOfMeasures();
}
