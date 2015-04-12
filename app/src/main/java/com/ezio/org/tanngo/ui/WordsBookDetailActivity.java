package com.ezio.org.tanngo.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsBooksTable;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

public class WordsBookDetailActivity extends BaseActivity {


    TextView mBookName;
    TextView mWordsCount;
    TextView mLevel;
    TextView mAuthor;
    TextView mPrice;
    TextView mDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_words_book_detail);
    }

    @Override
    public void initData() {

        String itemId = getIntent().getStringExtra(WordsBooksListActivity.BOOK_KEY);
        if (itemId!=null){
            BmobQuery<WordsBooksTable> query = new BmobQuery<WordsBooksTable>();
            query.getObject(this,itemId,new GetListener<WordsBooksTable>() {
                @Override
                public void onSuccess(WordsBooksTable wordsBooksTable) {
                    mBookName.setText(wordsBooksTable.getBookName());
                    mWordsCount.setText(wordsBooksTable.getWordsCount()+"");
                    mLevel.setText(wordsBooksTable.getLevel());
                    mAuthor.setText(wordsBooksTable.getAuthor());
                    mPrice.setText(wordsBooksTable.getPrice()+"");
                    mDescribe.setText(wordsBooksTable.getDescribe());
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }

    }

    @Override
    public void initViews() {

        mBookName = (TextView)findViewById(R.id.detail_book_name);
        mWordsCount= (TextView)findViewById(R.id.detail_words_count);
        mLevel= (TextView)findViewById(R.id.detail_level);
        mAuthor= (TextView)findViewById(R.id.detail_author);
        mPrice= (TextView)findViewById(R.id.detail_price);
        mDescribe= (TextView)findViewById(R.id.detail_describe);


    }

    @Override
    public void initListeners() {

    }


}
