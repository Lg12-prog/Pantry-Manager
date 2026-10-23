package com.example.smartpantry.ui.pantry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.data.repository.PantryRepository;

import java.util.List;

public class PantryViewModel extends ViewModel {

    private final PantryRepository repository = PantryRepository.getInstance();

    public LiveData<List<PantryItem>> getItems() {
        return repository.getItems();
    }

    public PantryItem getItem(long id) {
        return repository.getById(id);
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
