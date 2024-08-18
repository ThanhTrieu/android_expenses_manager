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
import com.example.asm2_applicationdevelopment.Model.Budget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditBudgetActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextStartDate, editTextEndDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel, buttonDelete;
    private BudgetDatabase budgetDatabase;
    private int budgetId;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        // Initialize the views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.btnSaveBudget);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonDelete = findViewById(R.id.btnDelete);

        // Initialize the database
        budgetDatabase = new BudgetDatabase(this);

        // Initialize calendar for date picking
        calendar = Calendar.getInstance();

        // Populate the Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set up date pickers
        setUpDatePicker(editTextStartDate);
        setUpDatePicker(editTextEndDate);

        // Get the budget ID from the intent
        budgetId = getIntent().getIntExtra("BUDGET_ID", -1);

        // Load budget data if editing
        if (budgetId != -1) {
            loadBudgetData();
        }

        // Set click listeners for buttons
        buttonSave.setOnClickListener(v -> saveBudget());
        buttonCancel.setOnClickListener(v -> finish());
        buttonDelete.setOnClickListener(v -> confirmDeleteBudget());
    }

    private void setUpDatePicker(final EditText editText) {
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(editText);
            }
        };

        editText.setOnClickListener(v -> new DatePickerDialog(EditBudgetActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void updateDateLabel(EditText editText) {
        String format = "yyyy-MM-dd"; // Change this format according to your needs
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void loadBudgetData() {
        // Load the budget data based on the budgetId
        Budget budget = budgetDatabase.getBudgetById(budgetId);

        if (budget != null) {
            editTextDescription.setText(budget.getDescription());
            editTextStartDate.setText(budget.getStartDate());
            editTextEndDate.setText(budget.getEndDate());
            editTextAmount.setText(String.valueOf(budget.getAmount()));

            // Set the correct category in the spinner
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
            int spinnerPosition = adapter.getPosition(budget.getCategory());
            if (spinnerPosition >= 0) {
                spinnerCategory.setSelection(spinnerPosition);
            }
        } else {
            Toast.makeText(this, "Budget not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBudget() {
        String description = editTextDescription.getText().toString().trim();
        String startDate = editTextStartDate.getText().toString().trim();
        String endDate = editTextEndDate.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (description.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        Budget budget = new Budget(budgetId, description, category, amount, startDate, endDate);

        if (budgetId == -1) {
            // Add new budget
            long result = budgetDatabase.addBudget(budget);
            if (result > 0) {
                Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add budget", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update existing budget
            int result = budgetDatabase.updateBudget(budget);
            if (result > 0) {
                Toast.makeText(this, "Budget updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update budget", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmDeleteBudget() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Budget")
                .setMessage("Do you want to delete this budget?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBudget())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteBudget() {
        if (budgetId != -1) {
            // Perform the delete operation
            int result = budgetDatabase.deleteBudget(budgetId);

            if (result > 0) {
                Toast.makeText(this, "Budget deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete budget", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No budget to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
