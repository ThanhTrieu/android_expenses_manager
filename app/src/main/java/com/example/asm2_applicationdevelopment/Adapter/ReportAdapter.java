package com.example.asm2_applicationdevelopment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Model.Expense;
import com.example.asm2_applicationdevelopment.Model.Income;
import com.example.asm2_applicationdevelopment.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Income> incomes;
    private List<Expense> expenses;
    private List<Object> filteredList;
    private FilterType filterType;

    public enum FilterType {
        ALL, INCOME_ONLY, EXPENSE_ONLY
    }

    public ReportAdapter(List<Income> incomes, List<Expense> expenses) {
        this.incomes = incomes;
        this.expenses = expenses;
        this.filteredList = new ArrayList<>();
        this.filterType = FilterType.ALL;
        applyFilter();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Object item = filteredList.get(position);
        if (item instanceof Income) {
            Income income = (Income) item;
            holder.tvAmount.setText(String.format("%.3f", income.getAmount()));
            holder.tvDescription.setText(income.getDescription());
            holder.tvDate.setText(income.getDate());
        } else if (item instanceof Expense) {
            Expense expense = (Expense) item;
            holder.tvAmount.setText(String.format("%.3f", expense.getAmount()));
            holder.tvDescription.setText(expense.getDescription());
            holder.tvDate.setText(expense.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public void applyFilter() {
        filteredList.clear();
        if (filterType == FilterType.ALL || filterType == FilterType.INCOME_ONLY) {
            filteredList.addAll(incomes);
        }
        if (filterType == FilterType.ALL || filterType == FilterType.EXPENSE_ONLY) {
            filteredList.addAll(expenses);
        }
        notifyDataSetChanged();
    }

    public void sortByAmountAscending() {
        Collections.sort(filteredList, (o1, o2) -> {
            double amount1 = (o1 instanceof Income) ? ((Income) o1).getAmount() : ((Expense) o1).getAmount();
            double amount2 = (o2 instanceof Income) ? ((Income) o2).getAmount() : ((Expense) o2).getAmount();
            return Double.compare(amount1, amount2);
        });
        notifyDataSetChanged();
    }

    public void sortByAmountDescending() {
        Collections.sort(filteredList, (o1, o2) -> {
            double amount1 = (o1 instanceof Income) ? ((Income) o1).getAmount() : ((Expense) o1).getAmount();
            double amount2 = (o2 instanceof Income) ? ((Income) o2).getAmount() : ((Expense) o2).getAmount();
            return Double.compare(amount2, amount1);
        });
        notifyDataSetChanged();
    }

    public void sortByDateNewestFirst() {
        Collections.sort(filteredList, (o1, o2) -> {
            String date1 = (o1 instanceof Income) ? ((Income) o1).getDate() : ((Expense) o1).getDate();
            String date2 = (o2 instanceof Income) ? ((Income) o2).getDate() : ((Expense) o2).getDate();
            return date2.compareTo(date1);
        });
        notifyDataSetChanged();
    }

    public void sortByDateOldestFirst() {
        Collections.sort(filteredList, (o1, o2) -> {
            String date1 = (o1 instanceof Income) ? ((Income) o1).getDate() : ((Expense) o1).getDate();
            String date2 = (o2 instanceof Income) ? ((Income) o2).getDate() : ((Expense) o2).getDate();
            return date1.compareTo(date2);
        });
        notifyDataSetChanged();
    }

    public void updateData(List<Income> incomes, List<Expense> expenses) {
        this.incomes = incomes;
        this.expenses = expenses;
        applyFilter();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvDescription, tvDate;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
