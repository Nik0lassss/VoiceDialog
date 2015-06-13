package com.example.user.voicedialog.mappers;

import android.text.Html;

import com.example.user.voicedialog.helper.Helper;
import com.example.user.voicedialog.models.Animation;
import com.example.user.voicedialog.models.Image;
import com.example.user.voicedialog.models.Question;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15.05.2015.
 */
public class QuestionMapper {
    public static String getFileExtention(String filename)
    {
    int dotPos=filename.lastIndexOf(".")+1;
        return filename.substring(dotPos);
    }
    public static String getFileName(String filename)
    {
        int dotPos=filename.lastIndexOf(".");
        int slashPos= filename.lastIndexOf("/")+1;
        return   filename.subSequence(slashPos,dotPos).toString();
    }
    public static Question MapStringToQuestion(String response,String questionText)
    {
    Question question = new Question();
        question.setAnswerText(Html.fromHtml(response).toString().replace("￼.","").replace("/n",""));

        question.setQuestionText(questionText);
        Document doc = Jsoup.parse(response);
        List<Image> imagesList = new ArrayList<Image>();
        List<Animation> animationsList = new ArrayList<Animation>();
         for(Element image : doc.select("img"))
         {
             String imageAttr = image.attr("src");
             String fileExtention= QuestionMapper.getFileExtention(imageAttr);
             String fileName = QuestionMapper.getFileName(imageAttr);
             if (fileExtention.equals("mp4"))
             {
                 animationsList.add(new Animation(null,null, Helper.getURL()+"/"+imageAttr,null));
             }
             if(fileExtention.equals("gif"))
             {
                 imagesList.add(new Image(null, Helper.getURL()+"/"+imageAttr,fileName,null));
             }

         }
        question.setListAnimation(animationsList);
        question.setListImages(imagesList);

        return question;
    }
}
