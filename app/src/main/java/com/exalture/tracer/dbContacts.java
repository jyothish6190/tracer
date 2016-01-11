package com.exalture.tracer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ConatctDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String PREF_NUMBER = "PREF_NUMBER";
    public static final String PREF_NUMBER_ID = "PREF_NUMBER_ID";
    public static final String NUMBER = "NUMBER";
    public static final String NAME = "NAME";
    public static final String PRIMARY_NUMBER = "PRIMARY_NUMBER";

    private static final String DATABASE_CREATE_PREF_NUMBER = "create table "
            + PREF_NUMBER + "(" + PREF_NUMBER_ID
            + " integer primary key autoincrement, " + NUMBER
            + " text not null," + NAME + " text not null, " + PRIMARY_NUMBER + " integer);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_PREF_NUMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + PREF_NUMBER);
        onCreate(db);
    }
}

class Pref_Number {
    private int id;
    private String number;
    private String name;

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    private int primary;

    public int getId() {
        return id;
    }

    public void setId(int new_id) {
        this.id = new_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class dbContacts {
    private SQLiteDatabase database;

    private MySQLiteHelper dbHelper;

    public dbContacts(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }

    public long addNumber(String number_new, String name, int primary) {
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.NUMBER, number_new);
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.PRIMARY_NUMBER, primary);

        long insert_id = database.insert(MySQLiteHelper.PREF_NUMBER, null, values);
        return insert_id;
    }

    public boolean deleteNumber(String number_new) {
        return database.delete(MySQLiteHelper.PREF_NUMBER, MySQLiteHelper.NUMBER + "=?", new String[]{number_new}) > 0;
    }

    public long updateNumber(String number, String name, int primary){
        ContentValues values = new ContentValues();

        values.put(MySQLiteHelper.NUMBER, number);
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.PRIMARY_NUMBER, primary);
        return database.update(MySQLiteHelper.PREF_NUMBER, values, MySQLiteHelper.NUMBER + "=?", new String[]{number});
    }

    public List<Pref_Number> getContactLists() {
        String query1 = "SELECT * FROM PREF_NUMBER";
        List<Pref_Number> result = new ArrayList<Pref_Number>();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query1, null);
            Pref_Number num = null;
            int size = cursor.getCount();
            int i = 0;
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                    num = new Pref_Number();
                    num.setId(Integer.parseInt(cursor.getString(0)));
                    num.setNumber(cursor.getString(1));
                    num.setName(cursor.getString(2));
                    num.setPrimary(cursor.getInt(3));
                    result.add(num);
                    cursor.moveToNext();
                    i++;
                } while (i < size);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return result;
    }

    public boolean isExist(String number) {
        boolean result = false;
        String query1 = "SELECT * FROM PREF_NUMBER";
        Cursor cursor = database.rawQuery(query1, null);
        Pref_Number num = null;
        int size = cursor.getCount();
        int i = 0;
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(1).equals(number)) {
                    result = true;
                    break;
                }
                cursor.moveToNext();
                i++;
            } while (i < size);
        }
        cursor = null;
        return result;
    }
}

