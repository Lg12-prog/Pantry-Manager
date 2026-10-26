package com.example.smartpantry.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.smartpantry.data.db.AppDatabase;
import com.example.smartpantry.data.db.RecipeDao;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.List;

/** Read access to the seeded recipes, each with its ingredient list attached. */
public class RecipeRepository {

    private static volatile RecipeRepository instance;

    private final RecipeDao dao;
    private final LiveData<List<Recipe>> allRecipes;

    private RecipeRepository(Context context) {
        dao = AppDatabase.getInstance(context).recipeDao();
        allRecipes = Transformations.map(dao.observeAll(), rows -> {
            List<Recipe> recipes = new ArrayList<Recipe>();
            if (rows != null) {
                for (RecipeWithIngredients row : rows) {
                    recipes.add(row.toRecipe());
                }
            }
            return recipes;
        });
    }

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (RecipeRepository.class) {
                if (instance == null) {
                    instance = new RecipeRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    /** Emits null if no recipe has this id. */
    public LiveData<Recipe> getRecipe(long id) {
        return Transformations.map(dao.observeById(id), row -> row == null ? null : row.toRecipe());
    }
}
