package com.gmail.benshoe.paardrijlessen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ben on 8/18/14.
 */
public class LessonDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_LESSON_ID,
            MySQLiteHelper.COLUMN_LESSON_DATE,
            MySQLiteHelper.COLUMN_LESSON_DESCRIPTION,
            MySQLiteHelper.COLUMN_LESSON_HORSE_ID,
            MySQLiteHelper.COLUMN_LESSON_GRADE
    };

    public LessonDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Lesson> getAllLessons() {
        List<Lesson> lessons = new ArrayList<Lesson>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lesson lesson = cursorToLesson(cursor);
            lessons.add(lesson);
            cursor.moveToNext();
        }
        cursor.close();
        return lessons;
    }

    public void deleteLesson(Lesson lesson) {
        long id = lesson.getId();
        System.out.println("Lesson deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_LESSON, MySQLiteHelper.COLUMN_LESSON_ID
                + " = " + id, null);
    }

    public Lesson createLesson(Date date, String description, int horse_id, long lesson_grade) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LESSON_DATE, date.getTime());
        values.put(MySQLiteHelper.COLUMN_LESSON_DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_LESSON_HORSE_ID, horse_id);
        values.put(MySQLiteHelper.COLUMN_LESSON_GRADE, lesson_grade);
        long insertId = database.insert(MySQLiteHelper.TABLE_LESSON, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON, allColumns, MySQLiteHelper.COLUMN_LESSON_ID + "=" + insertId, null, null, null, null);
        values.clear();
        cursor.moveToFirst();
        Lesson newLesson = cursorToLesson(cursor);
        cursor.close();
        return newLesson;
    }

    private Lesson cursorToLesson(Cursor cursor) {
        Lesson lesson = new Lesson();
        lesson.setId(cursor.getLong(0));
        lesson.setDate(cursor.getLong(1));
        lesson.setDescription(cursor.getString(2));
        lesson.setHorse(cursor.getLong(3));
        lesson.setGrade(cursor.getLong(4));
        return lesson;
    }

    public Date stringToDate(String dateString) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = format.parse(dateString);
        return date;
    }

    public void updateLesson(Lesson lesson) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_LESSON_DESCRIPTION, lesson.getDescription());
        contentValues.put(MySQLiteHelper.COLUMN_LESSON_GRADE, lesson.getGrade());
        database.update(MySQLiteHelper.TABLE_LESSON, contentValues, MySQLiteHelper.COLUMN_LESSON_ID + "=" + lesson.getId(), null);
        contentValues.clear();
    }

    public List<Lesson> getLessonsByHorse(Horse horse) {
        open();
        List<Lesson> lessons = new ArrayList<Lesson>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_LESSON, allColumns, MySQLiteHelper.COLUMN_LESSON_HORSE_ID + "=" + horse.getId(), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lesson lesson = cursorToLesson(cursor);
            lessons.add(lesson);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return lessons;
    }
}
