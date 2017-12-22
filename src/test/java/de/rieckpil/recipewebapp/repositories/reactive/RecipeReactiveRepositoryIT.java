package de.rieckpil.recipewebapp.repositories.reactive;

import de.rieckpil.recipewebapp.domain.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Before
    public void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }

    @Test
    public void testRecipeSave() throws Exception {

        Recipe recipeToSave = new Recipe();
        recipeToSave.setDescription("Yummy!");

        assertEquals(Long.valueOf(0L), recipeReactiveRepository.count().block());

        recipeReactiveRepository.save(recipeToSave).block();

        Long count = recipeReactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);

    }

}
