package com.example.user.voicedialog.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.user.voicedialog.ImageActivity;
import com.example.user.voicedialog.R;
import com.example.user.voicedialog.fragments.VoiceDialogFragment;
import com.example.user.voicedialog.fragments.ZoomFragment;
import com.example.user.voicedialog.helper.Helper;
import com.example.user.voicedialog.models.Question;
import com.example.user.voicedialog.sender.SenderRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
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
    private VideoView videoWebView;
    private Response.Listener<Bitmap> responseListenerBitmap;
    private SenderRequest senderRequest;
    private Response.ErrorListener errorListener;
    private String fileName;
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
LinearLayout linLayout = (LinearLayout) convertView.findViewById(R.id.question_item_linear_layout_answer);
        linLayout.setBackgroundResource(R.drawable.message_layout_background_dialog_system);
        answerTextView=(TextView) convertView.findViewById(R.id.question_item_textview_answer);
        //answerTextView.setBackgroundResource(R.drawable.message_layout_background);
        questionTextView.setBackgroundResource(R.drawable.message_layout_background);
        pictureAnswer = (ImageView) convertView.findViewById(R.id.question_fragment_item_image_view_picture);
        pictureAnswer.setClickable(true);
        pictureAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageActivity.class);
                intent.putExtra("image_src",fileName);
                activity.startActivity(intent);
            }
        });
        videoWebView= (VideoView) convertView.findViewById(R.id.question_fragment_item_web_view);
        MediaController mediaController = new
                MediaController(activity);
        mediaController.setAnchorView(videoWebView);
        videoWebView.setMediaController(mediaController);
        videoWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                videoWebView.start();
                if(!videoWebView.isPlaying())
                {
                    videoWebView.start();
                }
                else
                {
                    videoWebView.stopPlayback();
                }
                return true;
            }
        });
        //String path1="http://192.168.137.1/figs/kalom.mp4";
        if(questionList.get(position).getListAnimation()!=null && questionList.get(position).getListAnimation().size()!=0)
        {
            Uri uri=Uri.parse("http://"+questionList.get(position).getListAnimation().get(0).getAnimation_src());
            videoWebView.setVideoURI(uri);
            videoWebView.setVisibility(View.VISIBLE);
        }
        else videoWebView.setVisibility(View.GONE);
       // videoWebView.clearFocus();

        //videoWebView.dispatchWindowFocusChanged(true);


        questionTextView.setText("Вопрос: "+questionList.get(position).getQuestionText());
        answerTextView.setText("Ответ: "+ questionList.get(position).getAnswerText());

            this.responseListenerBitmap = new Response.Listener<Bitmap>() {

                @Override
                public void onResponse(Bitmap bitmap) {
                    pictureAnswer.setImageBitmap(bitmap);
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),fileName+ ".png");
                    try {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } finally {
                            if (fos != null) fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    answerTextView.setText(volleyError.getMessage());
                }
            };
        if(questionList.get(position).getListImages()!=null && questionList.get(position).getListImages().size()!=0)
        {
            senderRequest.getPicture("http://"+questionList.get(position).getListImages().get(0).getImage_src(), responseListenerBitmap,errorListener);
            pictureAnswer.setVisibility(View.VISIBLE);
            fileName= "test";
        }
        else pictureAnswer.setVisibility(View.GONE);


        return convertView;
    }
}
