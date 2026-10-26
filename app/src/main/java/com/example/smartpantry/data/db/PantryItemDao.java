package com.example.smartpantry.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.smartpantry.data.model.PantryItem;

import java.util.List;

/** CRUD operations for pantry items. Inserts/updates/deletes must run off the main thread. */
@Dao
public interface PantryItemDao {

    // READ (list) - LiveData queries run in the background and re-emit whenever the table changes.
    @Query("SELECT * FROM pantry_items ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<PantryItem>> observeAll();

    // READ (single)
    @Query("SELECT * FROM pantry_items WHERE id = :id")
    LiveData<PantryItem> observeById(long id);

    // CREATE - returns the new row id
    @Insert
    long insert(PantryItem item);

    // UPDATE - matches the row by primary key
    @Update
    int update(PantryItem item);

    // DELETE
    @Query("DELETE FROM pantry_items WHERE id = :id")
    void deleteById(long id);
}
