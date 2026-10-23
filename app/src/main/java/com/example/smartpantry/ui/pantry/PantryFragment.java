package com.example.smartpantry.ui.pantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PantryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pantry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.pantry_recycler);
        final TextView emptyView = view.findViewById(R.id.pantry_empty);
        FloatingActionButton addButton = view.findViewById(R.id.fab_add);

        final PantryAdapter adapter = new PantryAdapter(item -> {
            Intent intent = new Intent(requireContext(), AddEditPantryItemActivity.class);
            intent.putExtra(Constants.EXTRA_PANTRY_ITEM_ID, item.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        PantryViewModel viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
            emptyView.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        });

        addButton.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddEditPantryItemActivity.class)));
    }
}
