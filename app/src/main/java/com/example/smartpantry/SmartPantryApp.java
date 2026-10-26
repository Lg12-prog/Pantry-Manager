package com.example.smartpantry;

import android.app.Application;

import com.example.smartpantry.data.db.AppDatabase;

public class SmartPantryApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Open the database in the background at launch. On the very first run this creates
        // the file and seeds the recipes, so the first screen does not have to wait for it.
        AppDatabase.databaseWriteExecutor.execute(
                () -> AppDatabase.getInstance(this).getOpenHelper().getWritableDatabase());
    }
}
