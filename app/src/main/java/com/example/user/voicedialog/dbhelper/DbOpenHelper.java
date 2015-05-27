package com.example.user.voicedialog.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bunis on 13.05.2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper {


    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_IMAGES = "images";
    private static final String KEY_ID = "_id";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_IMAGE_SOURCE = "image_source";
    private static final String KEY_QUESTION_ID = "question_id";


    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_DB_HISTORY = "CREATE TABLE " + TABLE_HISTORY + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_QUESTION + " TEXT NOT NULL,"
                + KEY_ANSWER + " TEXT NOT NULL);";
        final String CREATE_DB_IMAGES = "CREATE TABLE " + TABLE_IMAGES + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_IMAGE_SOURCE + " TEXT NOT NULL,"
                + KEY_QUESTION_ID + " INTEGER NOT NULL);";
        db.execSQL(CREATE_DB_HISTORY);
        db.execSQL(CREATE_DB_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_IMAGES);
        onCreate(db);
    }
}
