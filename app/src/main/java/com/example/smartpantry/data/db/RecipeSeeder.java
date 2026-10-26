package com.example.smartpantry.data.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

/**
 * Inserts the pre-loaded recipes the first time the database is created.
 * Runs inside Room's create transaction, so the recipes exist before the first query
 * returns, and a failure here fails loudly instead of leaving a half-seeded database.
 *
 * Table and column names must match the entities exactly.
 */
final class RecipeSeeder {

    private RecipeSeeder() {
    }

    static void seed(SupportSQLiteDatabase db) {
        for (Recipe recipe : RecipeSeedData.getRecipes()) {
            ContentValues recipeValues = new ContentValues();
            recipeValues.put("name", recipe.getName());
            recipeValues.put("steps", recipe.getSteps());
            long recipeId = db.insert("recipes", SQLiteDatabase.CONFLICT_NONE, recipeValues);
            if (recipeId == -1) {
                throw new IllegalStateException("Could not seed recipe: " + recipe.getName());
            }

            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                ContentValues ingredientValues = new ContentValues();
                ingredientValues.put("recipeId", recipeId);
                ingredientValues.put("name", ingredient.getName());
                ingredientValues.put("quantity", ingredient.getQuantity());
                ingredientValues.put("unit", ingredient.getUnit());
                long rowId = db.insert("recipe_ingredients", SQLiteDatabase.CONFLICT_NONE, ingredientValues);
                if (rowId == -1) {
                    throw new IllegalStateException("Could not seed ingredient: "
                            + ingredient.getName() + " for " + recipe.getName());
                }
            }
        }
    }
}
