package com.example.refocus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

public class AppDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "app_database";
    public static final String APP_TABLE_NAME = "apps";
    public static final String APP_COLUMN_NAME = "name";
    public static final String APP_COLUMN_CATEGORY = "category";
    public static final String APP_COLUMN_HOURS = "hours";
    public static final String APP_COLUMN_MINUTES = "minutes";
    public static final String APP_COLUMN_USAGEHOURS = "usage_hours";
    public static final String APP_COLUMN_USAGEMINUTES = "usage_minutes";

    String name;
    String package_name;
    String category;
    Drawable icon;
    String hours;
    String minutes;
    String usage_hours;
    String usage_minutes;

    public AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + APP_TABLE_NAME + " (" +
                APP_COLUMN_CATEGORY + "TEXT, " +
                APP_COLUMN_HOURS + " TEXT, " +
                APP_COLUMN_MINUTES + " TEXT, " +
                APP_COLUMN_USAGEHOURS + " TEXT" +
                APP_COLUMN_USAGEMINUTES + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + APP_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

