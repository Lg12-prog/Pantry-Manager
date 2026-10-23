package com.example.smartpantry.ui.recipes;

import androidx.lifecycle.ViewModel;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.repository.RecipeRepository;

public class RecipeDetailViewModel extends ViewModel {

    public Recipe getRecipe(long id) {
        return RecipeRepository.getInstance().getById(id);
    }
}
