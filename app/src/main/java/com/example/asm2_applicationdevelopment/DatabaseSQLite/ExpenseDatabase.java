package com.example.asm2_applicationdevelopment.DatabaseSQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asm2_applicationdevelopment.Model.Expense;

public class ExpenseDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "expense_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "expenses";
    private static final String ID_COL = "id";
    private static final String DESCRIPTION_COL = "description";
    private static final String DATE_COL = "date";
    private static final String AMOUNT_COL = "amount";
    private static final String CATEGORY_COL = "category";

    public ExpenseDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESCRIPTION_COL + " TEXT, "
                + DATE_COL + " TEXT, "
                + AMOUNT_COL + " REAL, "
                + CATEGORY_COL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new expense
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COL, expense.getDescription());
        values.put(DATE_COL, expense.getDate());
        values.put(AMOUNT_COL, expense.getAmount());
        values.put(CATEGORY_COL, expense.getCategory());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result; // Returns -1 if insert failed
    }

    // Method to get an expense by ID
    public Expense getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{ID_COL, DESCRIPTION_COL, DATE_COL, AMOUNT_COL, CATEGORY_COL},
                ID_COL + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {  // Check if the cursor is not empty and move to the first record
            @SuppressLint("Range") Expense expense = new Expense(
                    cursor.getInt(cursor.getColumnIndex(ID_COL)),
                    cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
                    cursor.getString(cursor.getColumnIndex(DATE_COL)),
                    cursor.getDouble(cursor.getColumnIndex(AMOUNT_COL)),
                    cursor.getString(cursor.getColumnIndex(CATEGORY_COL))
            );
            cursor.close();
            db.close();
            return expense;
        } else {
            // Handle the case where no data is found
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return null; // Or throw an exception, or return a default object, depending on your use case
        }
    }
    // Add this method to your ExpenseDatabase class
    @SuppressLint("Range")
    public Expense getLatestExpense() {
        Expense expense = null;
        SQLiteDatabase db = this.getReadableDatabase();
        // Query to get the most recently added expense based on the ID
        Cursor cursor = db.query("expenses", null, null, null, null, null, "id DESC", "1");

        if (cursor != null && cursor.moveToFirst()) {
            expense = new Expense();
            expense.setId(cursor.getInt(cursor.getColumnIndex("id")));
            expense.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            expense.setDate(cursor.getString(cursor.getColumnIndex("date")));
            expense.setAmount(cursor.getDouble(cursor.getColumnIndex("amount")));
            expense.setCategory(cursor.getString(cursor.getColumnIndex("category")));
            cursor.close();
        }

        db.close();
        return expense;
    }


    // Method to update an expense
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COL, expense.getDescription());
        values.put(DATE_COL, expense.getDate());
        values.put(AMOUNT_COL, expense.getAmount());
        values.put(CATEGORY_COL, expense.getCategory());
        int result = db.update(TABLE_NAME, values, ID_COL + "=?", new String[]{String.valueOf(expense.getId())});
        db.close();
        return result;
    }


    // Method to delete an expense
    public int deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

}
