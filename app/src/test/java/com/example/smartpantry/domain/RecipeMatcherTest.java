package com.example.smartpantry.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Tests for the strict-matching rule (brief, Section 2.3). */
public class RecipeMatcherTest {

    // ---------- helpers ----------

    private static PantryItem item(String name, double quantity, String unit) {
        return new PantryItem(0, name, quantity, unit, null);
    }

    private static RecipeIngredient need(String name, double quantity, String unit) {
        return new RecipeIngredient(name, quantity, unit);
    }

    private static Recipe recipe(String name, RecipeIngredient... ingredients) {
        return new Recipe(0, name, "steps", new ArrayList<RecipeIngredient>(Arrays.asList(ingredients)));
    }

    private static List<PantryItem> pantry(PantryItem... items) {
        return new ArrayList<PantryItem>(Arrays.asList(items));
    }

    private static boolean makeable(Recipe recipe, List<PantryItem> pantry) {
        return RecipeMatcher.evaluate(recipe, pantry).isMakeable();
    }

    private static Recipe fiveIngredientRecipe() {
        return recipe("Five",
                need("pasta", 200, "g"), need("tomatoes", 4, "pcs"), need("olive oil", 2, "tbsp"),
                need("garlic", 2, "pcs"), need("salt", 1, "tsp"));
    }

    // ---------- the rule itself ----------

    @Test
    public void everyIngredientPresent_isSuggested() {
        List<PantryItem> p = pantry(item("pasta", 500, "g"), item("tomatoes", 6, "pcs"),
                item("olive oil", 5, "tbsp"), item("garlic", 3, "pcs"), item("salt", 3, "tsp"));
        assertTrue(makeable(fiveIngredientRecipe(), p));
    }

    @Test
    public void fiveNeededFourPresent_isNotSuggested() {
        List<PantryItem> p = pantry(item("pasta", 500, "g"), item("tomatoes", 6, "pcs"),
                item("olive oil", 5, "tbsp"), item("garlic", 3, "pcs"));
        MatchResult result = RecipeMatcher.evaluate(fiveIngredientRecipe(), p);
        assertFalse(result.isMakeable());
        assertEquals(1, result.getMissingIngredients().size());
        assertEquals("salt", result.getMissingIngredients().get(0).getName());
    }

    @Test
    public void partialMatchNeverAppearsInMainList() {
        List<PantryItem> p = pantry(item("pasta", 500, "g"), item("tomatoes", 6, "pcs"),
                item("olive oil", 5, "tbsp"), item("garlic", 3, "pcs"));
        List<Recipe> all = Arrays.asList(fiveIngredientRecipe());
        assertEquals(0, RecipeMatcher.findMakeable(p, all).size());
    }

    @Test
    public void emptyPantry_suggestsNothing() {
        List<Recipe> all = Arrays.asList(fiveIngredientRecipe());
        assertEquals(0, RecipeMatcher.findMakeable(new ArrayList<PantryItem>(), all).size());
        assertEquals(0, RecipeMatcher.findMakeable(null, all).size());
    }

    @Test
    public void recipeWithNoIngredients_isNeverSuggested() {
        Recipe empty = recipe("Empty");
        assertFalse(makeable(empty, pantry(item("egg", 1, "pcs"))));
        assertEquals(0, RecipeMatcher.findAlmostThere(pantry(item("egg", 1, "pcs")), Arrays.asList(empty)).size());
    }

    @Test
    public void onlyMakeableRecipesAreReturned() {
        Recipe toast = recipe("Toast", need("bread", 2, "pcs"), need("butter", 1, "tbsp"));
        Recipe omelette = recipe("Omelette", need("eggs", 3, "pcs"), need("cheese", 50, "g"));
        List<PantryItem> p = pantry(item("Bread", 4, "pcs"), item("Butter", 2, "tbsp"), item("Eggs", 6, "pcs"));
        List<Recipe> result = RecipeMatcher.findMakeable(p, Arrays.asList(toast, omelette));
        assertEquals(1, result.size());
        assertEquals("Toast", result.get(0).getName());
    }

    // ---------- names ----------

    @Test
    public void singularAndPluralNamesMatch() {
        Recipe r = recipe("Soup", need("tomato", 2, "pcs"), need("onions", 1, "pcs"));
        assertTrue(makeable(r, pantry(item("Tomatoes", 3, "pcs"), item("Onion", 1, "pcs"))));
    }

    @Test
    public void similarButDifferentIngredientsDoNotMatch() {
        Recipe r = recipe("Toast", need("butter", 1, "tbsp"));
        assertFalse(makeable(r, pantry(item("peanut butter", 5, "tbsp"))));
    }

    // ---------- quantities ----------

    @Test
    public void notEnoughQuantity_isNotSuggested() {
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"));
        MatchResult result = RecipeMatcher.evaluate(r, pantry(item("eggs", 2, "pcs")));
        assertFalse(result.isMakeable());
        assertEquals(1, result.getMissingIngredients().size());
    }

    @Test
    public void exactlyTheRequiredQuantity_isEnough() {
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"));
        assertTrue(makeable(r, pantry(item("eggs", 3, "pcs"))));
    }

    @Test
    public void moreThanRequired_isEnough() {
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"));
        assertTrue(makeable(r, pantry(item("eggs", 12, "pcs"))));
    }

