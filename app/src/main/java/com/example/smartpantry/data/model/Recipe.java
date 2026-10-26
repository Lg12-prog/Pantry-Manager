package com.example.smartpantry.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe. Stored in the "recipes" table; its ingredients live in "recipe_ingredients"
 * and are attached after loading (see RecipeWithIngredients), so the list is not a column.
 */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String name;
    /** Preparation steps, one per line (separated by "\n"). */
    private final String steps;

    @Ignore
    private List<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();

    /** Constructor used by Room. */
    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    /** Convenience constructor for code (ignored by Room). */
    @Ignore
    public Recipe(long id, String name, String steps, List<RecipeIngredient> ingredients) {
        this(id, name, steps);
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSteps() {
        return steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
