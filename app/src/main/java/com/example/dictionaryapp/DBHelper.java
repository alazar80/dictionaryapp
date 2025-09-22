package com.example.dictionaryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "dictionary.db";
    private static String DB_PATH = "";
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        copyDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    private void copyDatabase() {
        try {
            java.io.File dbFile = new java.io.File(DB_PATH + DB_NAME);
            if (!dbFile.exists()) {
                this.getReadableDatabase();
                this.close();
                InputStream myInput = mContext.getAssets().open(DB_NAME);
                OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (IOException e) {
            throw new Error("Error copying database: " + e.getMessage());
        }
    }

    private SQLiteDatabase openDatabase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        return mDatabase;
    }

    public String searchWord(String word) {
        mDatabase = openDatabase();
        String result = "Not Found";
        Cursor cursor = mDatabase.rawQuery(
                "SELECT amharic, english FROM dictionary WHERE amharic=? OR english=?",
                new String[]{word, word});
        if (cursor.moveToFirst()) {
            String am = cursor.getString(0);
            String en = cursor.getString(1);
            result = am + " ↔ " + en;
        }
        cursor.close();
        mDatabase.close();
        return result;
    }
}
