package com.example.smartpantry.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.data.model.PantryItem;
import com.example.smartpantry.util.DateUtils;
import com.example.smartpantry.util.FormatUtils;

import java.util.Objects;

public class PantryAdapter extends ListAdapter<PantryItem, PantryAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    private static final DiffUtil.ItemCallback<PantryItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PantryItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
                    return Objects.equals(oldItem.getName(), newItem.getName())
                            && oldItem.getQuantity() == newItem.getQuantity()
                            && Objects.equals(oldItem.getUnit(), newItem.getUnit())
                            && Objects.equals(oldItem.getExpiryDate(), newItem.getExpiryDate());
                }
            };

    private final OnItemClickListener listener;

    public PantryAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView quantity;
        private final TextView expiry;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            expiry = itemView.findViewById(R.id.item_expiry);
        }

        void bind(final PantryItem item, final OnItemClickListener listener) {
            name.setText(item.getName());
            quantity.setText(FormatUtils.formatQuantity(item.getQuantity()) + " " + item.getUnit());
            Long expiryDate = item.getExpiryDate();
            if (expiryDate != null) {
                expiry.setText(itemView.getContext().getString(
                        R.string.expires_on, DateUtils.formatDate(expiryDate)));
                expiry.setVisibility(View.VISIBLE);
            } else {
                expiry.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
