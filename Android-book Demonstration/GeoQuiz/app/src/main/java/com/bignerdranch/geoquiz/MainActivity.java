package com.bignerdranch.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    making log messages part 1 add TAG constant
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX_QUESTIONS_ANSWERED = "answered_index";
    private static final String KEY_INDEX_CORRECT_ANSWERS = "correct_answers_index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_INDEX_CHEATER = "cheater";
    private static final String KEY_INDEX_CURRENT_CHEATS = "current_cheats";


    //  adding variables
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private boolean mIsCheater;
    private Button mPreviousButton;
    private int mCurrentIndex = 0;
    private TextView mQuestionTextView;
    private int mCorrectAnswers = 0;
    private int mMaxCheats = 3;
    private int mCurrentCheats = 0;
    private TextView mCheatTextView;


    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      making log messages part 2 adding a log statement
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCorrectAnswers = savedInstanceState.getInt(KEY_INDEX_CORRECT_ANSWERS,0);
            mIsCheater = savedInstanceState.getBoolean(KEY_INDEX_CHEATER,false);
            mCurrentCheats = savedInstanceState.getInt(KEY_INDEX_CURRENT_CHEATS, 0);

            boolean[] questionsAnswered = savedInstanceState.getBooleanArray(KEY_INDEX_QUESTIONS_ANSWERED);
            for (int i = 0; i < questionsAnswered.length; i++){
                mQuestionBank[i].setAlreadyAnswered(questionsAnswered[i]);
            }
        }

//      wiring textview to cheat
        mCheatTextView = (TextView) findViewById(R.id.cheat);
//       added click listener to textview (click on text to see updated cheats)
        mCheatTextView.setText(mMaxCheats - mCurrentCheats + " Cheats Left");

//      wiring textview to question bank
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
//       added click listener to textview (click on text to see next question)
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

//      reference widget  true button
        mTrueButton = (Button) findViewById(R.id.true_button);

//      setting listener(when button is pressed) true button
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          check answer is true
                checkAnswer(true);
                updateQuestion();
            }
        });

//      reference widget  false button
        mFalseButton = (Button) findViewById(R.id.false_button);

//      setting listener(when button is pressed) false button
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateQuestion();
            }
        });


//      next button goes forward one question
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int percentage = (int) (((double) mCorrectAnswers / (double) mQuestionBank.length ) * 100);

                if(mCurrentIndex >= mQuestionBank.length - 1){
                    Toast toast = Toast.makeText(getBaseContext(),  String.valueOf(percentage) + "%", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 500);
                    toast.show();

                }   else {
                        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                        mIsCheater = false;
                        updateQuestion();
                }
            }
        });

//      previous button goes back one question
        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

//      cheat button starts cheat activity5
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {

             @Override
        public void onClick(View v) {
//              Start cheat activity
             if (mCurrentCheats == mMaxCheats) {
                 mCheatButton.setEnabled(false);
             }  else {
                 mCurrentCheats = mCurrentCheats + 1;
                 mCheatTextView.setText(mMaxCheats - mCurrentCheats + " Cheats Left");
             }

             boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
             Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
             startActivityForResult(intent, REQUEST_CODE_CHEAT);
        }
    });
    updateQuestion();

    }

//  gets result of cheat activity and assigns it to mIsCheater
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//      if not RESULT_OK end function
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

//      checks if it came from cheat_activity
        if (requestCode == REQUEST_CODE_CHEAT) {

//          if no data user did not cheat, end function
            if (data == null) {
                return;
            }

//          gets boolean showing user is a cheater
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

//  making log messages part 3 overriding more life cycle methods
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

//  saves instances for when screen is rotated
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_INDEX_CORRECT_ANSWERS, mCorrectAnswers);
        savedInstanceState.putBoolean(KEY_INDEX_CHEATER, mIsCheater);

        boolean[] questionsAnswered = new boolean[mQuestionBank.length];
        for (int i = 0; i < mQuestionBank.length; i++)
                questionsAnswered[i] = mQuestionBank[i].isAlreadyAnswered();
        savedInstanceState.putBooleanArray(KEY_INDEX_QUESTIONS_ANSWERED, questionsAnswered);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

//  updates question to correct index position
    private void updateQuestion(){
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);
            if(mQuestionBank[mCurrentIndex].isAlreadyAnswered()) {
                mTrueButton.setClickable(false);
                mFalseButton.setClickable(false);
            }   else{
                    mTrueButton.setClickable(true);
                    mFalseButton.setClickable(true);
            }
    }

    private void checkAnswer(boolean userPressedTrue){

        mQuestionBank[mCurrentIndex].setAlreadyAnswered(true);

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

//      if user cheated show judgy message
        if (mIsCheater){
            messageResId = R.string.judgy_message;
        }   else {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;
                    mCorrectAnswers = mCorrectAnswers + 1;
                }   else {
                        messageResId = R.string.incorrect_toast;
                }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show();
    }
}