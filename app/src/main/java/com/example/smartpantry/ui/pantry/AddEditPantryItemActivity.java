package com.example.smartpantry.ui.pantry;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartpantry.R;
import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.util.Constants;
import com.example.smartpantry.util.DateUtils;
import com.example.smartpantry.util.FormatUtils;
import com.example.smartpantry.util.InsetsUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;

/** Adds a new pantry item, or edits/deletes an existing one when an item id is passed in. */
public class AddEditPantryItemActivity extends AppCompatActivity {

    private static final String STATE_EXPIRY = "state_expiry";

    private PantryViewModel viewModel;
    private long itemId = Constants.NO_ID;
    /** Expiry as UTC-midnight millis (what MaterialDatePicker returns), or null. */
    private Long expiryMillis = null;
    /** True once the edit form has been filled from the database (so later emissions do not overwrite typing). */
    private boolean formFilled = false;

    private TextInputLayout nameLayout;
    private TextInputLayout quantityLayout;
    private TextInputLayout unitLayout;
    private TextInputEditText nameInput;
    private TextInputEditText quantityInput;
    private TextInputEditText expiryInput;
    private AutoCompleteTextView unitInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_pantry_item);
        InsetsUtils.applySystemBarPadding(findViewById(R.id.add_edit_root));

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        nameLayout = findViewById(R.id.layout_name);
        quantityLayout = findViewById(R.id.layout_quantity);
        unitLayout = findViewById(R.id.layout_unit);
        nameInput = findViewById(R.id.input_name);
        quantityInput = findViewById(R.id.input_quantity);
        unitInput = findViewById(R.id.input_unit);
        expiryInput = findViewById(R.id.input_expiry);
        MaterialButton clearDateButton = findViewById(R.id.button_clear_date);
        MaterialButton saveButton = findViewById(R.id.button_save);
        MaterialButton deleteButton = findViewById(R.id.button_delete);

        itemId = getIntent().getLongExtra(Constants.EXTRA_PANTRY_ITEM_ID, Constants.NO_ID);
        final boolean editing = itemId != Constants.NO_ID;
        toolbar.setTitle(editing ? R.string.edit_item : R.string.add_item);
        deleteButton.setVisibility(editing ? View.VISIBLE : View.GONE);

        if (savedInstanceState != null) {
            // Text fields restore themselves; only the date value needs restoring by hand.
            if (savedInstanceState.containsKey(STATE_EXPIRY)) {
                expiryMillis = savedInstanceState.getLong(STATE_EXPIRY);
            }
        } else if (editing) {
            // The item loads in the background from the database; fill the form once.
            viewModel.getItem(itemId).observe(this, item -> {
                if (item == null) {
                    finish();
                    return;
                }
                if (formFilled) {
                    return;
                }
                formFilled = true;
                nameInput.setText(item.getName());
                quantityInput.setText(FormatUtils.formatQuantity(item.getQuantity()));
                unitInput.setText(item.getUnit(), false);
                expiryMillis = item.getExpiryDate();
                if (expiryMillis != null) {
                    expiryInput.setText(DateUtils.formatDate(expiryMillis));
                }
            });
        }

        expiryInput.setOnClickListener(v -> showDatePicker());
        clearDateButton.setOnClickListener(v -> {
            expiryMillis = null;
            expiryInput.setText("");
        });
        saveButton.setOnClickListener(v -> save());
        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set in onResume so the dropdown always lists every unit, even after the saved
        // text is restored on rotation (otherwise it can show only the selected unit).
        unitInput.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, Constants.UNITS));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (expiryMillis != null) {
            outState.putLong(STATE_EXPIRY, expiryMillis);
        }
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder =
                MaterialDatePicker.Builder.datePicker().setTitleText(R.string.expiry_date);
        if (expiryMillis != null) {
            builder.setSelection(expiryMillis);
        }
        MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(selection -> {
            expiryMillis = selection;
            expiryInput.setText(DateUtils.formatDate(selection));
        });
        picker.show(getSupportFragmentManager(), "expiry_picker");
    }

    private void save() {
        String name = textOf(nameInput).trim();
        String quantityText = textOf(quantityInput).trim().replace(',', '.');
        String unit = unitInput.getText().toString().trim();

        nameLayout.setError(null);
        quantityLayout.setError(null);
        unitLayout.setError(null);

        boolean valid = true;

        if (name.isEmpty()) {
            nameLayout.setError(getString(R.string.error_name_required));
            valid = false;
        }

        double quantity = 0;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantity = 0;
        }
        if (quantity <= 0) {
            quantityLayout.setError(getString(R.string.error_quantity_invalid));
            valid = false;
        }

        if (!Arrays.asList(Constants.UNITS).contains(unit)) {
            unitLayout.setError(getString(R.string.error_unit_required));
            valid = false;
        }

        if (!valid) {
            return;
        }

        // A new object is created even when editing so the list's DiffUtil sees the change.
        if (itemId != Constants.NO_ID) {
            viewModel.updateItem(new PantryItem(itemId, name, quantity, unit, expiryMillis));
        } else {
            viewModel.addItem(new PantryItem(Constants.NEW_ITEM_ID, name, quantity, unit, expiryMillis));
        }
        finish();
    }

    private void confirmDelete() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete_item)
                .setMessage(R.string.delete_item_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    viewModel.deleteItem(itemId);
                    finish();
                })
                .show();
    }

    private static String textOf(TextInputEditText editText) {
        CharSequence text = editText.getText();
        return text == null ? "" : text.toString();
    }
}
