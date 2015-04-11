package com.ezio.org.tanngo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;

/**
 * Created by Ezio on 2015/4/1.
 */

/*
* This is a sharedPreference handler
* You can use it :
* 1.get/set deadline year/month/day, and word dict name . they are unique
*
*
*
* */
public class MyPreference {
    private Context mContext;
    private SharedPreferences sp;

    private long currentTimeMillis;
    private long defaultDeadlineTimeMillis;
    private long diffTimeMillis;

    private int currentJulianDay;

    private Time defaultDeadlineTime;

    private int diffDay;
    private int remainingWordsNum;

    private final static String saved_deadline_year = "SAVED_DEADLINE_YEAR";
    private final static String saved_deadline_month = "SAVED_DEADLINE_MONTH";
    private final static String saved_deadline_day = "SAVED_DEADLINE_DAY";

    private final static String saved_dict_name = "SAVED_DICT_NAME";

    public final static int today_havent_run = -5;
    public final static int EACH_DAY_AT_LEAST_WORDS = 10;


    public final static String TOTAL_TAG = "TOTAL";

    public MyPreference(Context context) {
        mContext = context;

        //initial SharedPreferences editor
        sp = mContext.getSharedPreferences(
                mContext.getString(R.string.default_preference_file_key), Context.MODE_PRIVATE);

        //get current time and new a two later Time object
        Time time = new Time();
        time.setToNow();

        //get default deadline time millis
        long foo = 5184000000L;
        currentTimeMillis = System.currentTimeMillis();
        defaultDeadlineTimeMillis = currentTimeMillis + foo;

        defaultDeadlineTime = new Time();
        defaultDeadlineTime.set(defaultDeadlineTimeMillis);


//        Log.d(Utility.LOG_TAG,"currentTimeM--->"+currentTimeMillis );
//        Log.d(Utility.LOG_TAG,"ddtTimeM--->"+defaultDeadlineTimeMillis );

        currentJulianDay = Time.getJulianDay(currentTimeMillis, time.gmtoff);
        //int defaultDeadlineTimeJulianDay = Time.getJulianDay(defaultDeadlineTimeMillis,time.gmtoff);


//        Log.d(Utility.LOG_TAG,"currentTime--->"+time.month+" Month"+ time.monthDay+" Day" );
//        Log.d(Utility.LOG_TAG,"ddtTime--->"+defaultDeadlineTime.month+" Month"+ defaultDeadlineTime.monthDay+" Day" );


    }

    //getter and setter


    //获取离死线还有多少天，因为同一时间只能选择一个单词书，并且只有一个死线，所以这个不需要区分单词书，是唯一的
    public int getRemainingDay() {
        Time deadlineTime = new Time();
        deadlineTime.set(getDeadlineDay(), getDeadlineMonth(), getDeadlineYear());
        long deadlineTimeMillis = deadlineTime.toMillis(false);
        diffTimeMillis = deadlineTimeMillis - currentTimeMillis;
        if (diffTimeMillis < 0) {
            diffTimeMillis = 0;
        }
        diffDay = (int) (diffTimeMillis / 86400000L + 1L);
        return diffDay;


    }

    //TODO:获取总词数
    public int getWordsNumTotal() {
        return 999;
    }

    //获取离完成还有多少单词，这个需要查询数据库，同一时间只有一个值，与单词书对应
    public int getRemainingWordsNumTotal() {
        Log.d(Utility.LOG_TAG, "getWordsNum-----");

        if (getDictName() == null) {
            return 0;
        }
        WordsDbHelper mDbHelper = new WordsDbHelper(mContext);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //TODO:改写这个sql

        Cursor cursor = db.rawQuery("SELECT COUNT(" + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT
                + ") FROM " + getDictName() +
                " WHERE " + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT + " < 7", null);

        Log.d(Utility.LOG_TAG, "getWordsNum-----1");
        cursor.moveToFirst();
        Log.d(Utility.LOG_TAG, "getWordsNum-----2");
        remainingWordsNum = cursor.getInt(0);
        cursor.close();


        Log.d(Utility.LOG_TAG, "words number --->" + remainingWordsNum);
        return remainingWordsNum;


    }

