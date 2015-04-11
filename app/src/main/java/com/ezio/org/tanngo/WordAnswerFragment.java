package com.ezio.org.tanngo;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordAnswerFragment extends Fragment {


    OnNextWordBtnSelectedListener mCallback;

    private View rootView;

    private TextView wordText;
    private TextView kanaText;
    private TextView defiText;
    private TextView exSentenceText;
    private TextView isCorrectText;

    private Button nextWordBTN;

    private Boolean isCorrect;


    public WordAnswerFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_word_answer, container, false);

        initialView(rootView);

        Bundle args = getArguments();
        isCorrect = args.getBoolean(WordActivity.IS_SELECT_RIGHT_KEY);

        setTextToView(args);

        nextWordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onNextWordBtnSelected(isCorrect);

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnNextWordBtnSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextWordBtnSelectedListener");
        }
    }

    private void initialView(View rootView) {
        wordText = (TextView) rootView.findViewById(R.id.answer_word_text);
        kanaText = (TextView) rootView.findViewById(R.id.answer_kana_text);
        defiText = (TextView) rootView.findViewById(R.id.answer_definition_text);
        exSentenceText = (TextView) rootView.findViewById(R.id.answer_example_sentence_text);
        isCorrectText = (TextView) rootView.findViewById(R.id.is_answer_correct_text);
        nextWordBTN = (Button) rootView.findViewById(R.id.next_word_btn);

    }

    private void setTextToView(Bundle args) {
        //Set text to view
        wordText.setText(args.getString(WordActivity.WORD_KEY));
        kanaText.setText(args.getString(WordActivity.KANA_KEY));
        defiText.setText(args.getString(WordActivity.DEFI_KEY));
        exSentenceText.setText(args.getString(WordActivity.EXSE_KEY));
        if (isCorrect) {
            isCorrectText.setText("正确！");
        } else {
            isCorrectText.setText("错误！");
        }
    }

    public interface OnNextWordBtnSelectedListener {
        public void onNextWordBtnSelected(Boolean isCorrect);
    }


}
