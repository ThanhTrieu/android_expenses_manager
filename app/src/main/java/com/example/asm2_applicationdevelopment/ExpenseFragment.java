package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm2_applicationdevelopment.Adapter.ExpenseAdapter;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;

import java.util.List;

public class ExpenseFragment extends Fragment {

    private Button btnAddExpense;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ExpenseDatabase expenseDatabase;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    public static ExpenseFragment newInstance() {
        return new ExpenseFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expenseDatabase = new ExpenseDatabase(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.rvExpenses); // Ensure this ID matches your layout

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter(expenseDatabase.getAllExpenses(), this::onExpenseItemClick);
        recyclerView.setAdapter(expenseAdapter);

        LinearLayoutManager expenseLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(expenseLayoutManager);
        expenseAdapter = new ExpenseAdapter(expenseDatabase.getAllExpenses(), this::onExpenseItemClick);
        int itemOffset = getResources().getDimensionPixelSize(R.dimen.item_offset); // Ensure this dimension is defined in dimens.xml
        recyclerView.addItemDecoration(new ItemOffsetDecoration(itemOffset));

        // Set click listener for add button
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the RecyclerView to load the latest expenses
        expenseAdapter.updateExpenses(expenseDatabase.getAllExpenses());
    }

    private void navigateToAddExpense() {
        Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
        startActivity(intent);
    }

    private void onExpenseItemClick(Expense expense) {
        Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
        intent.putExtra("EXPENSE_ID", expense.getId());
        startActivity(intent);
    }
}
