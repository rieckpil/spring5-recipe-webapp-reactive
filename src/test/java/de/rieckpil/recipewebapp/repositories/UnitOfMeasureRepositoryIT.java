package de.rieckpil.recipewebapp.repositories;

import de.rieckpil.recipewebapp.bootstrap.RecipeBootstrap;
import de.rieckpil.recipewebapp.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    UnitOfMeasureRepository cut;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Before
    public void setUp() {

        cut.deleteAll();
        categoryRepository.deleteAll();
        recipeRepository.deleteAll();

        RecipeBootstrap recipeBootstrap= new RecipeBootstrap(categoryRepository, recipeRepository, cut);

        recipeBootstrap.onApplicationEvent(null);
    }

    @Test
    public void testFindByDescription() throws Exception {

        Optional<UnitOfMeasure> uomOptional = cut.findByDescription("Teaspoon");

        assertEquals("Teaspoon", uomOptional.get().getDescription());

    }

    @Test
    public void testFindByDescriptionCup() throws Exception {

        Optional<UnitOfMeasure> uomOptional = cut.findByDescription("Cup");

        assertEquals("Cup", uomOptional.get().getDescription());

    }


}
