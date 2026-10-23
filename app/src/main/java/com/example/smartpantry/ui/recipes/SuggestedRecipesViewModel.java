package com.example.smartpantry.ui.recipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.repository.PantryRepository;
import com.example.smartpantry.data.repository.RecipeRepository;
import com.example.smartpantry.domain.RecipeMatcher;

import java.util.List;

public class SuggestedRecipesViewModel extends ViewModel {

    private final LiveData<List<Recipe>> suggestedRecipes;

    public SuggestedRecipesViewModel() {
        final RecipeRepository recipeRepository = RecipeRepository.getInstance();
        // Re-runs the matcher every time the pantry changes.
        suggestedRecipes = Transformations.map(
                PantryRepository.getInstance().getItems(),
                pantryItems -> RecipeMatcher.findMakeable(pantryItems, recipeRepository.getAllRecipes()));
    }

    public LiveData<List<Recipe>> getSuggestedRecipes() {
        return suggestedRecipes;
    }
}
