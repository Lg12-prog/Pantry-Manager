package com.example.smartpantry.domain;

import java.util.Locale;

/**
 * Converts quantities to a common base so "1 kg" can be compared with "200 g".
 *
 *   mass   -> grams        (g = 1, kg = 1000)
 *   volume -> millilitres  (ml = 1, l = 1000, tsp = 5, tbsp = 15, cup = 240)
 *   count  -> pieces       (pcs = 1)
 *
 * Mass and volume cannot be converted into each other without knowing the ingredient's
 * density, and pieces cannot become weight, so quantities in DIFFERENT dimensions are
 * treated as not comparable (see RecipeMatcher). Unknown units are only comparable with
 * the identical unit.
 *
 * No Android classes, so it can be unit-tested with plain JUnit.
 */
public final class UnitConverter {

    public static final String MASS = "mass";
    public static final String VOLUME = "volume";
    public static final String COUNT = "count";

    private UnitConverter() {
    }

    /** Lower-cased, trimmed, without a trailing full stop. */
    static String normalizeUnit(String unit) {
        if (unit == null) {
            return "";
        }
        String u = unit.trim().toLowerCase(Locale.ROOT);
        if (u.endsWith(".")) {
            u = u.substring(0, u.length() - 1);
        }
        return u;
    }

    /** "mass", "volume", "count", or "unit:xyz" for a unit the app does not know. */
    public static String dimensionOf(String unit) {
        String u = normalizeUnit(unit);
        switch (u) {
            case "g": case "gram": case "grams":
            case "kg": case "kilogram": case "kilograms":
                return MASS;
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
            case "l": case "litre": case "litres": case "liter": case "liters":
            case "tsp": case "teaspoon": case "teaspoons":
            case "tbsp": case "tablespoon": case "tablespoons":
            case "cup": case "cups":
                return VOLUME;
            case "pcs": case "pc": case "piece": case "pieces":
                return COUNT;
            default:
                return "unit:" + u;
        }
    }

    /** How many base units (g, ml or pieces) one of this unit is. */
    static double factorToBase(String unit) {
        switch (normalizeUnit(unit)) {
            case "kg": case "kilogram": case "kilograms":
            case "l": case "litre": case "litres": case "liter": case "liters":
                return 1000.0;
            case "cup": case "cups":
                return 240.0;
            case "tbsp": case "tablespoon": case "tablespoons":
                return 15.0;
            case "tsp": case "teaspoon": case "teaspoons":
                return 5.0;
            default:
                return 1.0;
        }
    }

    public static double toBase(double quantity, String unit) {
        return quantity * factorToBase(unit);
    }
}
