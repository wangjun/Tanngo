package com.ezio.org.tanngo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.utils.Utility;


public class HomeActivity extends BaseActivity {



    private Button mStartBackWordsBTN;

    //be sure mWordsEachday's text same as mWordsToday, mWordsEachday don't use really each day data
    private TextView mWordsEachday;
    private TextView mRemainingDay;
    private TextView mAlreadyDoneWordsToday;
    private TextView mWordsToday;

    private TextView mTotalProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initViews() {



        mStartBackWordsBTN = (Button) findViewById(R.id.start_back_words_BTN);
        //textView
        mWordsEachday = (TextView) findViewById(R.id.words_eachday);
        mRemainingDay = (TextView) findViewById(R.id.remaining_day);
        mAlreadyDoneWordsToday = (TextView) findViewById(R.id.already_done_words_today);
        mWordsToday = (TextView) findViewById(R.id.words_today);
        mTotalProgress = (TextView)findViewById(R.id.total_progress_textview);

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {


    }

    @Override
    protected void onStart() {
        super.onStart();

        //if these is no dict , then make the "start back " button as "choose dict button"
        if (myPref.getDictName() == null || myPref.getTodayWordsNumTotal() == 0) {
            mStartBackWordsBTN.setText("选择单词书");
            mStartBackWordsBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ChangePlanActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            mStartBackWordsBTN.setText("开始背单词");
            mStartBackWordsBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, WordActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //update HomeActivity's view texts' information
        //doing it in onResume is for add some animation later
        mWordsEachday.setText(myPref.getTodayWordsNumTotal() + "");
        mRemainingDay.setText(myPref.getRemainingDay() + "");



        int alreadyDoneWordsTotal=(myPref.getWordsNumTotal() - myPref.getRemainingWordsNumTotal());
        int wordsTotal=myPref.getWordsNumTotal();
        mTotalProgress.setText(
                Utility.formatTotalProgress(getApplicationContext(),
                        alreadyDoneWordsTotal,
                        wordsTotal));


        //when land state , the string isn't on one row , so can't use formatted string temp
        mAlreadyDoneWordsToday.setText((myPref.getTodayWordsNumTotal() - myPref.getTodayWordsRemaining()) + "");
        mWordsToday.setText(myPref.getTodayWordsNumTotal() + "");



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.change_plan_menu_item){
            Intent intent = new Intent(HomeActivity.this, ChangePlanActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
