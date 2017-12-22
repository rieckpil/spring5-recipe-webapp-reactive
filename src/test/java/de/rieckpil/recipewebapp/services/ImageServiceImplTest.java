package de.rieckpil.recipewebapp.services;

import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    RecipeReactiveRepository mockedRecipeRepository;

    ImageService cut;

    @Before
    public void setUp() {
        cut = new ImageServiceImpl(mockedRecipeRepository);
    }

    @Test
    public void testSaveImageFile() throws IOException {

        String id = "1";
        MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring Framework Guru".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);

        when(mockedRecipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(mockedRecipeRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        cut.saveImageFile(id, multipartFile).block();

        verify(mockedRecipeRepository, times(1)).save(argumentCaptor.capture());
        verify(mockedRecipeRepository, times(1)).findById(anyString());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}