package com.example.smartpantry.domain;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The outcome of checking one recipe against the pantry. */
public final class MatchResult {

    private final Recipe recipe;
    private final List<RecipeIngredient> missingIngredients;
    private final boolean makeable;

    MatchResult(Recipe recipe, List<RecipeIngredient> missingIngredients, boolean makeable) {
        this.recipe = recipe;
        this.missingIngredients = Collections.unmodifiableList(new ArrayList<RecipeIngredient>(missingIngredients));
        this.makeable = makeable;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    /** Recipe ingredients the pantry does not cover (absent, or not enough of it). */
    public List<RecipeIngredient> getMissingIngredients() {
        return missingIngredients;
    }

    /** True only if EVERY ingredient is covered in at least the required quantity. */
    public boolean isMakeable() {
        return makeable;
    }

    /** Missing exactly one ingredient - the optional "Almost There" group. Never true if makeable. */
    public boolean isAlmostThere() {
        return !makeable && missingIngredients.size() == 1;
    }
}
