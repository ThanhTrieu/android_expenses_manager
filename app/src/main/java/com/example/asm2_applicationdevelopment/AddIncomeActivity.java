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

import com.example.asm2_applicationdevelopment.DatabaseSQLite.IncomeDatabase;
import com.example.asm2_applicationdevelopment.Model.Income;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText editTextDescription, editTextDate, editTextAmount;
    private Spinner spinnerCategory;
    private Button buttonSave, buttonCancel;
    private IncomeDatabase incomeDatabase;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        // Initialize views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Initialize database
        incomeDatabase = new IncomeDatabase(this);

        // Initialize calendar for date picker
        calendar = Calendar.getInstance();

        // Set up the category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.income_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set date picker dialog
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Set save button click listener
        buttonSave.setOnClickListener(view -> saveIncome());

        // Set cancel button click listener
        buttonCancel.setOnClickListener(view -> finish());
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
        String myFormat = "yyyy-MM-dd"; // Adjust date format if needed
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editTextDate.setText(sdf.format(calendar.getTime()));
    }

    private void saveIncome() {
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

        // Create an Income object
        Income income = new Income();
        income.setDescription(description);
        income.setDate(date);
        income.setAmount(amount);
        income.setCategory(category);

        // Save to database
        long result = incomeDatabase.addIncome(income);
        if (result != -1) {
            Toast.makeText(this, "Income added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and go back
        } else {
            Toast.makeText(this, "Failed to add income", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection when activity is destroyed
        incomeDatabase.close();
    }
}
