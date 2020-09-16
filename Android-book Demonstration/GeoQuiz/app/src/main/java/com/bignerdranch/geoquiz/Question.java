package com.bignerdranch.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAlreadyAnswered;

//    getters
    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() { return mAnswerTrue; }

    public boolean isAlreadyAnswered() { return mAlreadyAnswered; }

// setters
    public void setTextResId(int textResId) {
    mTextResId = textResId;
}

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void  setAlreadyAnswered(boolean alreadyAnswered) { mAlreadyAnswered = alreadyAnswered; }

    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAlreadyAnswered = false;

    }
}
