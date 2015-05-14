package com.example.user.voicedialog.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 06.05.2015.
 */
public class Question implements Parcelable{
    private String questionText;
    private String answerText;


    public  static  final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public Question(String questionText, String answerText) {
        this.questionText = questionText;
        this.answerText = answerText;
    }
    private Question(Parcel parcel)
    {
        questionText=parcel.readString();
        answerText= parcel.readString();
    }
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
     dest.writeString(questionText);
     dest.writeString(answerText);
    }


}
