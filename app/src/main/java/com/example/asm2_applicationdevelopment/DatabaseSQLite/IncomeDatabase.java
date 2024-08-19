package com.example.asm2_applicationdevelopment.DatabaseSQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asm2_applicationdevelopment.Model.Income;

import java.util.ArrayList;
import java.util.List;

public class IncomeDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "income_db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "incomes";
    private static final String ID_COL = "id";
    private static final String DESCRIPTION_COL = "description";
    private static final String DATE_COL = "date";
    private static final String AMOUNT_COL = "amount";
    private static final String CATEGORY_COL = "category";

    public IncomeDatabase(Context context) {
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

    // Method to add a new income
    public long addIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COL, income.getDescription());
        values.put(DATE_COL, income.getDate());
        values.put(AMOUNT_COL, income.getAmount());
        values.put(CATEGORY_COL, income.getCategory());
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result; // Returns -1 if insert failed
    }

    // Method to get an income by ID
    public Income getIncome(int id) {
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

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Income income = new Income(
                    cursor.getInt(cursor.getColumnIndex(ID_COL)),
                    cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
                    cursor.getString(cursor.getColumnIndex(DATE_COL)),
                    cursor.getDouble(cursor.getColumnIndex(AMOUNT_COL)),
                    cursor.getString(cursor.getColumnIndex(CATEGORY_COL))
            );
            cursor.close();
            db.close();
            return income;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            return null; // Or handle accordingly
        }
    }

    // Method to get the latest income
    @SuppressLint("Range")
    public Income getLatestIncome() {
        Income income = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ID_COL + " DESC", "1");

        if (cursor != null && cursor.moveToFirst()) {
            income = new Income(
                    cursor.getInt(cursor.getColumnIndex(ID_COL)),
                    cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
                    cursor.getString(cursor.getColumnIndex(DATE_COL)),
                    cursor.getDouble(cursor.getColumnIndex(AMOUNT_COL)),
                    cursor.getString(cursor.getColumnIndex(CATEGORY_COL))
            );
            cursor.close();
        }

        db.close();
        return income;
    }
    // Method to check if an income with the same category already exists
    public boolean isIncomeDuplicate(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{ID_COL},
                CATEGORY_COL + "=?",
                new String[]{category},
                null,
                null,
                null
        );

        boolean isDuplicate = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isDuplicate;
    }
    // Method to check if the category is already used by other income entries
    public boolean isCategoryDuplicate(String category, int excludeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{ID_COL},
                CATEGORY_COL + "=? AND " + ID_COL + "!=?",
                new String[]{category, String.valueOf(excludeId)},
                null,
                null,
                null
        );

        boolean isDuplicate = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isDuplicate;
    }


    // Method to get all incomes
    public List<Income> getAllIncomes() {
        List<Income> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ID_COL + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") Income income = new Income(
                        cursor.getInt(cursor.getColumnIndex(ID_COL)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION_COL)),
                        cursor.getString(cursor.getColumnIndex(DATE_COL)),
                        cursor.getDouble(cursor.getColumnIndex(AMOUNT_COL)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY_COL))
                );
                incomeList.add(income);
            }
            cursor.close();
        }

        db.close();
        return incomeList;
    }

    // Method to update an income
    public int updateIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COL, income.getDescription());
        values.put(DATE_COL, income.getDate());
        values.put(AMOUNT_COL, income.getAmount());
        values.put(CATEGORY_COL, income.getCategory());
        int result = db.update(TABLE_NAME, values, ID_COL + "=?", new String[]{String.valueOf(income.getId())});
        db.close();
        return result;
    }

    // Method to delete an income
    public int deleteIncome(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
