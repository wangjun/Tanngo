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
import com.ezio.org.tanngo.ui.WordActivity;

/**
 * Created by Ezio on 2015/4/1.
 */

/**
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
    private final static String saved_page_type = "saved_page_type";
    private final static String is_word_right = "is_word_right";

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


        currentJulianDay = Time.getJulianDay(currentTimeMillis, time.gmtoff);





    }



    //获取离死线还有多少天，因为同一时间只能选择一个单词书，并且只有一个死线，所以这个不需要区分单词书，是唯一的
    /**
     * get the difference between today and deadline day
     * @return diffDay :int day number
     * @author
     * */
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

    public boolean isNewDay(){
        SharedPreferences dailySP = mContext.getSharedPreferences(getDictName(), Context.MODE_PRIVATE);
        int todayWordsRemaining = dailySP.getInt(currentJulianDay + "", today_havent_run);
        if (todayWordsRemaining == today_havent_run) {
            return false;
        }else {
            return true;
        }
    }



    /**
     * query database to get total words number
     * */
    public int getWordsNumTotal() {

        if (getDictName() == null) {
            return 0;
        }else {
            WordsDbHelper mDbHelper = new WordsDbHelper(mContext);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT COUNT(" + WordsContract.WordsEntry.COLUMN_WORD
                    + ") FROM " + getDictName() +
                    ";", null);
            cursor.moveToFirst();
            int wordsNumTotal = cursor.getInt(0);
            cursor.close();
            return wordsNumTotal;
        }



    }

    /**
     * query database to get total remaining words number, if no words book selected ,return 0
     * @return remaining words number
     * */
    //获取离完成还有多少单词，这个需要查询数据库，同一时间只有一个值，与单词书对应
    public int getRemainingWordsNumTotal() {
        Log.d(Utility.LOG_TAG, "getWordsNum-----");

        if (getDictName() == null) {
            return 0;
        }else {
            WordsDbHelper mDbHelper = new WordsDbHelper(mContext);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            //TODO:改写这个sql，弄个provider

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


    }

    //由天数还有单词书得到每天平均要背多少，这个值应该是每天每个单词书有一个，一天内多次切换暂不考虑存储
    //每次都用重新查询来覆盖
    /**
     * get words number each day base on now time data
     * it wouldn't be less than 10
     * @return int words number each day
     * */
    private int getWordsNumEachDay() {
        //getRemainingTime()
        //先要从database那边获得还没背的单词的数量*7,暂定一个单词连续正确7遍才算过关
        //然后与剩下的天数相除

        if (getRemainingWordsNumTotal() == 0 || getRemainingDay() == 0) {
            return 0;
        }

        int wordsNumEachDay = getRemainingWordsNumTotal() * 7 / getRemainingDay();
        int remainderNum = getRemainingWordsNumTotal() * 7 % getRemainingDay();



        //TODO:因为目前是在今天的其他词的释义中选取作为错误选项，所以在词很少的时候有些困难，
        // TODO:所以暂定每天最少背10个词，之后改进
        //TODO：另外不应该粗暴乘以7，而是sql query 每个单词剩下的次数，然后累加起来
        //对于词的给出逻辑还有很大提升余地
        //可能应该是每天每个单词要过一个3、4次连续正确这样

        if (wordsNumEachDay > EACH_DAY_AT_LEAST_WORDS) {
            return wordsNumEachDay;
        } else if (wordsNumEachDay <= EACH_DAY_AT_LEAST_WORDS) {
            int dlday =getRemainingWordsNumTotal() * 7 /10 +currentJulianDay;
            Time dltime = new Time();
            dltime.setJulianDay(dlday);
            setDeadlineDay(dltime.monthDay);
            setDeadlineMonth(dltime.month + 1);
            setDeadlineYear(dltime.year);
            return EACH_DAY_AT_LEAST_WORDS;
        } else if (wordsNumEachDay == 0 && remainderNum > 7) {
            return remainderNum;
        } else if (wordsNumEachDay == 0 && remainderNum <= 7) {
            return 0;
        }


        Toast.makeText(mContext, "something wrong,please connect developer", Toast.LENGTH_SHORT).show();
        return remainderNum;

    }


    //日志是与单词书一一对应的，以单词table的name来作为SP文件的名字




    /*sharedPreferences' name are dictName, store key:value pairs ,
    key are that day's currentJulianDay ,
    value are that day's already backed words number, the todayWordsNum*/


    /**
     * today words remaining, different words book has be stored differently
     * */
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

    /**
     * use it to set today remaining words number when user do back words progress
     * @param wordsNum now remaining words number.
     * @author
     * */
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
    /**
     * today words number for now words book, it's final in one day
     * */
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

    public String getWordPageType(){
        return sp.getString(saved_page_type, WordActivity.QUESTION_PAGE);
    }

    public boolean getIsWordRight(){
        return sp.getBoolean(is_word_right, true);
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

    public void setWordPageType(String pageType){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(saved_page_type, pageType);
        editor.apply();
    }

    public void setIsWordRight(boolean isRight){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(is_word_right, isRight);
        editor.apply();
    }


}
