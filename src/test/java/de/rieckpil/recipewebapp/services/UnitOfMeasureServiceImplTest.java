package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import de.rieckpil.recipewebapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import de.rieckpil.recipewebapp.domain.UnitOfMeasure;
import de.rieckpil.recipewebapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitOfMeasureServiceImplTest {

    UnitOfMeasureService service;

    @Spy
    UnitOfMeasureToUnitOfMeasureCommand spyedConverter;

    @Mock
    UnitOfMeasureReactiveRepository mockedReactiveRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(mockedReactiveRepository, spyedConverter);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(spyedConverter, mockedReactiveRepository);
    }

    @Test
    public void testGetAllUnitOfMeasures() {

        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId("1");
        unitOfMeasures.add(uom1);

        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId("1");
        unitOfMeasures.add(uom2);

        when(mockedReactiveRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

        List<UnitOfMeasureCommand> result = service.getAllUnitOfMeasures().collectList().block();

        assertEquals(2, result.size());
        verify(mockedReactiveRepository, times(1)).findAll();
        verify(spyedConverter, times(2)).convert(any(UnitOfMeasure.class));

    }

}