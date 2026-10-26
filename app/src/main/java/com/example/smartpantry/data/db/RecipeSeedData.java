package com.example.smartpantry.data.db;

import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The pre-loaded recipe collection (18 recipes). Plain Java so it can be unit-tested.
 * Units come from Constants.UNITS (g, kg, ml, l, tsp, tbsp, cup, pcs).
 */
final class RecipeSeedData {

    private RecipeSeedData() {
    }

    static List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<Recipe>();

        list.add(recipe("Tomato Pasta",
                steps("Boil the pasta in salted water until tender.",
                        "Fry the chopped garlic in the olive oil for one minute.",
                        "Add the chopped tomatoes and simmer for 10 minutes.",
                        "Drain the pasta and toss it through the sauce."),
                ing("pasta", 200, "g"), ing("tomatoes", 4, "pcs"), ing("olive oil", 2, "tbsp"),
                ing("garlic", 2, "pcs"), ing("salt", 1, "tsp")));

        list.add(recipe("Scrambled Eggs on Toast",
                steps("Toast the bread.",
                        "Melt the butter in a pan and add the beaten eggs with the salt.",
                        "Stir gently until just set and serve on the toast."),
                ing("eggs", 3, "pcs"), ing("bread", 2, "pcs"), ing("butter", 1, "tbsp"),
                ing("salt", 0.5, "tsp")));

