package com.example.smartpantry.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.model.Recipe;
import com.example.smartpantry.data.model.RecipeIngredient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The app's SQLite database (via Room).
 *
 * If you change an entity (add/rename a column) you must raise {@code version} and add a
 * Migration. While developing, the quick alternative is to uninstall the app from the
 * emulator so the database is recreated (and re-seeded).
 */
@Database(
        entities = {PantryItem.class, Recipe.class, RecipeIngredient.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "smart_pantry.db";

    /** Single background thread for writes, so inserts/updates/deletes happen in order. */
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    private static volatile AppDatabase instance;

    public abstract PantryItemDao pantryItemDao();

    public abstract RecipeDao recipeDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .addCallback(new RoomDatabase.Callback() {
                                /** Called once, when the database file is first created. */
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    RecipeSeeder.seed(db);
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }
}
