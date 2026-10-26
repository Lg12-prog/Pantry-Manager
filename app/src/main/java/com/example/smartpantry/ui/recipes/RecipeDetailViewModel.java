package com.example.smartpantry.ui.recipes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.repository.RecipeRepository;

public class RecipeDetailViewModel extends AndroidViewModel {

    private final RecipeRepository repository;

    public RecipeDetailViewModel(@NonNull Application application) {
        super(application);
        repository = RecipeRepository.getInstance(application);
    }

    public LiveData<Recipe> getRecipe(long id) {
        return repository.getRecipe(id);
    }
}
