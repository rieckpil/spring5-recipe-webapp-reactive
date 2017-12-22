package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import de.rieckpil.recipewebapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import de.rieckpil.recipewebapp.repositories.UnitOfMeasureRepository;
import de.rieckpil.recipewebapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> getAllUnitOfMeasures() {

        return unitOfMeasureReactiveRepository
                .findAll()
                .map(unitOfMeasureToUnitOfMeasureCommand::convert);

    }
}
