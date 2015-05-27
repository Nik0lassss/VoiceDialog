package com.example.user.voicedialog.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.voicedialog.dbhelper.DbOpenHelper;
import com.example.user.voicedialog.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bunis on 13.05.2015.
 */
public class SqlDatabaseHelper {
    private static final String DB_NAME = "voiceDialogHistory.sqlite3";
    private static final String TABLE_NAME_HISTORY = "history";
    private static final String TABLE_NAME_IMAGES = "images";
    private static final int DB_VESION = 1;
    // Для удобства выполнения sql-запросов
    // создадим константы с именами полей таблицы
    // и номерами соответсвующих столбцов
    private static final String KEY_ID = "_id";
    private static final int ID_COLUMN = 0;
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    private static final int QUESTION_COLUMN = 1;
    private static final int ANSWER_COLUMN = 2;
    private static final String KEY_IMAGE_SOURCE = "image_source";
    private static final String KEY_QUESTION_ID = "question_id";
    private static final int IMAGE_SOURCE_COLUMN = 1;
    private static final int QUESTION_ID_COLUMN = 2;
    private Cursor cursorQuest;
    private Cursor cursorImages;
    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private Context context;

    public SqlDatabaseHelper(Context context) {
        this.context=context;
        init();
    }
//Методы для работы с базой данных

    public Cursor getAllImagesEntries(int questionId)
    {
        String[] columnsToTake = { KEY_ID, KEY_IMAGE_SOURCE,KEY_QUESTION_ID };
        // составляем запрос к базе
        return database.query(TABLE_NAME_IMAGES, columnsToTake,
                "question_id="+questionId, null, null, null, KEY_ID);
    }
    public Cursor getAllQuestionEntries() {
        //Список колонок базы, которые следует включить в результат
        String[] columnsToTake = { KEY_ID, KEY_QUESTION,KEY_ANSWER };
        // составляем запрос к базе
        return database.query(TABLE_NAME_HISTORY, columnsToTake,
                null, null, null, null, KEY_ID);
    }
   public List<Question> getAllQuestion()
   {
       ArrayList<Question> questions = new ArrayList<>();
       ArrayList names = new ArrayList();
       cursorQuest.moveToFirst();
       if (!cursorQuest.isAfterLast()) {
           do {
               int id = cursorQuest.getInt(ID_COLUMN);
               String questionText = cursorQuest.getString(QUESTION_COLUMN);
               String anwerText = cursorQuest.getString(ANSWER_COLUMN);
               cursorImages= getAllImagesEntries(id);
               cursorImages.moveToFirst();
               if(!cursorImages.isAfterLast())
               {
                  String uri_image=cursorImages.getString(IMAGE_SOURCE_COLUMN);
               } while(cursorImages.moveToNext());
               questions.add(new Question(questionText,anwerText));
           } while (cursorQuest.moveToNext());
       }
       return  questions;
   }
    public long addItem(Question question) {
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestionText());
        values.put(KEY_ANSWER,question.getAnswerText());
        long id = database.insert(TABLE_NAME_HISTORY, null, values);
        refresh();
        return id;
    }

    public boolean removeItem(Question nameToRemove) {
        boolean isDeleted = (database.delete(TABLE_NAME_HISTORY, KEY_QUESTION + "=?",
                new String[] { nameToRemove.getQuestionText() })) > 0;
        refresh();
        return isDeleted;
    }

    public boolean updateItem(long id, String newQuestion, String newAnswer) {
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, newQuestion);
        values.put(KEY_ANSWER,newAnswer);
        boolean isUpdated = (database.update(TABLE_NAME_HISTORY, values, KEY_ID + "=?",
                new String[] {id+""})) > 0;
        return isUpdated;
    }

    //Прочие служебные методы

    public void onDestroy() {
        dbOpenHelper.close();
    }

    //Вызывает обновление вида
    private void refresh() {
        cursorQuest = getAllQuestionEntries();
    }

    // Инициализация адаптера: открываем базу и создаем курсор
    private void init() {
        dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VESION);
        try {
            database = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            // /Если база не открылась, то дальше нам дороги нет
            // но это особый случай
            Log.e(this.toString(), "Error while getting database");
            throw new Error("The end");
        }
        cursorQuest = getAllQuestionEntries();

    }


}
