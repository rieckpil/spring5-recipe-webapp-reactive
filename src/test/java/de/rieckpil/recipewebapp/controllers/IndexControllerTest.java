package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.services.RecipeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class IndexControllerTest {

    private IndexController cut;

    @Mock
    private Model mockedModel;

    @Mock
    private RecipeService mockedService;

    @Captor
    private ArgumentCaptor<Set<Recipe>> recipeSetCaptor;

    @Before
    public void setUp() {
        cut = new IndexController(mockedService);
    }

    @Test
    public void testMockMvc() throws Exception{
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(cut).build();

        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));

    }

    @Test
    public void testGetIndexPage() {

        Recipe recipe1 = new Recipe();
        recipe1.setDescription("Dummy description");

        Recipe recipe2 = new Recipe();
        recipe2.setDescription("Dummy description 2 ");

        when(mockedService.getRecipes()).thenReturn(Flux.just(recipe1, recipe2));

        String viewName = cut.getIndexPage(mockedModel);

        verify(mockedService).getRecipes();
        verify(mockedModel).addAttribute(eq("recipes"),recipeSetCaptor.capture());

        assertEquals("index", viewName);
        assertEquals(2, recipeSetCaptor.getValue().size());
    }

}