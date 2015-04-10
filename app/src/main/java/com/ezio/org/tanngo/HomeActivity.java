package com.ezio.org.tanngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cn.bmob.v3.Bmob;


public class HomeActivity extends ActionBarActivity{


    private final static String BMOB_APP_ID ="9118cc097159952e26caf5c9b535006e";

    //initial view
    private Button mWordsListBTN;
    private Button mChangePlanBTN;
    private Button mStartBackWordsBTN;

    MyPreference myPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();

        //test code
        String dictName = myPref.getDictName();
        if (dictName==null){
            Log.d(Utility.LOG_TAG, "dictName is null");
        }else {
            Log.d(Utility.LOG_TAG,"I dont know dictName is what");
        }

        setListenerToBtn();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //if these is no dict , then make the "start back " button as "choose dict button"
        if (myPref.getDictName()==null||myPref.getWordsNumEachDay()==0){
            mStartBackWordsBTN.setText("选择单词书");
            mStartBackWordsBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,ChangePlanActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            mStartBackWordsBTN.setText("开始背单词");
            mStartBackWordsBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,WordActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

    private void initialize() {

        //initial Bmob SDK
        Bmob.initialize(this,BMOB_APP_ID);


        //button
        mWordsListBTN = (Button)findViewById(R.id.words_list_BTN);
        mChangePlanBTN = (Button)findViewById(R.id.change_plan_BTN);
        mStartBackWordsBTN=(Button)findViewById(R.id.start_back_words_BTN);

        //SharedPreference
        myPref = new MyPreference(getApplicationContext());
    }

    private void setListenerToBtn() {
        mWordsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: later
            }
        });

        mChangePlanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //How to get outer class's field? use outerClassName.property
                Intent intent = new Intent(HomeActivity.this,ChangePlanActivity.class);
                startActivity(intent);
            }
        });



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
        }

        return super.onOptionsItemSelected(item);
    }


}