    @Test
    public void zeroQuantityInPantry_countsAsAbsent() {
        Recipe r = recipe("Toast", need("bread", 1, "pcs"));
        assertFalse(makeable(r, pantry(item("bread", 0, "pcs"))));
    }

    @Test
    public void quantitiesOfTheSameIngredientAreAddedUp() {
        Recipe r = recipe("Cake", need("flour", 700, "g"));
        assertTrue(makeable(r, pantry(item("flour", 300, "g"), item("Flour", 0.5, "kg"))));
        assertFalse(makeable(r, pantry(item("flour", 300, "g"), item("Flour", 0.3, "kg"))));
    }

    // ---------- units ----------

    @Test
    public void kilogramsCoverGrams() {
        Recipe r = recipe("Pancakes", need("flour", 200, "g"));
        assertTrue(makeable(r, pantry(item("flour", 1, "kg"))));
    }

    @Test
    public void gramsCanFallShortOfKilograms() {
        Recipe r = recipe("Bread", need("flour", 1, "kg"));
        assertFalse(makeable(r, pantry(item("flour", 900, "g"))));
        assertTrue(makeable(r, pantry(item("flour", 1000, "g"))));
    }

    @Test
    public void litresCoverMillilitres() {
        Recipe r = recipe("Pancakes", need("milk", 300, "ml"));
        assertTrue(makeable(r, pantry(item("milk", 0.5, "l"))));
        assertFalse(makeable(r, pantry(item("milk", 0.25, "l"))));
    }

    @Test
    public void spoonsAndCupsConvertToVolume() {
        assertTrue(makeable(recipe("A", need("oil", 1, "tbsp")), pantry(item("oil", 3, "tsp"))));
        assertFalse(makeable(recipe("B", need("oil", 1, "tbsp")), pantry(item("oil", 2, "tsp"))));
        assertTrue(makeable(recipe("C", need("milk", 1, "cup")), pantry(item("milk", 250, "ml"))));
        assertFalse(makeable(recipe("D", need("milk", 1, "cup")), pantry(item("milk", 200, "ml"))));
        assertTrue(makeable(recipe("E", need("milk", 150, "ml")), pantry(item("milk", 1, "cup"))));
    }

    @Test
    public void unitsCannotBeCompared_acrossDimensions() {
        // 500 g of eggs cannot be turned into a number of eggs, so it must not count.
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"));
        assertFalse(makeable(r, pantry(item("eggs", 500, "g"))));
        // Likewise grams vs millilitres.
        assertFalse(makeable(recipe("Pancakes", need("milk", 300, "ml")), pantry(item("milk", 500, "g"))));
    }

    @Test
    public void rightUnitCountsEvenWhenAnotherRowHasAnIncompatibleUnit() {
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"));
        assertTrue(makeable(r, pantry(item("eggs", 500, "g"), item("eggs", 6, "pcs"))));
    }

    @Test
    public void floatingPointNoiseDoesNotBreakAnExactMatch() {
        Recipe r = recipe("Cake", need("flour", 300, "g"));
        assertTrue(makeable(r, pantry(item("flour", 0.1, "kg"), item("flour", 0.2, "kg"))));
    }

    // ---------- same ingredient used twice in one recipe ----------

    @Test
    public void sameStockIsNotCountedTwice() {
        Recipe r = recipe("Rich", need("butter", 1, "tbsp"), need("butter", 1, "tbsp"));
        assertFalse(makeable(r, pantry(item("butter", 1, "tbsp"))));
        assertTrue(makeable(r, pantry(item("butter", 2, "tbsp"))));
    }

    // ---------- expiry is not part of the rule ----------

    @Test
    public void expiryDateDoesNotChangeTheResult() {
        Recipe r = recipe("Toast", need("bread", 1, "pcs"));
        PantryItem old = new PantryItem(1, "bread", 2, "pcs", 1000L);
        assertTrue(makeable(r, pantry(old)));
    }

    // ---------- optional "Almost There" group ----------

    @Test
    public void almostThere_isExactlyOneMissing_andNeverMixesWithMakeable() {
        Recipe ready = recipe("Ready", need("bread", 1, "pcs"));
        Recipe oneMissing = recipe("One missing", need("bread", 1, "pcs"), need("cheese", 50, "g"));
        Recipe twoMissing = recipe("Two missing", need("cheese", 50, "g"), need("ham", 50, "g"));
        List<Recipe> all = Arrays.asList(ready, oneMissing, twoMissing);
        List<PantryItem> p = pantry(item("bread", 2, "pcs"));

        List<Recipe> main = RecipeMatcher.findMakeable(p, all);
        List<MatchResult> almost = RecipeMatcher.findAlmostThere(p, all);

        assertEquals(1, main.size());
        assertEquals("Ready", main.get(0).getName());
        assertEquals(1, almost.size());
        assertEquals("One missing", almost.get(0).getRecipe().getName());
        assertEquals("cheese", almost.get(0).getMissingIngredients().get(0).getName());
    }

    @Test
    public void insufficientQuantityCountsAsTheMissingIngredientForAlmostThere() {
        Recipe r = recipe("Omelette", need("eggs", 3, "pcs"), need("butter", 1, "tbsp"));
        List<MatchResult> almost = RecipeMatcher.findAlmostThere(
                pantry(item("eggs", 1, "pcs"), item("butter", 1, "tbsp")), Arrays.asList(r));
        assertEquals(1, almost.size());
        assertEquals("eggs", almost.get(0).getMissingIngredients().get(0).getName());
    }
}
