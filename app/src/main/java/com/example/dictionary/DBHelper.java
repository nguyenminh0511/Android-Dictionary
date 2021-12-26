package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private Context mContex;

    public static final String DATABASE_NAME = "dict_hh.db";
    public static final int DATABASE_VERSION = 1;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";

    private final String TBL_ENG_VN = "eng_vn";
    private final String TBL_VN_ENG = "vn_eng";
    private final String TBL_BOOKMARK = "bookmark";

    private final String COL_KEY = "word";
    private final String COL_VALUE = "html";

    public SQLiteDatabase myDB;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContex = context;

        DATABASE_LOCATION =  "data/data/" + mContex.getPackageName() + "/database/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;

        if (!isExistingDB()) {
            try {
                File dbLocation = new File(DATABASE_LOCATION);
                dbLocation.mkdir();

                extractAssetToDatabaseDirection(DATABASE_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }

    //check if db exists in device
    boolean isExistingDB() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }

    // copy db from code to device
    public void extractAssetToDatabaseDirection(String filename) throws IOException {
        int length;
        InputStream sourceDatabase = this.mContex.getAssets().open(filename);
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while ((length = sourceDatabase.read(buffer)) > 0) {
            destination.write(buffer, 0, length);
        }

        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getTableName(int dic_typ) {
        String tableName = "";

        if (dic_typ == R.id.action_eng_vn) {
            tableName = TBL_ENG_VN;
        } else if (dic_typ == R.id.action_vn_eng) {
            tableName = TBL_VN_ENG;
        }
        return tableName;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWord(int dicTyp) {
        String tableName = getTableName(dicTyp);
        String q = "SELECT [word], [html] FROM " + tableName;

        Cursor result = myDB.rawQuery(q, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString(result.getColumnIndex(COL_KEY)));
        }

        return source;
    }

    @SuppressLint("Range")
    public Word getWord(String key, int dicType) {
        String tableName = getTableName(dicType);
        String q = "SELECT [word], [html] FROM " + tableName +
                " WHERE upper([word]) = upper(?);";

        Cursor result = myDB.rawQuery(q, new String[]{key});

        Word word = new Word();
        while (result.moveToNext()) {
            word.key =  result.getString(result.getColumnIndex(COL_KEY));
            word.value =  result.getString(result.getColumnIndex(COL_VALUE));
        }

        return word;
    }

    public void addBookmark(Word word) {
        try {
            String q = "INSERT INTO bookmark([" + COL_KEY + "], [" + COL_VALUE +"]) VALUES (?, ?);";
            myDB.execSQL(q, new Object[] {word.key, word.value});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBookmark(Word word) {
        try {
            String q = "DELETE FROM bookmark WHERE upper(["+ COL_KEY +"]) = upper(?) AND ["
                    + COL_VALUE + "] = ?;";
            myDB.execSQL(q, new Object[] {word.key, word.value});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllWordFromBookmark(String key) {
        String q = "SELECT  * FROM bookmark ORDER BY [date] DESC;";

        Cursor result = myDB.rawQuery(q, new String[]{key});

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString(result.getColumnIndex(COL_KEY)));
        }

        return source;
    }

    public boolean isWordMark(Word word) {
        String q = "SELECT * FROM bookmark WHERE upper([word]) = upper(?) AND [description] = ?";
        Cursor result = myDB.rawQuery(q, new String[] {word.key, word.value});

        return result.getCount() > 0;
    }

    @SuppressLint("Range")
    public Word getWordFromBookmark(String key) {
        String q = "SELECT * FROM bookmark WHERE upper([word]) = upper(?)";
        Cursor result = myDB.rawQuery(q, new String[] {key});
        Word word = null;
        while (result.moveToNext()) {
            word = new Word();
            word.key = result.getString(result.getColumnIndex(COL_KEY));
            word.value = result.getString(result.getColumnIndex(COL_VALUE));
        }

        return word;
    }
}
