package com.example.asm2_applicationdevelopment.DatabaseSQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asm2_applicationdevelopment.Model.Budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";

    public BudgetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    public long addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, budget.getCategory());
        values.put(COLUMN_AMOUNT, budget.getAmount());

        return db.insert(TABLE_BUDGET, null, values);
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET,
                new String[]{COLUMN_ID, COLUMN_CATEGORY, COLUMN_AMOUNT},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));

                Budget budget = new Budget((int) id, category, amount);
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return budgetList;
    }

    public Budget getBudget(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET,
                new String[]{COLUMN_ID, COLUMN_CATEGORY, COLUMN_AMOUNT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
            cursor.close();
            return new Budget((int) id, category, amount);
        }
        return null;
    }
}
