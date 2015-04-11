package com.ezio.org.tanngo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ezio.org.tanngo.data.WordsContract.WordsEntry;

/**
 * Created by Ezio on 2015/4/2.
 */
public class WordsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "words.db";

    public WordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + WordsEntry.TABLE_NAME);
        //为什么给word unique 就会报错呢？
        final String SQL_CREATE_WORDS_TABLE = "CREATE TABLE " + WordsEntry.TABLE_NAME + "(" +
                WordsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WordsEntry.COLUMN_WORD + " TEXT UNIQUE NOT NULL," +
                WordsEntry.COLUMN_KANA + " TEXT DEFAULT ' '," +
                WordsEntry.COLUMN_DEFINITION + " TEXT NOT NULL," +
                WordsEntry.COLUMN_EXAMPLE_SENTENCE + " TEXT DEFAULT ' '," +
                WordsEntry.COLUMN_CONTINUOUS_RIGHT + " INTEGER DEFAULT 0," +
                WordsEntry.COLUMN_WRONG_TIMES + " INTEGER DEFAULT 0" +
                ");";

        db.execSQL(SQL_CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + WordsEntry.TABLE_NAME);
        onCreate(db);
        //TODO: onUpgrade
    }
}