    //由天数还有单词书得到每天平均要背多少，这个值应该是每天每个单词书有一个，一天内多次切换暂不考虑存储
    //每次都用重新查询来覆盖
    private int getWordsNumEachDay() {
        //getRemainingTime()
        //先要从database那边获得还没背的单词的数量
        //然后与剩下的天数相除

        //所以需要：
        //TODO:1.从database中query总的没有完成的单词量，也就是连续正确没到7的单词
        //TODO:2.把这个数据存储在pref中，这个只有每天需要调用一次,除非词典变了
        //TODO:3.以这个决定今天还需要背多少单词，存储在perf中，然后背完一个就递减一个
        //TODO:4.那么这样总的单词数要乘以7

        if (getRemainingWordsNumTotal() == 0 || getRemainingDay() == 0) {
            return 0;
        }

        int wordsNumEachDay = getRemainingWordsNumTotal() * 7 / getRemainingDay();
        int remainderNum = getRemainingWordsNumTotal() * 7 % getRemainingDay();


        //b
        //每天最少背10个词
        if (wordsNumEachDay > EACH_DAY_AT_LEAST_WORDS) {
            return wordsNumEachDay;
        } else if (wordsNumEachDay <= EACH_DAY_AT_LEAST_WORDS) {
            return EACH_DAY_AT_LEAST_WORDS;
        } else if (wordsNumEachDay == 0 && remainderNum > 7) {
            return remainderNum;
        } else if (wordsNumEachDay == 0 && remainderNum <= 7) {
            return 0;
        }


        Toast.makeText(mContext, "something wrong,please connect developer", Toast.LENGTH_SHORT).show();
        return remainderNum;

    }

    //TODO:下面需要一个判断是否今天已经运行过的函数，
    //日志是与单词书一一对应的，以单词table的name来作为SP文件的名字




    /*sharedPreferences' name are dictName, store key:value pairs ,
    key are that day's currentJulianDay ,
    value are that day's already backed words number, the todayWordsNum*/


    public int getTodayWordsRemaining() {
        //没运行过的话，获得今天需要背多少，并且写到今天的日志中,今天以后就不会走这条线了
        //如果运行过，从日志中取得需要背的数量
        //因为这个是今天还剩的单词数，是会随着用户的背诵而变化的，所以还有个set方法

        if (getDictName() == null) {
            return 0;
        }

        SharedPreferences dailySP = mContext.getSharedPreferences(getDictName(), Context.MODE_PRIVATE);
        int todayWordsRemaining = dailySP.getInt(currentJulianDay + "", today_havent_run);
        if (todayWordsRemaining == today_havent_run) {
            SharedPreferences.Editor editor = dailySP.edit();
            editor.putInt(currentJulianDay + "", getTodayWordsNumTotal());
            editor.apply();
            return getTodayWordsNumTotal();
        } else {
            return todayWordsRemaining;
        }
    }

    public void setTodayWordsRemaining(int wordsNum) {
        if (getDictName() != null) {
            SharedPreferences dailySP = mContext.getSharedPreferences(getDictName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = dailySP.edit();
            editor.putInt(currentJulianDay + "", wordsNum);
            editor.apply();
        }


    }

    //因为如果在背诵过程中改database的话，每天需要背的单词数可能会变动，
    // 所以不能依靠getWordsNumEach来获得今天要背词的总数，有必要用一个一天内不会变动的数来存储
    //类似于get/setTodayWordsRemaining,但是因为外部不需要set的功能，所以不提供，每天初始化已经在get中做了
    public int getTodayWordsNumTotal() {
        if (getDictName() == null) {
            return 0;
        }

        SharedPreferences dailyTotalSP = mContext.getSharedPreferences(
                getDictName() + TOTAL_TAG, Context.MODE_PRIVATE);
        int todayWordsTotal = dailyTotalSP.getInt(currentJulianDay + TOTAL_TAG, today_havent_run);
        if (todayWordsTotal == today_havent_run) {
            SharedPreferences.Editor editor = dailyTotalSP.edit();
            editor.putInt(currentJulianDay + TOTAL_TAG, getWordsNumEachDay());
            editor.apply();
            return getWordsNumEachDay();
        } else {
            return todayWordsTotal;
        }
    }


    //getter/setter，都是唯一的，不需费心
    public int getDeadlineYear() {
        return sp.getInt(saved_deadline_year, defaultDeadlineTime.year);
    }

    public int getDeadlineMonth() {
        return sp.getInt(saved_deadline_month, defaultDeadlineTime.month);
    }

    public int getDeadlineDay() {
        return sp.getInt(saved_deadline_day, defaultDeadlineTime.monthDay);
    }

    public String getDictName() {
        return sp.getString(saved_dict_name, null);
    }

    public void setDeadlineYear(int year) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(saved_deadline_year, year);
        editor.apply();
    }

    public void setDeadlineMonth(int month) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(saved_deadline_month, month);
        editor.apply();
    }

    public void setDeadlineDay(int day) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(saved_deadline_day, day);
        editor.apply();
    }

    public void setDictName(String dictName) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(saved_dict_name, dictName);
        editor.apply();
    }


}
