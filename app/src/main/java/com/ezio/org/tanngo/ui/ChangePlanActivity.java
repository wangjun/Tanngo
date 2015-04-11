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


public class ChangePlanActivity extends BaseActivity implements View.OnClickListener {

    private Button mPickWordsBookBTN;
    private Button mPickDeadlineBTN;
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

        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
    }

    @Override
    public void initListeners() {

        mPickWordsBookBTN.setOnClickListener(this);
        mPickDeadlineBTN.setOnClickListener(this);
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
            Intent intent = new Intent(this, SelectWordsBookActivity.class);
            startActivity(intent);
        } else if (v == mPickDeadlineBTN) {
            showDialog(DATE_DIALOG_ID);
        }
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
