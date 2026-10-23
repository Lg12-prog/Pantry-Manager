package com.example.smartpantry.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartpantry.data.model.PantryItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TEMPORARY in-memory implementation so the UI can be built and tested.
 * In the database step, replace the internals with Room DAO calls; the public
 * methods (and therefore the ViewModels and screens) should not need to change,
 * except that writes must move off the main thread.
 *
 * All methods must be called from the main thread (they use setValue).
 */
public class PantryRepository {

    private static PantryRepository instance;

    private final MutableLiveData<List<PantryItem>> items =
            new MutableLiveData<List<PantryItem>>(new ArrayList<PantryItem>());
    private long nextId = 1;

    private PantryRepository() {
    }

    public static synchronized PantryRepository getInstance() {
        if (instance == null) {
            instance = new PantryRepository();
        }
        return instance;
    }

    public LiveData<List<PantryItem>> getItems() {
        return items;
    }

    public PantryItem getById(long id) {
        List<PantryItem> current = items.getValue();
        if (current == null) {
            return null;
        }
        for (PantryItem item : current) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void add(PantryItem item) {
        item.setId(nextId++);
        List<PantryItem> updated = copyOfCurrent();
        updated.add(item);
        items.setValue(updated);
    }

    public void update(PantryItem item) {
        List<PantryItem> updated = copyOfCurrent();
        for (int i = 0; i < updated.size(); i++) {
            if (updated.get(i).getId() == item.getId()) {
                updated.set(i, item);
                break;
            }
        }
        items.setValue(updated);
    }

    public void delete(long id) {
        List<PantryItem> updated = copyOfCurrent();
        Iterator<PantryItem> iterator = updated.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                break;
            }
        }
        items.setValue(updated);
    }

    /** A new list each time, because ListAdapter/DiffUtil needs a new instance to detect changes. */
    private List<PantryItem> copyOfCurrent() {
        List<PantryItem> current = items.getValue();
        if (current == null) {
            return new ArrayList<PantryItem>();
        }
        return new ArrayList<PantryItem>(current);
    }
}
