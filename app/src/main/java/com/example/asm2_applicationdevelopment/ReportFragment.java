package com.example.asm2_applicationdevelopment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.ReportAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;
import com.example.asm2_applicationdevelopment.Model.Income;

import java.util.List;

public class ReportFragment extends Fragment {

    private TextView tvTotalIncomeReport;
    private TextView tvTotalExpenseReport;
    private RecyclerView rvReport;
    private Button btnFilterReports, btnSortReports;
    private IncomeDatabase incomeDatabase;
    private ExpenseDatabase expenseDatabase;
    private ReportAdapter reportAdapter;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomeDatabase = new IncomeDatabase(getActivity());
        expenseDatabase = new ExpenseDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        // Initialize UI components
        tvTotalIncomeReport = view.findViewById(R.id.tvTotalIncomeReport);
        tvTotalExpenseReport = view.findViewById(R.id.tvTotalExpenseReport);
        rvReport = view.findViewById(R.id.rvReport);
        btnFilterReports = view.findViewById(R.id.btnFilterReports);
        btnSortReports = view.findViewById(R.id.btnSortReports);

        // Set up RecyclerView
        List<Income> incomes = incomeDatabase.getAllIncomes();
        List<Expense> expenses = expenseDatabase.getAllExpenses();
        reportAdapter = new ReportAdapter(incomes, expenses);
        rvReport.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReport.setAdapter(reportAdapter);

        // Calculate and display the total amounts
        updateTotalAmounts();

        // Set click listeners for filter and sort buttons
        btnFilterReports.setOnClickListener(v -> filterReports());
        btnSortReports.setOnClickListener(v -> sortReports());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when the fragment resumes
        List<Income> incomes = incomeDatabase.getAllIncomes();
        List<Expense> expenses = expenseDatabase.getAllExpenses();
        reportAdapter.updateData(incomes, expenses);
        updateTotalAmounts();
    }

    private void updateTotalAmounts() {
        List<Income> incomes = incomeDatabase.getAllIncomes();
        List<Expense> expenses = expenseDatabase.getAllExpenses();

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        tvTotalIncomeReport.setText(String.format("%.3f", totalIncome));
        tvTotalExpenseReport.setText(String.format("%.3f", totalExpense));
    }

    private void filterReports() {
        // Example filter: Filter by income type
        String[] filterOptions = {"All", "Income Only", "Expense Only"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter Reports")
                .setItems(filterOptions, (dialog, which) -> {
                    switch (which) {
                        case 0: // All
                            reportAdapter.setFilterType(ReportAdapter.FilterType.ALL);
                            break;
                        case 1: // Income Only
                            reportAdapter.setFilterType(ReportAdapter.FilterType.INCOME_ONLY);
                            break;
                        case 2: // Expense Only
                            reportAdapter.setFilterType(ReportAdapter.FilterType.EXPENSE_ONLY);
                            break;
                    }
                    reportAdapter.applyFilter();
                });
        builder.create().show();
    }

    private void sortReports() {
        // Example sort: Sort by amount
        String[] sortOptions = {"Amount (Low to High)", "Amount (High to Low)", "Date (Newest First)", "Date (Oldest First)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort Reports")
                .setItems(sortOptions, (dialog, which) -> {
                    switch (which) {
                        case 0: // Amount (Low to High)
                            reportAdapter.sortByAmountAscending();
                            break;
                        case 1: // Amount (High to Low)
                            reportAdapter.sortByAmountDescending();
                            break;
                        case 2: // Date (Newest First)
                            reportAdapter.sortByDateNewestFirst();
                            break;
                        case 3: // Date (Oldest First)
                            reportAdapter.sortByDateOldestFirst();
                            break;
                    }
                });
        builder.create().show();
    }
}
