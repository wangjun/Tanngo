package com.ezio.org.tanngo.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.adapter.BaseAdapterHelper;
import com.ezio.org.tanngo.adapter.QuickAdapter;
import com.ezio.org.tanngo.data.WordsBooksTable;
import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.ezio.org.tanngo.utils.MyPreference;
import com.ezio.org.tanngo.utils.Utility;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class WordsBooksListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    Context mContext;

    MyPreference myPref;

    protected QuickAdapter<WordsBooksTable> BooksAdapter;

    ListView booksListView;

    TextView bookName;
    TextView bookWordsCount;

    public final static String BOOK_KEY="book_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_words_book);
    }

    @Override
    public void initData() {

        if (BooksAdapter == null){
            BooksAdapter = new QuickAdapter<WordsBooksTable>(this,R.layout.book_item) {
                @Override
                protected void convert(BaseAdapterHelper helper, WordsBooksTable item) {
                    helper.setText(R.id.book_name,item.getBookName());
                    helper.setText(R.id.book_words_count, item.getWordsCount() + "");


                }
            };
        }
        booksListView.setAdapter(BooksAdapter);
        //加载页面
        queryBooks();


    }

    @Override
    public void initViews() {

        booksListView = (ListView)findViewById(R.id.books_list_view);

        bookName = (TextView)findViewById(R.id.book_name);
        bookWordsCount = (TextView)findViewById(R.id.book_words_count);

    }

    @Override
    public void initListeners() {

        booksListView.setOnItemClickListener(this);
    }

    private void queryBooks(){
        booksListView.setVisibility(View.VISIBLE);
        BmobQuery<WordsBooksTable> query = new BmobQuery<WordsBooksTable>();
        query.order("-createdAt");
        query.findObjects(this,new FindListener<WordsBooksTable>() {
            @Override
            public void onSuccess(List<WordsBooksTable> wordsBooksTables) {
                BooksAdapter.clear();
                Utility.ShowDebugLog("top","top");
                if (wordsBooksTables==null||wordsBooksTables.size()==0){
                    //showErrorView
                    BooksAdapter.notifyDataSetChanged();
                    Utility.ShowDebugLog("in if ", "in if");
                    return;
                }

                for (int j = 0; j<wordsBooksTables.size();j++){
                    Utility.ShowDebugLog("success","success");
                    Utility.ShowDebugLog("name",wordsBooksTables.get(j).getBookName());
                    Utility.ShowDebugLog("describe",wordsBooksTables.get(j).getBookName());
                }
                //show progeress
                BooksAdapter.addAll(wordsBooksTables);
                booksListView.setAdapter(BooksAdapter);

                for (int j = 0; j<BooksAdapter.getCount();j++){
                    Utility.ShowDebugLog("adapter","adapter");
                    Utility.ShowDebugLog("adapter--name",BooksAdapter.getItem(j).getBookName());
                    Utility.ShowDebugLog("adapter--describe",BooksAdapter.getItem(j).getBookName());
                }

                Utility.ShowDebugLog("end","end");
            }

            @Override
            public void onError(int i, String s) {

                Utility.ShowDebugLog("Error","Error");
                //show error
            }
        });
    }

    public void creatFooDict() {

        //create db
        WordsDbHelper mDbHelp = new WordsDbHelper(mContext);
        SQLiteDatabase db = mDbHelp.getWritableDatabase();

        if (!mDbHelp.isTableExist(myPref.getDictName())){

            //create table


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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(this,WordsBookDetailActivity.class);
        intent.putExtra(BOOK_KEY,BooksAdapter.getItem(position).getObjectId());
        startActivity(intent);
    }
}
