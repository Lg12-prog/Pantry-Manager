package com.example.smartpantry.ui.recipes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.repository.PantryRepository;
import com.example.smartpantry.data.repository.RecipeRepository;
import com.example.smartpantry.domain.RecipeMatcher;

import java.util.List;

public class SuggestedRecipesViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<Recipe>> suggestedRecipes = new MediatorLiveData<>();
    private List<PantryItem> pantryItems;
    private List<Recipe> allRecipes;

    public SuggestedRecipesViewModel(@NonNull Application application) {
        super(application);
        PantryRepository pantryRepository = PantryRepository.getInstance(application);
        RecipeRepository recipeRepository = RecipeRepository.getInstance(application);

        // Re-run the matcher whenever the pantry OR the recipe list changes.
        suggestedRecipes.addSource(pantryRepository.getItems(), items -> {
            pantryItems = items;
            recompute();
        });
        suggestedRecipes.addSource(recipeRepository.getAllRecipes(), recipes -> {
            allRecipes = recipes;
            recompute();
        });
    }

    /** Emits only after both the pantry and the recipes have loaded (no false "empty" flash). */
    private void recompute() {
        if (pantryItems == null || allRecipes == null) {
            return;
        }
        suggestedRecipes.setValue(RecipeMatcher.findMakeable(pantryItems, allRecipes));
    }

    public LiveData<List<Recipe>> getSuggestedRecipes() {
        return suggestedRecipes;
    }
}
