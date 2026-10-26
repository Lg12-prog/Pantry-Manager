package com.example.smartpantry.ui.pantry;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.repository.PantryRepository;

import java.util.List;

public class PantryViewModel extends AndroidViewModel {

    private final PantryRepository repository;

    public PantryViewModel(@NonNull Application application) {
        super(application);
        repository = PantryRepository.getInstance(application);
    }

    public LiveData<List<PantryItem>> getItems() {
        return repository.getItems();
    }

    public LiveData<PantryItem> getItem(long id) {
        return repository.getItem(id);
    }

    public void addItem(PantryItem item) {
        repository.add(item);
    }

    public void updateItem(PantryItem item) {
        repository.update(item);
    }

    public void deleteItem(long id) {
        repository.delete(id);
    }
}
