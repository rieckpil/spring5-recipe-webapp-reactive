package de.rieckpil.recipewebapp.repositories;

import de.rieckpil.recipewebapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByDescription(String description);
}
