package com.example.smartpantry.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.smartpantry.data.db.AppDatabase;
import com.example.smartpantry.data.db.PantryItemDao;
import com.example.smartpantry.data.model.PantryItem;

import java.util.List;

/**
 * Single access point for pantry data (Room-backed). Reads return LiveData that Room
 * updates automatically; writes run on the database background thread.
 */
public class PantryRepository {

    private static volatile PantryRepository instance;

    private final PantryItemDao dao;
    private final LiveData<List<PantryItem>> items;

    private PantryRepository(Context context) {
        dao = AppDatabase.getInstance(context).pantryItemDao();
        items = dao.observeAll();
    }

    public static PantryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (PantryRepository.class) {
                if (instance == null) {
                    instance = new PantryRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public LiveData<List<PantryItem>> getItems() {
        return items;
    }

    public LiveData<PantryItem> getItem(long id) {
        return dao.observeById(id);
    }

    public void add(final PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.insert(item));
    }

    public void update(final PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.update(item));
    }

    public void delete(final long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> dao.deleteById(id));
    }
}
