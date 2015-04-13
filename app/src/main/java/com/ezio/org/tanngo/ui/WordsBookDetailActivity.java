package com.ezio.org.tanngo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsBooksTable;
import com.ezio.org.tanngo.service.FetchBookAndInsertTask;
import com.ezio.org.tanngo.utils.Utility;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;

public class WordsBookDetailActivity extends BaseActivity {


    private TextView mBookName;
    private TextView mWordsCount;
    private TextView mLevel;
    private TextView mAuthor;
    private TextView mPrice;
    private TextView mDescribe;

    private Button mDownLoadBtn;

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
                public void onSuccess(final WordsBooksTable wordsBooksTable) {
                    mBookName.setText(wordsBooksTable.getBookName());
                    mWordsCount.setText(wordsBooksTable.getWordsCount()+"");
                    mLevel.setText(wordsBooksTable.getLevel());
                    mAuthor.setText(wordsBooksTable.getAuthor());
                    mPrice.setText(wordsBooksTable.getPrice()+"");
                    mDescribe.setText(wordsBooksTable.getDescribe());

                    //下载按钮
                    mDownLoadBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (wordsBooksTable.getFile()!=null){
                                String fileName =wordsBooksTable.getFile().getFilename();
                                String fileUrl = wordsBooksTable.getFile().getFileUrl(getApplicationContext());

                                new FetchBookAndInsertTask(getApplicationContext())
                                        .execute(fileUrl,fileName);
                            }else {
                                Utility.ShowToast("服务器端没有文件",getApplicationContext());
                                Utility.ShowDebugLog("file",wordsBooksTable.getFile().toString());
                            }
                        }
                    });

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

        mDownLoadBtn = (Button)findViewById(R.id.detail_download_book);


    }

    @Override
    public void initListeners() {

    }


}
