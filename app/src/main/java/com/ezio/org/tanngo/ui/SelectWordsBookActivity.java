package com.ezio.org.tanngo.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsBooksTable;
import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.ezio.org.tanngo.utils.MyPreference;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;


public class SelectWordsBookActivity extends BaseActivity{

    Context mContext;

    MyPreference myPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        List<WordsBooksTable> wordsBooksList;
        ArrayList<String> bookNameList = new ArrayList<>();
        BmobQuery<WordsBooksTable> query = new BmobQuery<WordsBooksTable>();
        query.findObjects(getApplicationContext(),new FindListener<WordsBooksTable>() {
            @Override
            public void onSuccess(List<WordsBooksTable> wordsBooksTables) {
                wordsBooksList.addAll(wordsBooksTables);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectWordsBookActivity.this,
                android.R.layout.simple_list_item_1,bookNameList);
        ListView listView = (ListView)findViewById(R.id.books_list_view);
        listView.setAdapter(adapter);




        //TODO:由测试可知，处理数据库时间相当长，要在适当的时候用下progressbar，转圈圈，还有工作线程

        Button testButton = (Button) findViewById(R.id.make_testing_dict);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ezio", "onClick----->");
                mContext = v.getContext();

                //change words book name in SP to the table name of words book
                myPref = new MyPreference(mContext);
                myPref.setDictName(WordsContract.WordsEntry.TABLE_NAME);



                creatFooDict();

                myPref.getRemainingWordsNumTotal();
                Toast.makeText(mContext, "db created ,please back", Toast.LENGTH_SHORT).show();





            }
        });
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_words_book);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViews() {


    }

    @Override
    public void initListeners() {

    }

    public void creatFooDict() {

        //create db
        WordsDbHelper mDbHelp = new WordsDbHelper(mContext);
        SQLiteDatabase db = mDbHelp.getWritableDatabase();

        if (!mDbHelp.isTableExist(myPref.getDictName())){

            //create table
            mDbHelp.creatTableIfNotExist(myPref.getDictName());

            //insert data into table
            for (int i = 0; i < 50; i++) {
                ContentValues values = new ContentValues();

                values.put(WordsContract.WordsEntry.COLUMN_WORD, "Placehold Word " + i);
                values.put(WordsContract.WordsEntry.COLUMN_KANA, "Placehold Kana " + i);
                values.put(WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE, "Placehold sentence " + i);
                values.put(WordsContract.WordsEntry.COLUMN_DEFINITION, "Placehold definition " + i);

                long newRowId;
                newRowId = db.insert(myPref.getDictName(), null, values);

            }


        }


    }


}
