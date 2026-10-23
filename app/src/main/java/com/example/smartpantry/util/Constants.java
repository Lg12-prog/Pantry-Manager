package com.example.smartpantry.util;

public final class Constants {

    private Constants() {
    }

    /** Marker meaning "no id" (a new, unsaved item). */
    public static final long NO_ID = -1L;

    // Intent extras
    public static final String EXTRA_PANTRY_ITEM_ID = "com.example.smartpantry.EXTRA_PANTRY_ITEM_ID";
    public static final String EXTRA_RECIPE_ID = "com.example.smartpantry.EXTRA_RECIPE_ID";

    // Preference keys (must match res/xml/preferences.xml)
    public static final String PREF_EXPIRY_ALERTS = "pref_expiry_alerts";
    public static final String PREF_UNITS = "pref_units";

    /** Fixed list of units so that strict matching can compare units reliably later. */
    public static final String[] UNITS = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};
}
