package com.example.smartpantry.data.repository;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TEMPORARY in-memory implementation with a few placeholder recipes so the screens
 * can be tested. In the database step this is replaced by Room plus the seeded
 * collection of 15-20 recipes.
 */
public class RecipeRepository {

    private static RecipeRepository instance;

    private final List<Recipe> recipes;

    private RecipeRepository() {
        List<Recipe> list = new ArrayList<Recipe>();

        list.add(recipe(1, "Tomato Pasta",
                "Boil the pasta in salted water until tender.\n"
                        + "Heat the olive oil and cook the chopped tomatoes until soft.\n"
                        + "Drain the pasta and toss it through the sauce.",
                ing("pasta", 200, "g"), ing("tomato", 4, "pcs"), ing("olive oil", 2, "tbsp")));

        list.add(recipe(2, "Scrambled Eggs on Toast",
                "Toast the bread.\n"
                        + "Melt the butter in a pan and add the beaten eggs.\n"
                        + "Stir gently until just set and serve on the toast.",
                ing("eggs", 3, "pcs"), ing("bread", 2, "pcs"), ing("butter", 1, "tbsp")));

        list.add(recipe(3, "Egg Fried Rice",
                "Fry the chopped onion until soft.\n"
                        + "Add the cooked rice and stir-fry for a few minutes.\n"
                        + "Push the rice aside, scramble the eggs, then mix everything with the soy sauce.",
                ing("rice", 2, "cup"), ing("eggs", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("soy sauce", 2, "tbsp")));

        list.add(recipe(4, "Simple Pancakes",
                "Whisk the flour, milk and eggs into a smooth batter.\n"
                        + "Melt a little butter in a pan.\n"
                        + "Cook spoonfuls of batter until golden on both sides.",
                ing("flour", 200, "g"), ing("milk", 300, "ml"), ing("eggs", 2, "pcs"),
                ing("butter", 1, "tbsp")));

        recipes = Collections.unmodifiableList(list);
    }

    public static synchronized RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public List<Recipe> getAllRecipes() {
        return recipes;
    }

    public Recipe getById(long id) {
        for (Recipe recipe : recipes) {
            if (recipe.getId() == id) {
                return recipe;
            }
        }
        return null;
    }

    private static Recipe recipe(long id, String name, String steps, RecipeIngredient... ingredients) {
        List<RecipeIngredient> list = new ArrayList<RecipeIngredient>();
        for (RecipeIngredient ingredient : ingredients) {
            ingredient.setRecipeId(id);
            list.add(ingredient);
        }
        return new Recipe(id, name, steps, list);
    }

    private static RecipeIngredient ing(String name, double quantity, String unit) {
        return new RecipeIngredient(name, quantity, unit);
    }
}
