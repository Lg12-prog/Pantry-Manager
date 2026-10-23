package com.example.smartpantry;

import android.app.Application;

/**
 * Application class. Later this is where the database is created and the
 * recipes are seeded on first run.
 */
public class SmartPantryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO (database step): initialise Room and seed the 15-20 recipes on first run.
    }
}
