package com.example.smartpantry.domain;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Decides which recipes can be made from the pantry. This class must not use any
 * Android classes, so it can be unit-tested with plain JUnit.
 *
 * ================================================================================
 * TEMPORARY PLACEHOLDER - NOT THE STRICT-MATCHING RULE (Section 2.3).
 * It only checks that every ingredient NAME appears in the pantry. It does NOT
 * check quantities, units or expiry dates. It exists so the "Suggested Recipes"
 * screen can be tested end to end. Replace the body of findMakeable() in the
 * strict-matching step.
 * ================================================================================
 */
public final class RecipeMatcher {

    private RecipeMatcher() {
    }

    public static List<Recipe> findMakeable(List<PantryItem> pantry, List<Recipe> recipes) {
        Set<String> pantryNames = new HashSet<String>();
        for (PantryItem item : pantry) {
            pantryNames.add(normalize(item.getName()));
        }

        List<Recipe> makeable = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            List<RecipeIngredient> needed = recipe.getIngredients();
            boolean hasEverything = !needed.isEmpty();
            for (RecipeIngredient ingredient : needed) {
                if (!pantryNames.contains(normalize(ingredient.getName()))) {
                    hasEverything = false;
                    break;
                }
            }
            if (hasEverything) {
                makeable.add(recipe);
            }
        }
        return makeable;
    }

    private static String normalize(String name) {
        return name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
    }
}
