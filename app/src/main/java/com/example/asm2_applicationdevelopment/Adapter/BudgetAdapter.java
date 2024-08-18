package com.example.asm2_applicationdevelopment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Model.Budget;
import com.example.asm2_applicationdevelopment.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgetList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Budget budget);
    }

    public BudgetAdapter(List<Budget> budgetList, OnItemClickListener onItemClickListener) {
        this.budgetList = budgetList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_budget, parent, false); // Ensure this layout file is used
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.tvDescription.setText(budget.getDescription());
        holder.tvCategory.setText(budget.getCategory());
        holder.tvAmount.setText(String.valueOf(budget.getAmount()));
        holder.tvStartDate.setText(budget.getStartDate());
        holder.tvEndDate.setText(budget.getEndDate());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(budget));
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    // Method to update the list of budgets
    public void updateBudgets(List<Budget> newBudgets) {
        this.budgetList = new ArrayList<>(newBudgets);
        notifyDataSetChanged();
    }

    class BudgetViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvCategory;
        TextView tvAmount;
        TextView tvStartDate;
        TextView tvEndDate;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDes);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStartDate = itemView.findViewById(R.id.tvStartDateLabel);
            tvEndDate = itemView.findViewById(R.id.tvEndDateLabel);
        }
    }
}
