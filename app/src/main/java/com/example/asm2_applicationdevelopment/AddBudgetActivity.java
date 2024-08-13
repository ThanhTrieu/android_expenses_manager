package com.example.asm2_applicationdevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.BudgetDatabase;
import com.example.asm2_applicationdevelopment.Model.Budget;

public class AddBudgetActivity extends AppCompatActivity {

    private EditText etCategory;
    private EditText etAmount;
    private Button btnSaveBudget;
    private Button btnCancelBudget;
    private BudgetDatabase budgetDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        // Initialize UI components
        etCategory = findViewById(R.id.etCategory);
        etAmount = findViewById(R.id.etAmount);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnCancelBudget = findViewById(R.id.btnCancelBudget);

        // Initialize database
        budgetDatabase = new BudgetDatabase(this);

        // Set click listeners for buttons
        btnSaveBudget.setOnClickListener(v -> saveBudget());
        btnCancelBudget.setOnClickListener(v -> finish()); // Close the activity
    }

    private void saveBudget() {
        String category = etCategory.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (category.isEmpty()) {
            etCategory.setError("Category is required");
            return;
        }

        if (amountStr.isEmpty()) {
            etAmount.setError("Amount is required");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            etAmount.setError("Invalid amount");
            return;
        }

        // Create a new Budget object
        Budget budget = new Budget(category, amount);

        // Insert the budget into the database
        long result = budgetDatabase.addBudget(budget);

        if (result > 0) {
            Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to the previous screen
        } else {
            Toast.makeText(this, "Failed to add budget", Toast.LENGTH_SHORT).show();
        }
    }
}
