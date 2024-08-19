package com.example.asm2_applicationdevelopment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.BudgetDatabase;
import com.example.asm2_applicationdevelopment.DatabaseSQLite.ExpenseDatabase;
import com.example.asm2_applicationdevelopment.Model.Budget;
import com.example.asm2_applicationdevelopment.Model.Expense;

import java.util.Calendar;
import java.util.List;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel, buttonDelete;
    private ExpenseDatabase expenseDatabase;
    private BudgetDatabase budgetDatabase;
    private int expenseId;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        // Initialize views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Initialize databases
        expenseDatabase = new ExpenseDatabase(this);
        budgetDatabase = new BudgetDatabase(this);

        // Set up the category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Initialize calendar for date picker
        calendar = Calendar.getInstance();

        // Set date picker dialog
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Get the expense ID from the intent
        expenseId = getIntent().getIntExtra("EXPENSE_ID", -1);

        // Load expense data if editing
        if (expenseId != -1) {
            loadExpenseData(expenseId);
        }

        // Set click listeners for buttons
        buttonSave.setOnClickListener(v -> saveExpense());
        buttonCancel.setOnClickListener(v -> finish());
        buttonDelete.setOnClickListener(v -> confirmDeleteExpense());
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    updateDateField();
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDateField() {
        String myFormat = "yyyy-MM-dd"; // In which you need to put here
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, java.util.Locale.getDefault());
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void loadExpenseData(int id) {
        Expense expense = expenseDatabase.getExpense(id);

        if (expense != null) {
            editTextDescription.setText(expense.getDescription());
            editTextDate.setText(expense.getDate());
            editTextAmount.setText(String.valueOf(expense.getAmount()));

            // Set the correct category in the spinner
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
            int spinnerPosition = adapter.getPosition(expense.getCategory());
            if (spinnerPosition >= 0) {
                spinnerCategory.setSelection(spinnerPosition);
            }
        }
    }

    private void saveExpense() {
        // Get input data from user
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String amountString = editTextAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // Validate inputs
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(date) || TextUtils.isEmpty(amountString)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the selected category exists in the budget database
        List<Budget> budgets = budgetDatabase.getBudgetsByCategory(category);
        if (budgets.isEmpty()) {
            Toast.makeText(this, "This category does not exist in the budget. You cannot edit the expense with this category.", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if the expense amount exceeds the budget and if the date is within the budget period
        boolean isWithinBudget = false;

        // Convert the expense date to a Date object
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        java.util.Date expenseDate;
        try {
            expenseDate = dateFormat.parse(date);
        } catch (java.text.ParseException e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Budget budget : budgets) {
            // Convert the budget start and end dates to Date objects
            java.util.Date startDate, endDate;
            try {
                startDate = dateFormat.parse(budget.getStartDate());
                endDate = dateFormat.parse(budget.getEndDate());
            } catch (java.text.ParseException e) {
                continue; // Skip this budget if date parsing fails
            }

            // Check if the expense amount exceeds the budget amount
            if (amount > budget.getAmount()) {
                Toast.makeText(this, "Expense exceeds the budget for this category", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the expense date is within the budget's date range
            if (expenseDate != null && !expenseDate.before(startDate) && !expenseDate.after(endDate)) {
                isWithinBudget = true;
                break;
            }
        }

        if (!isWithinBudget) {
            Toast.makeText(this, "Expense date is not within the budget period for this category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an Expense object
        Expense expense = new Expense(expenseId, description, date, amount, category);

        if (expenseId == -1) {
            // New expense - Check if an expense with the same category already exists
            if (expenseDatabase.expenseCategoryExists(category)) {
                Toast.makeText(this, "Expense with this category already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add new expense
            long result = expenseDatabase.addExpense(expense);
            if (result != -1) {
                Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and go back
            } else {
                Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing expense - Check if the category exists except for the current expense
            if (expenseDatabase.expenseCategoryExistsExcept(category, expenseId)) {
                Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update existing expense
            int result = expenseDatabase.updateExpense(expense);
            if (result > 0) {
                Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update expense", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmDeleteExpense() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Do you want to delete this expense?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteExpense();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteExpense() {
        if (expenseId != -1) {
            // Perform the delete operation
            int result = expenseDatabase.deleteExpense(expenseId);

            if (result > 0) {
                Toast.makeText(this, "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete expense", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No expense to delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connections when activity is destroyed
        expenseDatabase.close();
        budgetDatabase.close();
    }
}
