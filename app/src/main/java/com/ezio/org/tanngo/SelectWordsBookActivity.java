package com.ezio.org.tanngo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;


public class SelectWordsBookActivity extends ActionBarActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words_book);

        //TODO:由测试可知，处理数据库时间相当长，要在适当的时候用下progressbar，转圈圈，还有工作线程

        Button testButton = (Button)findViewById(R.id.make_testing_dict);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ezio", "onClick----->");
                mContext = v.getContext();
                creatFooDict();
                Toast.makeText(mContext, "db created ,please back", Toast.LENGTH_SHORT).show();

                //change words book name in SP to the table name of words book
                MyPreference myPref = new MyPreference(mContext);
                myPref.setDictName(WordsContract.WordsEntry.TABLE_NAME);

                //test code
                MyPreference pref = new MyPreference(mContext);
                pref.getRemainingWordsNum();
            }
        });
    }
    public void creatFooDict(){

        WordsDbHelper mDbHelp = new WordsDbHelper(mContext);
        SQLiteDatabase db = mDbHelp.getWritableDatabase();

        for (int i =0;i<50;i++) {
            ContentValues values = new ContentValues();

            values.put(WordsContract.WordsEntry.COLUMN_WORD,"Placehold Word "+ i);
            values.put(WordsContract.WordsEntry.COLUMN_KANA,"Placehold Kana "+ i);
            values.put(WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE,"Placehold sentence "+ i);
            values.put(WordsContract.WordsEntry.COLUMN_DEFINITION,"Placehold definition "+ i);

            long newRowId;
            newRowId = db.insert(WordsContract.WordsEntry.TABLE_NAME,null,values);

        }
    }






}
