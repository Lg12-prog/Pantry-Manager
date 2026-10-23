package com.example.smartpantry.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartpantry.R;
import com.example.smartpantry.data.model.Recipe;
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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        long recipeId = getIntent().getLongExtra(Constants.EXTRA_RECIPE_ID, Constants.NO_ID);
        RecipeDetailViewModel viewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);
        Recipe recipe = viewModel.getRecipe(recipeId);
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

        StringBuilder steps = new StringBuilder();
        String[] lines = recipe.getSteps().split("\\r?\\n");
        int number = 1;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            if (steps.length() > 0) {
                steps.append("\n\n");
            }
            steps.append(number++).append(". ").append(line.trim());
        }

        ((TextView) findViewById(R.id.recipe_ingredients)).setText(ingredients);
        ((TextView) findViewById(R.id.recipe_steps)).setText(steps);
    }
}
