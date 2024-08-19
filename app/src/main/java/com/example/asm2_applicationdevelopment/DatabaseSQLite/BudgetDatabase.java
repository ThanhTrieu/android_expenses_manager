package com.example.asm2_applicationdevelopment.DatabaseSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asm2_applicationdevelopment.Model.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_manager.db";
    private static final int DATABASE_VERSION = 3; // Incremented version number

    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description"; // New Description field
    private static final String COLUMN_START_DATE = "start_date"; // New Start Date field
    private static final String COLUMN_END_DATE = "end_date"; // New End Date field

    public BudgetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBudgetTable = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_DESCRIPTION + " TEXT, " + // Added Description field
                COLUMN_START_DATE + " TEXT, " + // Added Start Date field
                COLUMN_END_DATE + " TEXT)"; // Added End Date field
        db.execSQL(createBudgetTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // Upgrading from version 1 to 2
            db.execSQL("ALTER TABLE " + TABLE_BUDGET + " ADD COLUMN " + COLUMN_DESCRIPTION + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_BUDGET + " ADD COLUMN " + COLUMN_START_DATE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_BUDGET + " ADD COLUMN " + COLUMN_END_DATE + " TEXT");
        }
    }

    public long addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, budget.getCategory());
        values.put(COLUMN_AMOUNT, budget.getAmount());
        values.put(COLUMN_DESCRIPTION, budget.getDescription()); // Insert Description
        values.put(COLUMN_START_DATE, budget.getStartDate()); // Insert Start Date
        values.put(COLUMN_END_DATE, budget.getEndDate()); // Insert End Date
        long id = db.insert(TABLE_BUDGET, null, values);
        db.close();
        return id;
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, null, null, null, null, COLUMN_ID + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Budget budget = new Budget(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)), // Retrieve Start Date
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE)) // Retrieve End Date
                );
                budgets.add(budget);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return budgets;
    }

    public Budget getBudgetById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Budget budget = new Budget(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE))
            );
            cursor.close();
            db.close();
            return budget;
        }

        db.close();
        return null;
    }

    public List<Budget> getBudgetsByCategory(String category) {
        List<Budget> budgets = new ArrayList<>();

        if (category == null) {
            // Handle the null category case
            return budgets; // or you could throw an exception if that makes sense for your app
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, null, COLUMN_CATEGORY + " = ?", new String[]{category}, null, null, COLUMN_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Budget budget = new Budget(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE))
                );
                budgets.add(budget);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return budgets;
    }

    public int updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, budget.getCategory());
        values.put(COLUMN_AMOUNT, budget.getAmount());
        values.put(COLUMN_DESCRIPTION, budget.getDescription()); // Update Description
        values.put(COLUMN_START_DATE, budget.getStartDate()); // Update Start Date
        values.put(COLUMN_END_DATE, budget.getEndDate()); // Update End Date
        int result = db.update(TABLE_BUDGET, values, COLUMN_ID + "=?", new String[]{String.valueOf(budget.getId())});
        db.close();
        return result;
    }

    public boolean budgetExists(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_BUDGET + " WHERE " + COLUMN_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }


    public int deleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BUDGET, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
