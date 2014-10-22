package com.gmail.benshoe.paardrijlessen.db;

/**
 * This class will contain scripts to run when the database version is upgraded.
 * Created by ben on 8/23/14.
 */
public class MySQLiteUpgradeScript {
    public static final String ADD_COLUMN_HORSE_IMAGE = "ALTER TABLE " + MySQLiteHelper.TABLE_HORSE + " ADD COLUMN " + MySQLiteHelper.COLUMN_HORSE_IMAGE + " TEXT;";
    public static final String ADD_COLUMN_LESSON_GROUP = "ALTER TABLE " + MySQLiteHelper.TABLE_LESSON + " ADD COLUMN " + MySQLiteHelper.COLUMN_LESSON_GROUP + " INTEGER;";
    public static final String DEFAULT_LESSON_GROUP_VALUE = "UPDATE " + MySQLiteHelper.TABLE_LESSON +
            " SET " + MySQLiteHelper.COLUMN_LESSON_GROUP + " = 2 WHERE " + MySQLiteHelper.COLUMN_LESSON_GROUP + " = 0;";
}
