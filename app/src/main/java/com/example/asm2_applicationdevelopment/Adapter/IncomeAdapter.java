package com.example.asm2_applicationdevelopment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Model.Income;
import com.example.asm2_applicationdevelopment.R;

import java.util.ArrayList;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private List<Income> incomeList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Income income);
    }

    public IncomeAdapter(List<Income> incomeList, OnItemClickListener onItemClickListener) {
        this.incomeList = incomeList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income income = incomeList.get(position);
        holder.tvDescription.setText(income.getDescription());
        holder.tvDate.setText(income.getDate());
        holder.tvAmount.setText(String.valueOf(income.getAmount()));
        holder.tvCategory.setText(income.getCategory());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(income));
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    // Method to update the list of incomes
    public void updateIncomes(List<Income> newIncomes) {
        this.incomeList = new ArrayList<>(newIncomes);
        notifyDataSetChanged();
    }

    class IncomeViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvDate;
        TextView tvAmount;
        TextView tvCategory;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
