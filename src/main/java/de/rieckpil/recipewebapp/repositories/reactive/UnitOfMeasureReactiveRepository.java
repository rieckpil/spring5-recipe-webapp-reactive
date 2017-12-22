package de.rieckpil.recipewebapp.repositories.reactive;

import de.rieckpil.recipewebapp.domain.Category;
import de.rieckpil.recipewebapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {

    Mono<UnitOfMeasure> findByDescription(String description);

}
