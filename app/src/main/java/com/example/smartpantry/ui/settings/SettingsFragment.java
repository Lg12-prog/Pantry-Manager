package com.example.smartpantry.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpantry.R;

/**
 * Settings are stored automatically in the default SharedPreferences using the keys
 * in Constants (PREF_EXPIRY_ALERTS, PREF_UNITS). Nothing reads them yet.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
