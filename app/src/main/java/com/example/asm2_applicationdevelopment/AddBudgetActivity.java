package com.example.asm2_applicationdevelopment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asm2_applicationdevelopment.DatabaseSQLite.BudgetDatabase;
import com.example.asm2_applicationdevelopment.Model.Budget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBudgetActivity extends AppCompatActivity {

    private EditText etDescription, etStartDate, etEndDate, etAmount;
    private Spinner spinnerCategory;
    private Button btnSaveBudget, btnCancel;
    private BudgetDatabase budgetDatabase;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        // Initialize the views
        etDescription = findViewById(R.id.editTextDescription);
        etStartDate = findViewById(R.id.editTextStartDate);
        etEndDate = findViewById(R.id.editTextEndDate);
        etAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveBudget = findViewById(R.id.btnSaveBudget);
        btnCancel = findViewById(R.id.buttonCancel);

        budgetDatabase = new BudgetDatabase(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Initialize calendar for date picking
        calendar = Calendar.getInstance();

        // Set up date pickers
        setUpDatePicker(etStartDate);
        setUpDatePicker(etEndDate);

        // Set the save button click listener
        btnSaveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });

        // Set the cancel button click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the activity if cancel is clicked
            }
        });
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

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddBudgetActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateLabel(EditText editText) {
        String format = "yyyy-MM-dd"; // Change this format according to your needs
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    private void saveBudget() {
        // Get values from the input fields
        String description = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that the budget's start and end dates are within the same month
        // Implement your date validation logic here

        // Create Budget object and save to database
        Budget budget = new Budget(0, description, category, amount, startDate, endDate);
        budgetDatabase.addBudget(budget);

        Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after saving
    }
}
