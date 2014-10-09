package com.gmail.benshoe.paardrijlessen.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.benshoe.paardrijlessen.HorseType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 8/13/14.
 */
public class HorseDataSource {

        // Database fields
        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String[] allColumns = { MySQLiteHelper.COLUMN_HORSE_ID,
                MySQLiteHelper.COLUMN_HORSE_NAME, MySQLiteHelper.COLUMN_HORSE_TYPE, MySQLiteHelper.COLUMN_HORSE_IMAGE};

        public HorseDataSource(Context context) {
            dbHelper = new MySQLiteHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public Horse createHorse(String name, HorseType horseType, String horseImage) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_HORSE_NAME, name);
            values.put(MySQLiteHelper.COLUMN_HORSE_TYPE, horseType.getName());
            values.put(MySQLiteHelper.COLUMN_HORSE_IMAGE, horseImage);
            long insertId = database.insert(MySQLiteHelper.TABLE_HORSE, null,
                    values);
            Cursor cursor = database.query(MySQLiteHelper.TABLE_HORSE,
                    allColumns, MySQLiteHelper.COLUMN_HORSE_ID + " = " + insertId, null,
                    null, null, null);
            values.clear();
            cursor.moveToFirst();
            Horse newHorse = cursorToHorse(cursor);
            cursor.close();
            return newHorse;
        }

        public void deleteHorse(Horse horse) {
            long id = horse.getId();
            System.out.println("Horse deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_HORSE, MySQLiteHelper.COLUMN_HORSE_ID
                    + " = " + id, null);
        }

        public List<Horse> getAllHorses() {
            List<Horse> horses = new ArrayList<Horse>();

            Cursor cursor = database.query(MySQLiteHelper.TABLE_HORSE,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Horse horse = cursorToHorse(cursor);
                horses.add(horse);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return horses;
        }

        private Horse cursorToHorse(Cursor cursor) {
            Horse horse = new Horse();
            horse.setId(cursor.getLong(0));
            horse.setName(cursor.getString(1));
            if(cursor.getString(2) == null) {
                horse.setHorseType(HorseType.ONBEKEND);
            } else {
                horse.setHorseType(HorseType.fromString(cursor.getString(2)));
            }
            horse.setImage(cursor.getString(3));
            return horse;
        }

    public Horse getHorseByName(String horseName) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_HORSE, allColumns, MySQLiteHelper.COLUMN_HORSE_NAME + "='" + horseName +"'", null, null, null, null);
        cursor.moveToFirst();
        Horse horse = cursorToHorse(cursor);
        cursor.close();
        return horse;
    }

    public Horse getHorseById(long horseId) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_HORSE, allColumns, MySQLiteHelper.COLUMN_HORSE_ID + "='" + horseId +"'", null, null, null, null);
        cursor.moveToFirst();
        Horse horse = cursorToHorse(cursor);
        cursor.close();
        return horse;
    }

    public int updateHorse(Horse horse) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_HORSE_NAME, horse.getName());
        values.put(MySQLiteHelper.COLUMN_HORSE_TYPE, horse.getHorseType().getName());
        values.put(MySQLiteHelper.COLUMN_HORSE_IMAGE, horse.getImage());

        return database.update(MySQLiteHelper.TABLE_HORSE, values, MySQLiteHelper.COLUMN_HORSE_ID + "='" + horse.getId() + "'", null);
    }
}
