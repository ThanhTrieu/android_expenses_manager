package com.example.asm2_applicationdevelopment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.Model.Expense;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel;
    private ExpenseDatabase expenseDatabase;
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // Initialize the views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Initialize the database
        expenseDatabase = new ExpenseDatabase(this);

        // Get the expense ID from the intent
        expenseId = getIntent().getIntExtra("expense_id", -1);

        // Load expense data if editing
        if (expenseId != -1) {
            loadExpenseData(expenseId);
        }

        // Set click listeners for buttons
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadExpenseData(int id) {
        Expense expense = expenseDatabase.getExpense(id);

        if (expense != null) {
            editTextDescription.setText(expense.getDescription());
            editTextDate.setText(expense.getDate());
            editTextAmount.setText(String.valueOf(expense.getAmount()));
            // Assuming you have set up the spinner adapter and data
            // spinnerCategory.setSelection(getIndex(spinnerCategory, expense.getCategory()));
        }
    }

    private void saveExpense() {
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (description.isEmpty() || date.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Expense expense = new Expense(expenseId, description, date, amount, category);

        // Update the existing expense
        int result = expenseDatabase.updateExpense(expense);

        if (result > 0) {
            Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update expense", Toast.LENGTH_SHORT).show();
        }
    }

    // Optional method to get spinner index based on value
    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }
}
