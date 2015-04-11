package com.ezio.org.tanngo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezio.org.tanngo.utils.MyPreference;
import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.utils.Utility;

import cn.bmob.v3.Bmob;


public class HomeActivity extends Activity {


    private final static String BMOB_APP_ID = "9118cc097159952e26caf5c9b535006e";

    //initial view
    //private Button mWordsListBTN;

    private Button mStartBackWordsBTN;

    //be sure mWordsEachday's text same as mWordsToday, mWordsEachday don't use really each day data
    private TextView mWordsEachday;
    private TextView mRemainingDay;
    private TextView mAlreadyDoneWordsTotal;
    private TextView mWordsTotal;
    private TextView mAlreadyDoneWordsToday;
    private TextView mWordsToday;

    MyPreference myPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();


        //test code
        String dictName = myPref.getDictName();
        if (dictName == null) {
            Log.d(Utility.LOG_TAG, "dictName is null");
        } else {
            Log.d(Utility.LOG_TAG, "I dont know dictName is what");
        }




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

        mWordsEachday.setText(myPref.getTodayWordsNumTotal() + "");
        mRemainingDay.setText(myPref.getRemainingDay() + "");


        mAlreadyDoneWordsTotal.setText((myPref.getWordsNumTotal() - myPref.getRemainingWordsNumTotal()) + "");
        mWordsTotal.setText(myPref.getWordsNumTotal() + "");


        mAlreadyDoneWordsToday.setText((myPref.getTodayWordsNumTotal() - myPref.getTodayWordsRemaining()) + "");
        mWordsToday.setText(myPref.getTodayWordsNumTotal() + "");


    }

    private void initialize() {

        //initial Bmob SDK
        Bmob.initialize(this, BMOB_APP_ID);


        //button
        //mWordsListBTN = (Button)findViewById(R.id.words_list_BTN);

        mStartBackWordsBTN = (Button) findViewById(R.id.start_back_words_BTN);

        //textView
        mWordsEachday = (TextView) findViewById(R.id.words_eachday);
        mRemainingDay = (TextView) findViewById(R.id.remaining_day);
        mAlreadyDoneWordsTotal = (TextView) findViewById(R.id.already_done_words_total);
        mWordsTotal = (TextView) findViewById(R.id.words_total);
        mAlreadyDoneWordsToday = (TextView) findViewById(R.id.already_done_words_today);
        mWordsToday = (TextView) findViewById(R.id.words_today);

        //SharedPreference
        myPref = new MyPreference(getApplicationContext());
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
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.change_plan_menu_item){
            Intent intent = new Intent(HomeActivity.this, ChangePlanActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
