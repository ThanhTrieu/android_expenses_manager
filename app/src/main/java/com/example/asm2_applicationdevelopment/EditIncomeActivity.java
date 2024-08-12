package com.example.asm2_applicationdevelopment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Income;

public class EditIncomeActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel, buttonDelete;
    private IncomeDatabase incomeDatabase;
    private int incomeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        // Initialize the views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonDelete = findViewById(R.id.buttonDelete); // Ensure this ID matches your layout

        // Initialize the database
        incomeDatabase = new IncomeDatabase(this);

        // Populate the Spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Get the income ID from the intent
        incomeId = getIntent().getIntExtra("INCOME_ID", -1); // Use the correct intent key "INCOME_ID"

        // Load income data if editing
        if (incomeId != -1) {
            loadIncomeData(incomeId);
        }

        // Set click listeners for buttons
        buttonSave.setOnClickListener(v -> saveIncome());
        buttonCancel.setOnClickListener(v -> finish());
        buttonDelete.setOnClickListener(v -> confirmDeleteIncome()); // Handle delete with confirmation
    }

    private void loadIncomeData(int id) {
        Income income = incomeDatabase.getIncome(id);

        if (income != null) {
            editTextDescription.setText(income.getDescription());
            editTextDate.setText(income.getDate());
            editTextAmount.setText(String.valueOf(income.getAmount()));

            // Set the correct category in the spinner
            String category = income.getCategory();
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
            int spinnerPosition = adapter.getPosition(category);
            if (spinnerPosition >= 0) {
                spinnerCategory.setSelection(spinnerPosition);
            }
        }
    }

    private void saveIncome() {
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (description.isEmpty() || date.isEmpty() || amountStr.isEmpty() || category.isEmpty()) {
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

        Income income = new Income(incomeId, description, date, amount, category);

        // Update the existing income
        int result = incomeDatabase.updateIncome(income);

        if (result > 0) {
            Toast.makeText(this, "Income updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update income", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDeleteIncome() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Income")
                .setMessage("Do you want to delete this income?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteIncome();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteIncome() {
        if (incomeId != -1) {
            // Perform the delete operation
            int result = incomeDatabase.deleteIncome(incomeId);

            if (result > 0) {
                Toast.makeText(this, "Income deleted successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deletion
            } else {
                Toast.makeText(this, "Failed to delete income", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No income to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
