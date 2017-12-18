package de.rieckpil.recipewebapp.controllers;

import de.rieckpil.recipewebapp.domain.Recipe;
import de.rieckpil.recipewebapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
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

        Set<Recipe> recipeSet = new HashSet<>();
        recipeSet.add(new Recipe());

        Recipe recipe = new Recipe();
        recipe.setDescription("Dummy description");
        recipeSet.add(recipe);

        when(mockedService.getRecipies()).thenReturn(recipeSet);

        String viewName = cut.getIndexPage(mockedModel);

        verify(mockedService).getRecipies();
        verify(mockedModel).addAttribute(eq("recipes"),recipeSetCaptor.capture());

        assertEquals("index", viewName);
        assertEquals(2, recipeSetCaptor.getValue().size());
    }

}