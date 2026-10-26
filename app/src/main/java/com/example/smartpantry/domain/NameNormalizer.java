package com.example.smartpantry.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Decides whether two ingredient names mean the same thing, ignoring simple real-world
 * messiness: upper/lower case, extra spaces and punctuation, singular vs plural
 * ("tomato" vs "tomatoes") and a few filler words ("fresh", "chopped", "garlic cloves").
 *
 * It is NOT a full language model. Names must otherwise be the same, word for word:
 * "butter" does NOT match "peanut butter" and "egg" does NOT match "eggplant".
 *
 * No Android classes, so it can be unit-tested with plain JUnit.
 */
public final class NameNormalizer {

    /**
     * Words that describe an ingredient without changing what it is. Dropped before comparing,
     * unless a name consists of nothing else (so "cloves", the spice, still works).
     */
    private static final Set<String> IGNORED_WORDS = new HashSet<String>(Arrays.asList(
            "fresh", "chopped", "diced", "sliced", "minced", "grated",
            "large", "small", "medium", "ripe", "organic",
            "clove", "cloves"));

    private NameNormalizer() {
    }

    /** True if the two names refer to the same ingredient. */
    public static boolean sameIngredient(String a, String b) {
        return sameForms(toWordForms(a), toWordForms(b));
    }

    /**
     * Breaks a name into words; each word is represented by the set of spellings it could
     * have as a singular. Two names match when they have the same number of words and, word
     * by word, those sets overlap.
     */
    static List<Set<String>> toWordForms(String name) {
        List<String> words = new ArrayList<String>();
        if (name != null) {
            String lower = name.toLowerCase(Locale.ROOT);
            StringBuilder cleaned = new StringBuilder();
            for (int i = 0; i < lower.length(); i++) {
                char ch = lower.charAt(i);
                cleaned.append(Character.isLetter(ch) ? ch : ' ');
            }
            for (String word : cleaned.toString().split("\\s+")) {
                if (!word.isEmpty()) {
                    words.add(word);
                }
            }
        }

        List<String> kept = new ArrayList<String>();
        for (String word : words) {
            if (!IGNORED_WORDS.contains(word)) {
                kept.add(word);
            }
        }
        if (kept.isEmpty()) {
            kept = words;
        }

        List<Set<String>> forms = new ArrayList<Set<String>>();
        for (String word : kept) {
            forms.add(singularForms(word));
        }
        return forms;
    }

    static boolean sameForms(List<Set<String>> a, List<Set<String>> b) {
        if (a.isEmpty() || b.isEmpty() || a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!overlaps(a.get(i), b.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean overlaps(Set<String> a, Set<String> b) {
        for (String form : a) {
            if (b.contains(form)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The word itself plus every singular it could plausibly come from. Several candidates are
     * produced on purpose (e.g. "cookies" -> "cooky" and "cookie") because the right one
     * depends on the word; matching succeeds if ANY candidate lines up.
     */
    static Set<String> singularForms(String word) {
        Set<String> forms = new LinkedHashSet<String>();
        forms.add(word);
        int n = word.length();

        if (n > 3 && word.endsWith("ies")) {          // berries -> berry, cookies -> cookie, chilies -> chili
            forms.add(word.substring(0, n - 3) + "y");
            forms.add(word.substring(0, n - 1));
            forms.add(word.substring(0, n - 2));
        }
        if (n > 3 && word.endsWith("oes")) {          // tomatoes -> tomato, shoes -> shoe
            forms.add(word.substring(0, n - 2));
            forms.add(word.substring(0, n - 1));
        }
        if (n > 3 && (word.endsWith("ches") || word.endsWith("shes") || word.endsWith("xes")
                || word.endsWith("sses") || word.endsWith("zes"))) {   // peaches -> peach, radishes -> radish
            forms.add(word.substring(0, n - 2));
        }
        if (n > 3 && word.endsWith("ves")) {          // loaves -> loaf, knives -> knife, olives -> olive
            forms.add(word.substring(0, n - 3) + "f");
            forms.add(word.substring(0, n - 3) + "fe");
            forms.add(word.substring(0, n - 1));
        }
        if (n > 2 && word.endsWith("s") && !word.endsWith("ss")) {     // eggs -> egg, peas -> pea
            forms.add(word.substring(0, n - 1));
        }
        return forms;
    }
}
