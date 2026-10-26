package com.example.smartpantry.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.smartpantry.data.model.RecipeWithIngredients;

import java.util.List;

/** Read-only access to the seeded recipe collection. */
@Dao
public interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<RecipeWithIngredients>> observeAll();

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<RecipeWithIngredients> observeById(long id);
}
