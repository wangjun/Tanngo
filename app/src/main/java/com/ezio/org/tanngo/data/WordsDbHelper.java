package com.ezio.org.tanngo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ezio.org.tanngo.data.WordsContract.WordsEntry;
import com.ezio.org.tanngo.utils.MyPreference;
import com.ezio.org.tanngo.utils.Utility;

/**
 * Created by Ezio on 2015/4/2.
 */
public class WordsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "words.db";

    private MyPreference myPref;
    private Context mContext = null;

    private SQLiteDatabase db = null;

    public WordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myPref = new MyPreference(context);
        mContext = context;

        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * use table name args build a SQL query to create words table
     * @param tableName the name of words book, be used as table name and stored in sharedPreference
     * @return SQL query string
     * @author
     * */
    public String getCreateWordsTableString(String tableName){
          final String SQL_CREATE_WORDS_TABLE = "CREATE TABLE " + tableName + "(" +
                WordsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WordsEntry.COLUMN_WORD + " TEXT UNIQUE NOT NULL," +
                WordsEntry.COLUMN_KANA + " TEXT DEFAULT ' '," +
                WordsEntry.COLUMN_DEFINITION + " TEXT NOT NULL," +
                WordsEntry.COLUMN_EXAMPLE_SENTENCE + " TEXT DEFAULT ' '," +
                WordsEntry.COLUMN_CONTINUOUS_RIGHT + " INTEGER DEFAULT 0," +
                WordsEntry.COLUMN_WRONG_TIMES + " INTEGER DEFAULT 0" +
                ");";

        return SQL_CREATE_WORDS_TABLE;
    }

    /**
     * check if table exist
     * @param tableName words book name OR table name
     * @return boolean
     * @author
     * */
    public boolean isTableExist(String tableName){
        final String CHECK_IS_TABLE_EXIST = "SELECT count(*) FROM sqlite_master WHERE type='table'" +
                " AND name='"+tableName+"';";

        try {

            if(tableName!=null){
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.rawQuery(CHECK_IS_TABLE_EXIST,null);
                cursor.moveToFirst();

                if (cursor.getInt(0)==0){
                    //no same name's table exist
                    Utility.ShowDebugLog("cursor第0个是",cursor.getInt(0)+"");
                    cursor.close();
                    return false;


                }else {

                    Utility.ShowDebugLog("cursor行数",cursor.getCount()+"");
                    Utility.ShowDebugLog("cursor第0个是",cursor.getInt(0)+"");
                    cursor.close();
                    return true;

                }

            }else {
                throw new NullPointerException("empty table name");
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
            Utility.ShowToast(e.getMessage(), mContext);
        }


        return false;
    }

    /**
     * creat table if no longer exist, and set table name as dict name in SharedPreference
     * @param tableName words book name OR table name
     * @author
     * */
    public void creatTableIfNotExist(String tableName){

        if (!isTableExist(tableName)){
            db.execSQL(getCreateWordsTableString(tableName));
            myPref.setDictName(tableName);
        }



    }
}