        list.add(recipe("Egg Fried Rice",
                steps("Fry the chopped onion in the oil until soft.",
                        "Add the cooked rice and stir-fry for a few minutes.",
                        "Push the rice aside and scramble the eggs in the pan.",
                        "Mix everything together with the soy sauce."),
                ing("rice", 2, "cup"), ing("eggs", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("soy sauce", 2, "tbsp"), ing("vegetable oil", 1, "tbsp")));

        list.add(recipe("Simple Pancakes",
                steps("Whisk the flour, sugar, milk and eggs into a smooth batter.",
                        "Melt a little butter in a pan.",
                        "Cook spoonfuls of batter until golden on both sides."),
                ing("flour", 200, "g"), ing("milk", 300, "ml"), ing("eggs", 2, "pcs"),
                ing("butter", 1, "tbsp"), ing("sugar", 1, "tbsp")));

        list.add(recipe("Cheese Omelette",
                steps("Beat the eggs with the salt.",
                        "Melt the butter in a pan and pour in the eggs.",
                        "When almost set, add the grated cheese and fold the omelette over."),
                ing("eggs", 3, "pcs"), ing("cheese", 50, "g"), ing("butter", 1, "tbsp"),
                ing("salt", 0.5, "tsp")));

        list.add(recipe("Vegetable Soup",
                steps("Chop the carrots, potatoes and onion.",
                        "Add them to the stock with the salt.",
                        "Simmer for 25 minutes until the vegetables are soft."),
                ing("carrots", 2, "pcs"), ing("potatoes", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("vegetable stock", 1, "l"), ing("salt", 1, "tsp")));

        list.add(recipe("Mashed Potatoes",
                steps("Peel and chop the potatoes, then boil them in salted water until soft.",
                        "Drain well and mash with the butter and warm milk.",
                        "Season with salt and serve."),
                ing("potatoes", 4, "pcs"), ing("butter", 2, "tbsp"), ing("milk", 100, "ml"),
                ing("salt", 1, "tsp")));

        list.add(recipe("Garlic Bread",
                steps("Mix the softened butter with the crushed garlic.",
                        "Spread it over the bread slices.",
                        "Bake at 200 C for about 10 minutes until golden."),
                ing("bread", 4, "pcs"), ing("butter", 2, "tbsp"), ing("garlic", 2, "pcs")));

        list.add(recipe("Chicken Rice Bowl",
                steps("Cook the rice.",
                        "Slice the chicken and fry it with the chopped onion until cooked through.",
                        "Add the soy sauce and serve over the rice."),
                ing("chicken breast", 300, "g"), ing("rice", 1, "cup"), ing("soy sauce", 2, "tbsp"),
                ing("onion", 1, "pcs")));

        list.add(recipe("Tuna Pasta Salad",
                steps("Boil the pasta, drain and let it cool.",
                        "Chop the tomatoes and mix them with the pasta and flaked tuna.",
                        "Dress with the olive oil and salt."),
                ing("pasta", 150, "g"), ing("canned tuna", 1, "pcs"), ing("tomatoes", 2, "pcs"),
                ing("olive oil", 1, "tbsp"), ing("salt", 0.5, "tsp")));

        list.add(recipe("French Toast",
                steps("Whisk the eggs, milk and sugar together.",
                        "Dip the bread slices in the mixture.",
                        "Fry in the butter until golden on both sides."),
                ing("bread", 4, "pcs"), ing("eggs", 2, "pcs"), ing("milk", 100, "ml"),
                ing("butter", 1, "tbsp"), ing("sugar", 1, "tsp")));

        list.add(recipe("Porridge",
                steps("Combine the oats, milk and salt in a pot.",
                        "Cook over low heat for about 5 minutes, stirring.",
                        "Sweeten with the sugar and serve hot."),
                ing("oats", 1, "cup"), ing("milk", 300, "ml"), ing("sugar", 1, "tbsp"),
                ing("salt", 0.25, "tsp")));

        list.add(recipe("Banana Pancakes",
                steps("Mash the bananas and mix in the eggs and flour.",
                        "Melt a little butter in a pan.",
                        "Cook small spoonfuls until golden on both sides."),
                ing("bananas", 2, "pcs"), ing("eggs", 2, "pcs"), ing("flour", 100, "g"),
                ing("butter", 1, "tbsp")));

        list.add(recipe("Tomato Soup",
                steps("Soften the chopped onion and garlic in the olive oil.",
                        "Add the chopped tomatoes and the stock.",
                        "Simmer for 20 minutes, then blend until smooth."),
                ing("tomatoes", 6, "pcs"), ing("onion", 1, "pcs"), ing("garlic", 2, "pcs"),
                ing("vegetable stock", 500, "ml"), ing("olive oil", 1, "tbsp")));

        list.add(recipe("Cheese Toastie",
                steps("Butter the outside of the bread slices.",
                        "Place the cheese between them.",
                        "Toast in a pan until golden and the cheese has melted."),
                ing("bread", 2, "pcs"), ing("cheese", 60, "g"), ing("butter", 1, "tbsp")));

        list.add(recipe("Roast Potatoes",
                steps("Heat the oven to 220 C.",
                        "Chop the potatoes and toss them with the olive oil and salt.",
                        "Roast for about 40 minutes, turning once, until crisp."),
                ing("potatoes", 800, "g"), ing("olive oil", 3, "tbsp"), ing("salt", 1, "tsp")));

        list.add(recipe("Lentil Stew",
                steps("Fry the chopped onion, carrots and garlic in the olive oil.",
                        "Add the lentils and the stock.",
                        "Simmer for about 30 minutes until the lentils are soft."),
                ing("lentils", 1, "cup"), ing("carrots", 2, "pcs"), ing("onion", 1, "pcs"),
                ing("garlic", 2, "pcs"), ing("vegetable stock", 750, "ml"),
                ing("olive oil", 1, "tbsp")));

        list.add(recipe("Rice Pudding",
                steps("Combine the rice, milk and sugar in a pot.",
                        "Simmer very gently for about 30 minutes, stirring often.",
                        "Stir in the butter and serve warm."),
                ing("rice", 0.5, "cup"), ing("milk", 500, "ml"), ing("sugar", 2, "tbsp"),
                ing("butter", 1, "tsp")));

        return Collections.unmodifiableList(list);
    }

    private static Recipe recipe(String name, String steps, RecipeIngredient... ingredients) {
        List<RecipeIngredient> ingredientList = new ArrayList<RecipeIngredient>();
        for (RecipeIngredient ingredient : ingredients) {
            ingredientList.add(ingredient);
        }
        return new Recipe(0, name, steps, ingredientList);
    }

    private static RecipeIngredient ing(String name, double quantity, String unit) {
        return new RecipeIngredient(name, quantity, unit);
    }

    /** Joins steps with newlines (String.join needs API 26, so it is done by hand). */
    private static String steps(String... lines) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                builder.append('\n');
            }
            builder.append(lines[i]);
        }
        return builder.toString();
    }
}
