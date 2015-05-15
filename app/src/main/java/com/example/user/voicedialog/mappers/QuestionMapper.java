package com.example.user.voicedialog.mappers;

import android.text.Html;

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
    public static Question MapStringToQuestion(String response,String questionText)
    {
    Question question = new Question();
        question.setAnswerText(Html.fromHtml(response).toString());
        question.setQuestionText(questionText);
        Document doc = Jsoup.parse(response);
        List<Image> imagesList = new ArrayList<Image>();
         for(Element image : doc.select("img"))
         {
           imagesList.add(new Image(null,image.attr("src"),null,null));
         }
        question.setListImages(imagesList);
        return question;
    }
}
