package com.ezio.org.tanngo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ezio on 2015/4/6.
 */
public class WordQuestionFragment extends Fragment implements View.OnClickListener{

    OnAnswerSelectedListener mCallback;

    private View rootView;

    private Button  defiBTN_1;
    private Button  defiBTN_2;
    private Button  defiBTN_3;
    private Button  defiBTN_4;

    private Button rightBTN;

    private TextView wordText;
    private TextView kanaText;
    private TextView exSentenceText;



    public interface OnAnswerSelectedListener{
        public void onDefiBtnSelected(View v,Button rightBTN);
    }



    public WordQuestionFragment() {
        super();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_word_question, container, false);

        initialView();

        setTextToView();

        setListenerToBtn();



        return rootView;
    }

    private void initialView(){
        wordText = (TextView)rootView.findViewById(R.id.word_text);
        kanaText = (TextView)rootView.findViewById(R.id.kana_text);
        exSentenceText = (TextView)rootView.findViewById(R.id.example_sentence_text);

        defiBTN_1 = (Button)rootView.findViewById(R.id.defi_btn_1);
        defiBTN_2 = (Button)rootView.findViewById(R.id.defi_btn_2);
        defiBTN_3 = (Button)rootView.findViewById(R.id.defi_btn_3);
        defiBTN_4 = (Button)rootView.findViewById(R.id.defi_btn_4);
    }

    private void setTextToView(){
        Bundle args = getArguments();

        //Set text to view
        wordText.setText(args.getString(WordActivity.WORD_KEY));
        kanaText.setText(args.getString(WordActivity.KANA_KEY));
        exSentenceText.setText(args.getString(WordActivity.EXSE_KEY));

        //Set text to button
        Object[] defiBtnList = {defiBTN_1,defiBTN_2,defiBTN_3,defiBTN_4};
        int randomNum = Utility.randomInt(0,3);
        rightBTN = (Button)defiBtnList[randomNum];
        (rightBTN).setText(args.getString(WordActivity.DEFI_KEY));

        defiBtnList[randomNum]= null;

        //容易出错的地方

        for (int i = 0, j =0;i<defiBtnList.length;i++){
            if (defiBtnList[i]!=null){
                ((Button)defiBtnList[i]).setText(args.getString(WordActivity.WRONG_WORD_KEY_LIST[j++]));

            }
        }
    }

    private void setListenerToBtn(){
        defiBTN_1.setOnClickListener(this);
        defiBTN_2.setOnClickListener(this);
        defiBTN_3.setOnClickListener(this);
        defiBTN_4.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //check if activity implement this fragment's interface
        try {
            mCallback = (OnAnswerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAnswerSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {

        if (rightBTN!=null){
            mCallback.onDefiBtnSelected(v,rightBTN);
        }

    }
}
