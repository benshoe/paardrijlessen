package com.gmail.benshoe.paardrijlessen.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ben on 8/13/14.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_HORSE = "horse";
    public static final String COLUMN_HORSE_ID = "_id";
    public static final String COLUMN_HORSE_NAME = "name";
    public static final String COLUMN_HORSE_TYPE = "type";
    public static final String COLUMN_HORSE_IMAGE = "image_location";

    public static final String TABLE_LESSON = "lesson";
    public static final String COLUMN_LESSON_ID = "_id";
    public static final String COLUMN_LESSON_DATE = "date";
    public static final String COLUMN_LESSON_HORSE_ID = "horse_id";
    public static final String COLUMN_LESSON_GRADE = "grade";
    public static final String COLUMN_LESSON_GROUP = "lesson_group";
    public static final String COLUMN_LESSON_DESCRIPTION = "description";

    private static final String DATABASE_NAME = "paardrijlessen.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String CREATE_HORSE_TABLE = "create table "
            + TABLE_HORSE + "(" + COLUMN_HORSE_ID
            + " integer primary key autoincrement, "
            + COLUMN_HORSE_NAME + " text not null, "
            + COLUMN_HORSE_TYPE + " text null, "
            + COLUMN_HORSE_IMAGE + " text null); ";
    private static final String CREATE_LESSON_TABLE = "create table "
            + TABLE_LESSON + "("
            + COLUMN_LESSON_ID + " integer primary key autoincrement, "
            + COLUMN_LESSON_DATE + " integer not null, "
            + COLUMN_LESSON_DESCRIPTION + " text not null, "
            + COLUMN_LESSON_GRADE + " integer not null, "
            + COLUMN_LESSON_GROUP + " integer not null, "
            + COLUMN_LESSON_HORSE_ID + " integer, foreign key (" + COLUMN_LESSON_HORSE_ID + ") references " + TABLE_HORSE + "(" + COLUMN_HORSE_ID + ") "
            + ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(CREATE_HORSE_TABLE);
        database.execSQL(CREATE_LESSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will keep all old data");
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion)
        {
            switch (upgradeTo)
            {
                case 1:
                    break;
                case 2:
                    try {
                        db.execSQL(MySQLiteUpgradeScript.ADD_COLUMN_HORSE_IMAGE);
                    } catch(SQLiteException exception) {
                        if(exception.getMessage().contains("duplicate column name")) {
                            break;
                        }
                        System.out.println("the message is not duplicate column name");
                        throw exception;
                    }
                    break;
                case 4:
                    try {
                        db.execSQL(MySQLiteUpgradeScript.ADD_COLUMN_LESSON_GROUP);
                        db.execSQL(MySQLiteUpgradeScript.DEFAULT_LESSON_GROUP_VALUE);
                    } catch (SQLiteException exception) {
                        if(exception.getMessage().contains("duplicate column name")) {
                            break;
                        }
                        System.out.println("the message is not duplicate column name");
                        throw exception;
                    }
            }
            upgradeTo++;
        }
    }

}
