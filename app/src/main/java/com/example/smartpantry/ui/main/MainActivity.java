package com.example.smartpantry.ui.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.R;
import com.example.smartpantry.ui.pantry.PantryFragment;
import com.example.smartpantry.ui.recipes.SuggestedRecipesFragment;
import com.example.smartpantry.ui.settings.SettingsFragment;
import com.example.smartpantry.util.InsetsUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InsetsUtils.applySystemBarPadding(findViewById(R.id.main_root));

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment;
            int titleRes;
            if (id == R.id.nav_pantry) {
                fragment = new PantryFragment();
                titleRes = R.string.title_pantry;
            } else if (id == R.id.nav_suggested) {
                fragment = new SuggestedRecipesFragment();
                titleRes = R.string.title_suggested;
            } else if (id == R.id.nav_settings) {
                fragment = new SettingsFragment();
                titleRes = R.string.title_settings;
            } else {
                return false;
            }
            toolbar.setTitle(titleRes);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        });

        if (savedInstanceState == null) {
            // Triggers the listener above, which shows the Pantry screen.
            bottomNav.setSelectedItemId(R.id.nav_pantry);
        } else {
            // After rotation the fragment is restored automatically; only the title is reset.
            int selected = bottomNav.getSelectedItemId();
            if (selected == R.id.nav_suggested) {
                toolbar.setTitle(R.string.title_suggested);
            } else if (selected == R.id.nav_settings) {
                toolbar.setTitle(R.string.title_settings);
            } else {
                toolbar.setTitle(R.string.title_pantry);
            }
        }
    }
}
