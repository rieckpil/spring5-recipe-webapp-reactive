package de.rieckpil.recipewebapp.repositories.reactive;

import de.rieckpil.recipewebapp.domain.UnitOfMeasure;
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
public class UnitOfMeasureReactiveRepositoryIT {

    public static final String DESCRIPTION = "Yumm UOM!";
    @Autowired
    UnitOfMeasureReactiveRepository cut;

    @Before
    public void setUp() {
        cut.deleteAll().block();
    }

    @Test
    public void testSave() {

        UnitOfMeasure unitOfMeasureToSave = new UnitOfMeasure();
        unitOfMeasureToSave.setDescription(DESCRIPTION);

        cut.save(unitOfMeasureToSave).block();

        Long count = cut.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
    public void testFindByDescription() {

        UnitOfMeasure unitOfMeasureToSave = new UnitOfMeasure();
        unitOfMeasureToSave.setDescription(DESCRIPTION);

        cut.save(unitOfMeasureToSave).block();

        UnitOfMeasure result = cut.findByDescription(DESCRIPTION).block();

        assertNotNull(result);
    }

}
