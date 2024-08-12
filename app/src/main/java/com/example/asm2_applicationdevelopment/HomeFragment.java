package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.ExpenseAdapter;
import com.example.asm2_applicationdevelopment.Adapter.IncomeAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;
import com.example.asm2_applicationdevelopment.Model.Income;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewIncome;
    private RecyclerView recyclerViewExpense;
    private IncomeAdapter incomeAdapter;
    private ExpenseAdapter expenseAdapter;
    private IncomeDatabase incomeDatabase;
    private ExpenseDatabase expenseDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        recyclerViewIncome = view.findViewById(R.id.rvIncomes);
        recyclerViewExpense = view.findViewById(R.id.rvExpenses);

        // Set up RecyclerViews
        recyclerViewIncome.setLayoutManager(new LinearLayoutManager(getContext()));
        incomeAdapter = new IncomeAdapter(incomeDatabase.getAllIncomes(), this::onIncomeItemClick);
        recyclerViewIncome.setAdapter(incomeAdapter);

        recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter(expenseDatabase.getAllExpenses(), this::onExpenseItemClick);
        recyclerViewExpense.setAdapter(expenseAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerViews to load the latest data
        incomeAdapter.updateIncomes(incomeDatabase.getAllIncomes());
        expenseAdapter.updateExpenses(expenseDatabase.getAllExpenses());
    }

    private void onIncomeItemClick(Income income) {
        Intent intent = new Intent(getActivity(), EditIncomeActivity.class);
        intent.putExtra("INCOME_ID", income.getId());
        startActivity(intent);
    }

    private void onExpenseItemClick(Expense expense) {
        Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
        intent.putExtra("EXPENSE_ID", expense.getId());
        startActivity(intent);
    }
}
