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
    private static final String TABLE_NAME = "history";
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
    private Cursor cursor;
    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private Context context;

    public SqlDatabaseHelper(Context context) {
        this.context=context;
        init();
    }
//Методы для работы с базой данных

    public Cursor getAllEntries() {
        //Список колонок базы, которые следует включить в результат
        String[] columnsToTake = { KEY_ID, KEY_QUESTION,KEY_ANSWER };
        // составляем запрос к базе
        return database.query(TABLE_NAME, columnsToTake,
                null, null, null, null, KEY_ID);
    }
   public List<Question> getAllQuestion()
   {
       ArrayList<Question> questions = new ArrayList<>();
       ArrayList names = new ArrayList();
       cursor.moveToFirst();
       if (!cursor.isAfterLast()) {
           do {
               long id = cursor.getLong(ID_COLUMN);
               String questionText = cursor.getString(QUESTION_COLUMN);
               String anwerText = cursor.getString(ANSWER_COLUMN);
               questions.add(new Question(questionText,anwerText));
           } while (cursor.moveToNext());
       }
       return  questions;
   }
    public long addItem(Question question) {
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, question.getQuestionText());
        values.put(KEY_ANSWER,question.getAnswerText());
        long id = database.insert(TABLE_NAME, null, values);
        refresh();
        return id;
    }

    public boolean removeItem(Question nameToRemove) {
        boolean isDeleted = (database.delete(TABLE_NAME, KEY_QUESTION + "=?",
                new String[] { nameToRemove.getQuestionText() })) > 0;
        refresh();
        return isDeleted;
    }

    public boolean updateItem(long id, String newQuestion, String newAnswer) {
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, newQuestion);
        values.put(KEY_ANSWER,newAnswer);
        boolean isUpdated = (database.update(TABLE_NAME, values, KEY_ID + "=?",
                new String[] {id+""})) > 0;
        return isUpdated;
    }

    //Прочие служебные методы

    public void onDestroy() {
        dbOpenHelper.close();
    }

    //Вызывает обновление вида
    private void refresh() {
        cursor = getAllEntries();
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
        cursor = getAllEntries();
    }


}
