package com.ezio.org.tanngo.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.ezio.org.tanngo.utils.Utility;

import java.util.HashSet;
import java.util.Iterator;


public class WordActivity extends BaseActivity implements
        WordQuestionFragment.OnAnswerSelectedListener,
        WordAnswerFragment.OnNextWordBtnSelectedListener {

    //control tools
    private int todayWordsNum;
    //now word index also have a meaning , it equal to how many words already backed today
    private int nowWordIndex = 0;


    public final static String QUESTION_PAGE = "question_page";
    public final static String ANSWER_PAGE = "answer_page";
    public final static String DONE_PAGE = "done_page";


    private String nextPageType = QUESTION_PAGE;

    //fragment tag , 然而并没有什么卵用
    public final static String QUE_TAG = "question_tag";
    public final static String ANS_TAG = "answer_tag";

    //words' info list
    private String[] wordsList;
    private String[] kanaList;
    private String[] definitionList;
    private String[] exSentenceList;

    //fragment args key
    public final static String WORD_KEY = "word_key";
    public final static String KANA_KEY = "kana_key";
    public final static String DEFI_KEY = "definition_key";
    public final static String EXSE_KEY = "example_sentence_key";
    public final static String WRONG_WORD_DEFI_1_KEY = "wrong_word_defi_1_key";
    public final static String WRONG_WORD_DEFI_2_KEY = "wrong_word_defi_2_key";
    public final static String WRONG_WORD_DEFI_3_KEY = "wrong_word_defi_3_key";
    public final static String[] WRONG_WORD_KEY_LIST = {
            WRONG_WORD_DEFI_1_KEY,
            WRONG_WORD_DEFI_2_KEY,
            WRONG_WORD_DEFI_3_KEY
    };
    public final static String IS_SELECT_RIGHT_KEY = "is_select_right_key";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_word);
    }

    @Override
    public void initViews() {
        //get the content
        Bundle bundle = new Bundle();
        getFragmentPage(bundle);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {

        //get the words book's name , use it as table name to query from database
        String dictName = myPref.getDictName();
        todayWordsNum = myPref.getTodayWordsNumTotal();

        queryWordsInfo(dictName);
    }

    //判断下一个要填充的fragment类型,将需要显示的东西作为参数传进去,给fragment处理
    private void getFragmentPage(Bundle bundle) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        //two diff fragments have diff bundle, have to make some diff
        if (nextPageType.equals(QUESTION_PAGE)) {

            //this bundle will be set into fragment as args
            bundle.putString(WORD_KEY, wordsList[nowWordIndex]);
            bundle.putString(KANA_KEY, kanaList[nowWordIndex]);
            bundle.putString(DEFI_KEY, definitionList[nowWordIndex]);
            bundle.putString(EXSE_KEY, exSentenceList[nowWordIndex]);
            //Word Question Fragment need word, kana, definition + 3 wrong definitions, example sentence
            //first initial fragment object
            WordQuestionFragment wordQuestionFragment = new WordQuestionFragment();


            //Get three random numbers, as index of wrong answer
            HashSet<Integer> wrongDefiSet = new HashSet<>(3);
            Utility.randomSet(0, todayWordsNum - 1, 3, nowWordIndex, wrongDefiSet);

            //put three words' definition into bundle
            Iterator iterator = wrongDefiSet.iterator();
            for (int i = 0; iterator.hasNext() && i < WRONG_WORD_KEY_LIST.length; i++) {
                bundle.putString(WRONG_WORD_KEY_LIST[i], definitionList[((int) iterator.next())]);
            }

            //Set bundle as args and show this fragment
            wordQuestionFragment.setArguments(bundle);
            transaction.replace(R.id.container_in_word_activity,
                    wordQuestionFragment,
                    QUE_TAG + nowWordIndex).commit();


        } else if (nextPageType.equals(ANSWER_PAGE)) {

            //this bundle will be set into fragment as args
            bundle.putString(WORD_KEY, wordsList[nowWordIndex]);
            bundle.putString(KANA_KEY, kanaList[nowWordIndex]);
            bundle.putString(DEFI_KEY, definitionList[nowWordIndex]);
            bundle.putString(EXSE_KEY, exSentenceList[nowWordIndex]);
            //Word answer fragment need word , kana ,definition, example sentence, no more args to put
            //initial fragment
            WordAnswerFragment wordAnswerFragment = new WordAnswerFragment();
            wordAnswerFragment.setArguments(bundle);
            transaction.replace(R.id.container_in_word_activity,
                    wordAnswerFragment,
                    ANS_TAG + nowWordIndex).commit();


        } else if (nextPageType.equals(DONE_PAGE)) {
            //All words back done,show finished page
            DoneFragment doneFragment = new DoneFragment();
            transaction.replace(R.id.container_in_word_activity,
                    doneFragment,
                    null).commit();
        }
    }

    //在开始背单词之前,从数据库中取得今天的单词内容,写入4个数组
    private void queryWordsInfo(String dictName) {
        WordsDbHelper mDbHelper = new WordsDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String projection =
                WordsContract.WordsEntry.COLUMN_WORD + ", " +
                        WordsContract.WordsEntry.COLUMN_KANA + ", " +
                        WordsContract.WordsEntry.COLUMN_DEFINITION + ", " +
                        WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE;

        String sortOrder = WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT + " DESC";


        //build query sentence
        Cursor cursor = db.rawQuery("SELECT " + projection +
                " FROM " + dictName +
                " ORDER BY " + sortOrder +
                " LIMIT " + todayWordsNum + ";", null);


        //put words' info into lists
        wordsList = new String[todayWordsNum];
        kanaList = new String[todayWordsNum];
        definitionList = new String[todayWordsNum];
        exSentenceList = new String[todayWordsNum];

        if (cursor.moveToFirst()) {
            for (int i = 0; i < wordsList.length; i++, cursor.moveToNext()) {
                wordsList[i] = cursor.getString(cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_WORD));
                kanaList[i] = cursor.getString(cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_KANA));
                definitionList[i] = cursor.getString(cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_DEFINITION));
                exSentenceList[i] = cursor.getString(cursor.getColumnIndex(WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE));


            }
        }

        cursor.close();

    }


    //在用户选择了答案之后，判断答案是否正确，并将其传递给下面的AnswerFragment
    //同时更新数据库中Word的信息
    @Override
    public void onDefiBtnSelected(View v, Button rightBTN) {

        nextPageType = ANSWER_PAGE;

        //check is the user's answer is right ,and pass it to Answer Fragment
        Bundle bundle = new Bundle();
        Boolean isRight = (v.getId() == rightBTN.getId());
        bundle.putBoolean(IS_SELECT_RIGHT_KEY, isRight);
        getFragmentPage(bundle);

        WordsDbHelper mDbHelper = new WordsDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //update database
        if (isRight) {
            //let now word's continuous right times plus 1
            db.execSQL("UPDATE " + myPref.getDictName() +
                    " SET " + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT + " = "
                    + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT + "+1 WHERE "
                    + WordsContract.WordsEntry.COLUMN_WORD + " = '"
                    + wordsList[nowWordIndex] + "';");

        } else {
            //let now word's continuous right times be 0, and wrong times plus 1
            db.execSQL("UPDATE " + myPref.getDictName() +
                    " SET " + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT + " = 0, "
                    + WordsContract.WordsEntry.COLUMN_WRONG_TIMES + " = "
                    + WordsContract.WordsEntry.COLUMN_WRONG_TIMES + " +1 WHERE "
                    + WordsContract.WordsEntry.COLUMN_WORD + " = '"
                    + wordsList[nowWordIndex] + "';");
        }

        //test code
        Cursor cursor = db.rawQuery("SELECT " + WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT +
                ", " + WordsContract.WordsEntry.COLUMN_WRONG_TIMES +
                " FROM " + myPref.getDictName() + " WHERE " +
                WordsContract.WordsEntry.COLUMN_WORD + " = '" +
                wordsList[nowWordIndex] + "';", null);
        if (cursor.moveToFirst()) {
            int cr = cursor.getInt(cursor.getColumnIndexOrThrow(WordsContract.WordsEntry.COLUMN_CONTINUOUS_RIGHT));
            int wt = cursor.getInt(cursor.getColumnIndexOrThrow(WordsContract.WordsEntry.COLUMN_WRONG_TIMES));
            Log.d(Utility.LOG_TAG, "Word-->" + wordsList[nowWordIndex] + ";CONTINUOUS_RIGHT--->" + cr + ";WRONG_TIMES--->" + wt);
            cursor.close();
        }

    }

    @Override
    public void onNextWordBtnSelected(Boolean isCorrect) {

        nextPageType = QUESTION_PAGE;


        if (isCorrect) {
            //TODO:if it is correct,++ and sql update
            nowWordIndex++;
            int remainingWords = todayWordsNum - nowWordIndex;
            myPref.setTodayWordsRemaining(remainingWords);
            if (remainingWords == 0) {
                nextPageType = DONE_PAGE;
            }

        } else {
            //TODO:update sql wrong times
        }

        Bundle bundle = new Bundle();
        getFragmentPage(bundle);
    }
}
