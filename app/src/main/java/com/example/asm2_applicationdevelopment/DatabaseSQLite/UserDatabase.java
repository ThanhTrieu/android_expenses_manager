package com.example.asm2_applicationdevelopment.DatabaseSQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asm2_applicationdevelopment.Model.User;

public class UserDatabase extends SQLiteOpenHelper {
    public static final String DB_NAME = "it0602_asm";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String ID_COL = "id";
    public static final String USERNAME_COL = "username";
    public static final String PASSWORD_COL = "password";
    public static final String EMAIL_COL = "email";
    public static final String PHONE_COL = "phone";

    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tao bang du lieu
        String query = "CREATE TABLE " + TABLE_NAME + " ( "
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " VARCHAR(60), "
                + PASSWORD_COL + " VARCHAR(200), "
                + EMAIL_COL + " VARCHAR(100), "
                + PHONE_COL + " VARCHAR(30))";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public long addNewUser(String username, String password, String email, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        values.put(EMAIL_COL, email);
        values.put(PHONE_COL, phone);
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert; // tra ve la -1 : insert that bai
    }
    @SuppressLint("Range")
    public User getInfoUser(String username, String password){
        Cursor cursor = null;
        User user = new User();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            // lay cac du lieu tu cot nao
            String[] columns = {ID_COL, USERNAME_COL, EMAIL_COL, PHONE_COL};
            String condition = USERNAME_COL  + " = ? " + " AND " +PASSWORD_COL + " = ? "  ;
            String[] params = { username, password};
            cursor = db.query(
                    TABLE_NAME,
                    columns,
                    condition,
                    params,
                    null,
                    null,
                    null
            );
            // select id, username, email, phone, where username = ? AND password = ?;
            if (cursor.getCount() > 0){
                cursor.moveToFirst();// lay ra 1 dong du lieu
                // do du lieu vao model
                user.setId(cursor.getInt(cursor.getColumnIndex(ID_COL)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME_COL)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL_COL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE_COL)));
            }
            db.close();
        }
        finally {
            assert cursor != null;
            cursor.close();

        }

        return user;
    }
}
