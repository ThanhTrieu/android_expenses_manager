package com.example.asm2_applicationdevelopment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;

public class ExpenseFragment extends Fragment {

    private Button btnAddExpense;
    private Button btnEditExpense;
    private Button btnDeleteExpense;
    private TextView textViewExpenseDetails;
    private ExpenseDatabase expenseDatabase;
    private int currentExpenseId = -1; // Default to -1 if no expense is selected

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
        btnAddExpense = view.findViewById(R.id.btnAdd);
        btnEditExpense = view.findViewById(R.id.btnEdit);
        btnDeleteExpense = view.findViewById(R.id.btnDelete);
        textViewExpenseDetails = view.findViewById(R.id.tvExpenseDetails);

        // Set click listeners for buttons
        btnAddExpense.setOnClickListener(v -> navigateToAddExpense());
        btnEditExpense.setOnClickListener(v -> navigateToEditExpense());
        btnDeleteExpense.setOnClickListener(v -> deleteExpense());

        // For demonstration purposes, hardcoding an expense ID to show details
        // Replace this with actual logic to get the selected expense ID
        currentExpenseId = 1; // Replace with dynamic value if necessary
        showExpenseDetails(currentExpenseId);

        return view;
    }

    private void navigateToAddExpense() {
        Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
        startActivity(intent);
    }

    private void navigateToEditExpense() {
        if (currentExpenseId != -1) {
            Intent intent = new Intent(getActivity(), EditExpenseActivity.class);
            intent.putExtra("EXPENSE_ID", currentExpenseId);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "No expense selected for editing", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteExpense() {
        if (currentExpenseId != -1) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        int result = expenseDatabase.deleteExpense(currentExpenseId);
                        if (result > 0) {
                            Toast.makeText(getActivity(), "Expense deleted", Toast.LENGTH_SHORT).show();
                            // Optionally, update UI or navigate back
                        } else {
                            Toast.makeText(getActivity(), "Failed to delete expense", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else {
            Toast.makeText(getActivity(), "No expense selected for deletion", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExpenseDetails(int id) {
        Expense expense = expenseDatabase.getExpense(id);
        if (expense != null) {
            String details = "Description: " + expense.getDescription() + "\n"
                    + "Date: " + expense.getDate() + "\n"
                    + "Amount: " + expense.getAmount() + "\n"
                    + "Category: " + expense.getCategory();
            textViewExpenseDetails.setText(details);
        } else {
            textViewExpenseDetails.setText("No details available.");
        }
    }
}
