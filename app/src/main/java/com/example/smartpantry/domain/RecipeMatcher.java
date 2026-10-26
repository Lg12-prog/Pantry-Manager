package com.example.smartpantry.domain;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * THE STRICT-MATCHING RULE (brief, Section 2.3).
 *
 * A recipe is "makeable" only if EVERY ingredient it requires is in the pantry in at least
 * the required quantity. One missing ingredient, or too little of one, excludes the recipe.
 *
 * How an ingredient is checked:
 *  1. NAME   - compared with NameNormalizer (case, spacing, singular/plural, filler words).
 *  2. UNITS  - quantities are converted with UnitConverter so 1 kg covers 200 g, 0.5 l covers
 *              300 ml, 3 tsp covers 1 tbsp. Mass, volume and pieces cannot be converted into one
 *              another, so e.g. "500 g eggs" does not cover "3 pcs eggs".
 *  3. AMOUNT - all pantry rows with the same ingredient (in the same dimension) are added up,
 *              and the total must be >= the requirement.
 *  4. SHARING - a pantry quantity is used up once an ingredient takes it, so a recipe that lists
 *              the same ingredient twice cannot count the same stock twice.
 *
 * Not used on purpose: expiry dates (the brief only asks for "present in the pantry").
 * No Android classes, so it can be unit-tested with plain JUnit.
 */
public final class RecipeMatcher {

    /** Absorbs floating-point error such as 0.1 + 0.2 != 0.3. */
    private static final double EPSILON = 1e-9;

    private RecipeMatcher() {
    }

    /** Checks one recipe against the pantry. */
    public static MatchResult evaluate(Recipe recipe, List<PantryItem> pantry) {
        return evaluateAgainst(recipe, toEntries(pantry));
    }

    /** The main suggestions list: recipes the user can make right now. Never includes near-misses. */
    public static List<Recipe> findMakeable(List<PantryItem> pantry, List<Recipe> recipes) {
        List<PantryEntry> entries = toEntries(pantry);
        List<Recipe> makeable = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (evaluateAgainst(recipe, entries).isMakeable()) {
                makeable.add(recipe);
            }
        }
        return makeable;
    }

    /**
     * Optional "Almost There" group: recipes missing exactly ONE ingredient. Kept separate from
     * findMakeable() so the two lists can never mix.
     */
    public static List<MatchResult> findAlmostThere(List<PantryItem> pantry, List<Recipe> recipes) {
        List<PantryEntry> entries = toEntries(pantry);
        List<MatchResult> almost = new ArrayList<MatchResult>();
        for (Recipe recipe : recipes) {
            MatchResult result = evaluateAgainst(recipe, entries);
            if (result.isAlmostThere()) {
                almost.add(result);
            }
        }
        return almost;
    }

    // ------------------------------------------------------------------------------------

    private static MatchResult evaluateAgainst(Recipe recipe, List<PantryEntry> entries) {
        List<RecipeIngredient> needed = recipe.getIngredients();
        if (needed == null) {
            needed = new ArrayList<RecipeIngredient>();
        }

        List<RecipeIngredient> missing = new ArrayList<RecipeIngredient>();
        double[] remaining = new double[entries.size()];
        for (int i = 0; i < remaining.length; i++) {
            remaining[i] = entries.get(i).amount;
        }

        for (RecipeIngredient ingredient : needed) {
            List<Set<String>> nameForms = NameNormalizer.toWordForms(ingredient.getName());
            String dimension = UnitConverter.dimensionOf(ingredient.getUnit());
            double required = UnitConverter.toBase(ingredient.getQuantity(), ingredient.getUnit());

            boolean present = false;
            double available = 0;
            List<Integer> usable = new ArrayList<Integer>();
            for (int i = 0; i < entries.size(); i++) {
                PantryEntry entry = entries.get(i);
                if (!NameNormalizer.sameForms(nameForms, entry.nameForms)) {
                    continue;
                }
                present = true;
                if (entry.dimension.equals(dimension)) {
                    usable.add(i);
                    available += remaining[i];
                }
            }

            boolean covered = required > 0 ? available + EPSILON >= required : present;
            if (!covered) {
                missing.add(ingredient);
                continue;
            }

            // Use up the stock so the same pantry quantity is not counted twice.
            double toTake = required;
            for (int index : usable) {
                if (toTake <= 0) {
                    break;
                }
                double take = Math.min(remaining[index], toTake);
                remaining[index] -= take;
                toTake -= take;
            }
        }

        boolean makeable = !needed.isEmpty() && missing.isEmpty();
        return new MatchResult(recipe, missing, makeable);
    }

    /** Pantry rows prepared for comparison. Unusable rows (blank name, quantity <= 0) are skipped. */
    private static List<PantryEntry> toEntries(List<PantryItem> pantry) {
        List<PantryEntry> entries = new ArrayList<PantryEntry>();
        if (pantry == null) {
            return entries;
        }
        for (PantryItem item : pantry) {
            if (item == null || !(item.getQuantity() > 0)) {
                continue;
            }
            List<Set<String>> forms = NameNormalizer.toWordForms(item.getName());
            if (forms.isEmpty()) {
                continue;
            }
            entries.add(new PantryEntry(
                    forms,
                    UnitConverter.dimensionOf(item.getUnit()),
                    UnitConverter.toBase(item.getQuantity(), item.getUnit())));
        }
        return entries;
    }

    private static final class PantryEntry {
        final List<Set<String>> nameForms;
        final String dimension;
        final double amount;

        PantryEntry(List<Set<String>> nameForms, String dimension, double amount) {
            this.nameForms = nameForms;
            this.dimension = dimension;
            this.amount = amount;
        }
    }
}
