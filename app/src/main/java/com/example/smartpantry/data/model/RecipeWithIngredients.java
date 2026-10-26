package com.example.smartpantry.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

/** Result of a "recipe + its ingredient rows" query. Not a table. */
public class RecipeWithIngredients {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<RecipeIngredient> ingredients;

    /** Returns the recipe with its ingredient list filled in. */
    public Recipe toRecipe() {
        recipe.setIngredients(ingredients == null
                ? new ArrayList<RecipeIngredient>()
                : ingredients);
        return recipe;
    }
}
