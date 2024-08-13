package com.example.asm2_applicationdevelopment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Model.Budget;
import com.example.asm2_applicationdevelopment.R;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgetList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Budget budget);
    }

    public BudgetAdapter(List<Budget> budgetList, OnItemClickListener listener) {
        this.budgetList = budgetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.bind(budget, listener);
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public void updateBudgets(List<Budget> newBudgetList) {
        this.budgetList = newBudgetList;
        notifyDataSetChanged();
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCategory;
        private final TextView tvAmount;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }

        public void bind(final Budget budget, final OnItemClickListener listener) {
            tvCategory.setText(budget.getCategory());
            tvAmount.setText(String.format("%.2f", budget.getAmount()));
            itemView.setOnClickListener(v -> listener.onItemClick(budget));
        }
    }
}
