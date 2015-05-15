package com.example.user.voicedialog.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.user.voicedialog.R;
import com.example.user.voicedialog.helper.Helper;
import com.example.user.voicedialog.models.Question;
import com.example.user.voicedialog.sender.SenderRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by user on 06.05.2015.
 */
public class QuestionsAdapter extends BaseAdapter {

    private List<Question> questionList;
    private Activity activity;
    private TextView questionTextView;
    private TextView answerTextView;
    private ImageView pictureAnswer;
    private Response.Listener<Bitmap> responseListenerBitmap;
    private SenderRequest senderRequest;
    private Response.ErrorListener errorListener;
    public QuestionsAdapter(Activity activity,List<Question> questionList)
    {
        this.questionList=questionList;
        this.activity=activity;
         senderRequest = new SenderRequest(activity);
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
        pictureAnswer = (ImageView) convertView.findViewById(R.id.question_fragment_item_image_view_picture);

        questionTextView.setText("Вопрос: "+questionList.get(position).getQuestionText());
        answerTextView.setText(questionList.get(position).getAnswerText());

            this.responseListenerBitmap = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    pictureAnswer.setImageBitmap(bitmap);
                }
            };
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    answerTextView.setText(volleyError.getMessage());
                }
            };
            senderRequest.getPicture("", responseListenerBitmap,errorListener);

        return convertView;
    }
}
