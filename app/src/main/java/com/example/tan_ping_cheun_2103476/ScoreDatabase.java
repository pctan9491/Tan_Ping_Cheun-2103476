package com.example.tan_ping_cheun_2103476;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class ScoreDatabase {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "score_table";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    private static final String SCRIPT_CREATE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_SCORE + " INTEGER NOT NULL)";

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public ScoreDatabase(Context context){
        helper = new SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SCRIPT_CREATE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        db = helper.getWritableDatabase();
        this.db = db;
    }

    public void addScore(ScoreInfo score){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,score.getName());
        cv.put(COLUMN_SCORE, score.getScore());
        db.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<ScoreInfo> getTop25() {
        ArrayList<ScoreInfo> scoreInfo = new ArrayList<>();
        String query = "SELECT name, score FROM score_table ORDER BY score DESC LIMIT 25";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
                ScoreInfo scores = new ScoreInfo(name, score);
                scoreInfo.add(scores);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scoreInfo;
    }




}

