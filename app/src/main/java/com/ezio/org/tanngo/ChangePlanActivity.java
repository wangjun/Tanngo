package com.ezio.org.tanngo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


public class ChangePlanActivity extends ActionBarActivity implements View.OnClickListener{

    private Button mPickWordsBookBTN;
    private Button mPickDeadlineBTN;
    private TextView mDateDisplay;

    static final int DATE_DIALOG_ID = 0;

    private int mYear;
    private int mMonth;
    private int mDay;

    MyPreference pref;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_plan);

        mPickWordsBookBTN = (Button)findViewById(R.id.pick_words_book_button);
        mPickDeadlineBTN=(Button)findViewById(R.id.pick_deadline_button);

        mPickWordsBookBTN.setOnClickListener(this);
        mPickDeadlineBTN.setOnClickListener(this);


        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);



        pref = new MyPreference(this);
        mYear = pref.getDeadlineYear();
        mMonth = pref.getDeadlineMonth();
        mDay = pref.getDeadlineDay();


//        final Calendar c = Calendar.getInstance();
//        mYear = c.get(Calendar.YEAR);
//        mMonth = c.get(Calendar.MONTH);
//        mDay = c.get(Calendar.DAY_OF_MONTH);


        updateDisplay();

    }





    @Override
    public void onClick(View v) {
        if (v==mPickWordsBookBTN){
            Intent intent = new Intent(this,SelectWordsBookActivity.class);
            startActivity(intent);
        }else if(v==mPickDeadlineBTN){
            showDialog(DATE_DIALOG_ID);
        }
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        if (id==DATE_DIALOG_ID) {
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id ==DATE_DIALOG_ID){
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

        }
    }

    private void updateDisplay() {
        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    pref.setDeadlineDay(mDay);
                    pref.setDeadlineMonth(mMonth);
                    pref.setDeadlineYear(mYear);
                    updateDisplay();
                }
            };




}
