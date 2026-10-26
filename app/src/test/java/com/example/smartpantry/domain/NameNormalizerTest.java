package com.example.smartpantry.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NameNormalizerTest {

    @Test
    public void tomatoMatchesTomatoes_bothDirections() {
        assertTrue(NameNormalizer.sameIngredient("tomato", "tomatoes"));
        assertTrue(NameNormalizer.sameIngredient("tomatoes", "tomato"));
    }

    @Test
    public void simplePlurals() {
        assertTrue(NameNormalizer.sameIngredient("egg", "eggs"));
        assertTrue(NameNormalizer.sameIngredient("potatoes", "potato"));
        assertTrue(NameNormalizer.sameIngredient("carrot", "carrots"));
        assertTrue(NameNormalizer.sameIngredient("bean", "beans"));
        assertTrue(NameNormalizer.sameIngredient("pea", "peas"));
        assertTrue(NameNormalizer.sameIngredient("onion", "onions"));
    }

    @Test
    public void irregularLookingPlurals() {
        assertTrue(NameNormalizer.sameIngredient("berry", "berries"));
        assertTrue(NameNormalizer.sameIngredient("cherry", "cherries"));
        assertTrue(NameNormalizer.sameIngredient("cookie", "cookies"));
        assertTrue(NameNormalizer.sameIngredient("peach", "peaches"));
        assertTrue(NameNormalizer.sameIngredient("radish", "radishes"));
        assertTrue(NameNormalizer.sameIngredient("loaf", "loaves"));
        assertTrue(NameNormalizer.sameIngredient("olive", "olives"));
        assertTrue(NameNormalizer.sameIngredient("cheese", "cheeses"));
        assertTrue(NameNormalizer.sameIngredient("chili", "chilies"));
        assertTrue(NameNormalizer.sameIngredient("mango", "mangoes"));
    }

    @Test
    public void ignoresCaseSpacesAndPunctuation() {
        assertTrue(NameNormalizer.sameIngredient("Tomatoes", "tomato"));
        assertTrue(NameNormalizer.sameIngredient("  OLIVE   Oil ", "olive oil"));
        assertTrue(NameNormalizer.sameIngredient("olive-oil", "Olive Oil"));
        assertTrue(NameNormalizer.sameIngredient("eggs.", "egg"));
    }

    @Test
    public void multiWordNamesWithPlurals() {
        assertTrue(NameNormalizer.sameIngredient("chicken breast", "Chicken Breasts"));
        assertTrue(NameNormalizer.sameIngredient("olive oil", "olive oils"));
    }

    @Test
    public void fillerWordsAreIgnored() {
        assertTrue(NameNormalizer.sameIngredient("Fresh Tomatoes", "tomato"));
        assertTrue(NameNormalizer.sameIngredient("chopped onion", "onions"));
        assertTrue(NameNormalizer.sameIngredient("garlic cloves", "garlic"));
        assertTrue(NameNormalizer.sameIngredient("grated cheese", "cheese"));
    }

    @Test
    public void aNameMadeOnlyOfFillerWordsStillWorks() {
        assertTrue(NameNormalizer.sameIngredient("cloves", "clove"));
        assertFalse(NameNormalizer.sameIngredient("cloves", "garlic"));
    }

    @Test
    public void differentIngredientsDoNotMatch() {
        assertFalse(NameNormalizer.sameIngredient("butter", "peanut butter"));
        assertFalse(NameNormalizer.sameIngredient("egg", "eggplant"));
        assertFalse(NameNormalizer.sameIngredient("rice", "rice vinegar"));
        assertFalse(NameNormalizer.sameIngredient("tomato", "potato"));
        assertFalse(NameNormalizer.sameIngredient("pasta", "pasty"));
        assertFalse(NameNormalizer.sameIngredient("oil", "olive oil"));
        assertFalse(NameNormalizer.sameIngredient("olive oil", "oil olive "));
    }

    @Test
    public void blankOrNullNamesNeverMatch() {
        assertFalse(NameNormalizer.sameIngredient("", ""));
        assertFalse(NameNormalizer.sameIngredient(null, "egg"));
        assertFalse(NameNormalizer.sameIngredient("egg", "   "));
        assertFalse(NameNormalizer.sameIngredient("123", "egg"));
    }

    @Test
    public void wordsEndingInSsAreNotTreatedAsPlurals() {
        assertTrue(NameNormalizer.sameIngredient("glass", "glasses"));
        assertFalse(NameNormalizer.sameIngredient("glass", "glas"));
    }
}
