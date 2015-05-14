package com.example.user.voicedialog.adapters;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.user.voicedialog.R;
import com.example.user.voicedialog.models.Question;

import java.util.List;

/**
 * Created by user on 06.05.2015.
 */
public class QuestionsAdapter extends BaseAdapter {

    private List<Question> questionList;
    private Activity activity;
    private TextView questionTextView;
    private TextView answerTextView;
    public QuestionsAdapter(Activity activity,List<Question> questionList)
    {
        this.questionList=questionList;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=activity.getLayoutInflater().inflate(R.layout.question_item,null);
        questionTextView=(TextView)convertView.findViewById(R.id.question_item_textview_question);
        answerTextView=(TextView) convertView.findViewById(R.id.question_item_textview_answer);
        questionTextView.setText("Вопрос: "+questionList.get(position).getQuestionText());
        answerTextView.setText(Html.fromHtml(questionList.get(position).getAnswerText()));
        return convertView;
    }
}
