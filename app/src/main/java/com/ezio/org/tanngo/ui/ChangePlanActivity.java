package com.ezio.org.tanngo.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ezio.org.tanngo.R;
import com.ezio.org.tanngo.data.WordsBooksTable;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateDisplay();

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

        BmobQuery<WordsBooksTable> query = new BmobQuery<WordsBooksTable>();

        Utility.ShowDebugLog("prepare","prepare");

        query.findObjects(this,new FindListener<WordsBooksTable>() {
            @Override
            public void onSuccess(List<WordsBooksTable> wordsBooksTables) {

                for (int j = 0; j<wordsBooksTables.size();j++){
                    Utility.ShowDebugLog("success","success");
                    Utility.ShowDebugLog("name",wordsBooksTables.get(j).getBookName());
                    Utility.ShowDebugLog("describe",wordsBooksTables.get(j).getBookName());
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
