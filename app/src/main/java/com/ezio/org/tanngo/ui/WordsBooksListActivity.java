package com.ezio.org.tanngo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.adapter.BaseAdapterHelper;
import com.ezio.org.tanngo.adapter.QuickAdapter;
import com.ezio.org.tanngo.data.WordsBook;
import com.ezio.org.tanngo.utils.Utility;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class WordsBooksListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    

    protected QuickAdapter<WordsBook> BooksAdapter;

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
            BooksAdapter = new QuickAdapter<WordsBook>(this,R.layout.book_item) {
                @Override
                protected void convert(BaseAdapterHelper helper, WordsBook item) {
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
        BmobQuery<WordsBook> query = new BmobQuery<WordsBook>();
        query.order("-createdAt");
        query.findObjects(this,new FindListener<WordsBook>() {
            @Override
            public void onSuccess(List<WordsBook> wordsBooks) {
                BooksAdapter.clear();
                Utility.ShowDebugLog("top","top");
                if (wordsBooks ==null|| wordsBooks.size()==0){
                    //showErrorView
                    BooksAdapter.notifyDataSetChanged();
                    Utility.ShowDebugLog("in if ", "in if");
                    return;
                }

                for (int j = 0; j< wordsBooks.size();j++){
                    Utility.ShowDebugLog("success","success");
                    Utility.ShowDebugLog("name", wordsBooks.get(j).getBookName());
                    Utility.ShowDebugLog("describe", wordsBooks.get(j).getBookName());
                }
                //show progeress
                BooksAdapter.addAll(wordsBooks);
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




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(this,WordsBookDetailActivity.class);
        intent.putExtra(BOOK_KEY,BooksAdapter.getItem(position).getObjectId());
        startActivity(intent);
    }
}
