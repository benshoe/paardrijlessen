package com.gmail.benshoe.paardrijlessen.db;

/**
 * This class will contain scripts to run when the database version is upgraded.
 * Created by ben on 8/23/14.
 */
public class MySQLiteUpgradeScript {
    public static final String ADD_COLUMN_HORSE_IMAGE = "ALTER TABLE " + MySQLiteHelper.TABLE_HORSE + " ADD COLUMN " + MySQLiteHelper.COLUMN_HORSE_IMAGE + " TEXT;";
}
