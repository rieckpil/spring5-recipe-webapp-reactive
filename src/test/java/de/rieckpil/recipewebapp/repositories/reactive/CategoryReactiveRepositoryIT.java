package de.rieckpil.recipewebapp.repositories.reactive;

import de.rieckpil.recipewebapp.domain.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryReactiveRepositoryIT {

    public static final String YUMMY_CATEGORY = "Yummy category!";
    @Autowired
    CategoryReactiveRepository cut;

    @Before
    public void setUp() {
        cut.deleteAll().block();
    }

    @Test
    public void testSave() {

        Category categoryToSave = new Category();
        categoryToSave.setDescription("Yummy category!");


        cut.save(categoryToSave).block();

        Long count = cut.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() {

        Category categoryToSave = new Category();
        categoryToSave.setDescription(YUMMY_CATEGORY);

        cut.save(categoryToSave).then().block();

        Category fetchedCat = cut.findByDescription(YUMMY_CATEGORY).block();

        assertNotNull(fetchedCat);
    }

}
