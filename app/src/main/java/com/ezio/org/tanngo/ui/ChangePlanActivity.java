package com.ezio.org.tanngo.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsBook;
import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.ezio.org.tanngo.utils.MyPreference;
import com.ezio.org.tanngo.utils.Utility;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class ChangePlanActivity extends BaseActivity implements View.OnClickListener {

    private Button mPickWordsBookBTN;
    private Button mPickDeadlineBTN;
    private Button mReadFakeDataBTN;
    private TextView mDateDisplay;

    public static final int DATE_DIALOG_ID = 0;

    private int mYear;
    private int mMonth;
    private int mDay;

    public final static String FAKE_DICT_NAME= "fake";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateDisplay();

        Button testButton = (Button) findViewById(R.id.make_testing_dict);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Ezio", "onClick----->");
                 Context context = v.getContext();

                //change words book name in SP to the table name of words book
                myPref = new MyPreference(context);
                myPref.setDictName(FAKE_DICT_NAME);


                creatFooDict(context);

                myPref.getRemainingWordsNumTotal();
                Toast.makeText(context, "db created ,please back", Toast.LENGTH_SHORT).show();


            }
        });

    }

    public void creatFooDict(Context context) {

        //create db
        WordsDbHelper mDbHelp = new WordsDbHelper(context);
        SQLiteDatabase db = mDbHelp.getWritableDatabase();

        if (!mDbHelp.isTableExist(myPref.getDictName())){

            //create table
            mDbHelp.creatTableIfNotExist(myPref.getDictName());


            //insert data into table
            for (int i = 0; i < 3000; i++) {
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
    public void setContentView() {
        setContentView(R.layout.activity_change_plan);
    }

    @Override
    public void initViews() {

        mPickWordsBookBTN = (Button) findViewById(R.id.pick_words_book_button);
        mPickDeadlineBTN = (Button) findViewById(R.id.pick_deadline_button);
        mReadFakeDataBTN = (Button)findViewById(R.id.read_fake_data);

        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
    }

    @Override
    public void initListeners() {

        mPickWordsBookBTN.setOnClickListener(this);
        mPickDeadlineBTN.setOnClickListener(this);
        mReadFakeDataBTN.setOnClickListener(this);
    }

    @Override
    public void initData() {

        mYear = myPref.getDeadlineYear();
        mMonth = myPref.getDeadlineMonth();
        mDay = myPref.getDeadlineDay();
    }


    @Override
    public void onClick(View v) {
        if (v == mPickWordsBookBTN) {
            Intent intent = new Intent(this, WordsBooksListActivity.class);
            startActivity(intent);
        } else if (v == mPickDeadlineBTN) {
            showDialog(DATE_DIALOG_ID);
        }else if(v ==mReadFakeDataBTN){
            readFakeData();
        }
    }

    private void readFakeData() {

        BmobQuery<WordsBook> query = new BmobQuery<WordsBook>();

        Utility.ShowDebugLog("prepare","prepare");

        query.findObjects(this,new FindListener<WordsBook>() {
            @Override
            public void onSuccess(List<WordsBook> wordsBooks) {

                for (int j = 0; j< wordsBooks.size();j++){
                    Utility.ShowDebugLog("success","success");
                    Utility.ShowDebugLog("name", wordsBooks.get(j).getBookName());
                    Utility.ShowDebugLog("describe", wordsBooks.get(j).getBookName());
                }

            }

            @Override
            public void onError(int i, String s) {

                Utility.ShowDebugLog("Error","Error");
            }


        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this,
                    mDateSetListener,
                    mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == DATE_DIALOG_ID) {
            ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

        }
    }

    private void updateDisplay() {
        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(""));

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    myPref.setDeadlineDay(mDay);
                    myPref.setDeadlineMonth(mMonth);
                    myPref.setDeadlineYear(mYear);
                    updateDisplay();
                }
            };


}
