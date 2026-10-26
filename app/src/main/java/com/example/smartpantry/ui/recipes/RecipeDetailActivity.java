package com.example.smartpantry.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartpantry.R;
import com.example.smartpantry.data.model.RecipeIngredient;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.FormatUtils;
import com.example.smartpantry.util.InsetsUtils;
import com.google.android.material.appbar.MaterialToolbar;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        InsetsUtils.applySystemBarPadding(findViewById(R.id.recipe_detail_root));

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        final TextView ingredientsView = findViewById(R.id.recipe_ingredients);
        final TextView stepsView = findViewById(R.id.recipe_steps);

        long recipeId = getIntent().getLongExtra(Constants.EXTRA_RECIPE_ID, Constants.NO_ID);
        RecipeDetailViewModel viewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);

        // The recipe loads in the background; the screen fills in when it arrives.
        viewModel.getRecipe(recipeId).observe(this, recipe -> {
            if (recipe == null) {
                finish();
                return;
            }
            toolbar.setTitle(recipe.getName());

            StringBuilder ingredients = new StringBuilder();
            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                if (ingredients.length() > 0) {
                    ingredients.append('\n');
                }
                ingredients.append("\u2022 ")
                        .append(FormatUtils.formatQuantity(ingredient.getQuantity()))
                        .append(' ')
                        .append(ingredient.getUnit())
                        .append(' ')
                        .append(ingredient.getName());
            }
            ingredientsView.setText(ingredients);

            StringBuilder steps = new StringBuilder();
            int number = 1;
            for (String line : recipe.getSteps().split("\\r?\\n")) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (steps.length() > 0) {
                    steps.append("\n\n");
                }
                steps.append(number++).append(". ").append(line.trim());
            }
            stepsView.setText(steps);
        });
    }
}
