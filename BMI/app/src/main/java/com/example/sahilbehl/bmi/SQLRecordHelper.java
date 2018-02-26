package com.example.sahilbehl.bmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SQLRecordHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BMIFINALTEST5";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME1 = "RECORD";

    private static final String COLUMN_RECORD_ID = "record_id";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_BMI = "bmi";
    private static final String COLUMN_DATE = "date";

    public static final String TABLE_NAME2 = "USER";

    private String un;

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_HEALTHNUMBER = "user_healthnumber";
    private static final String COLUMN_USER_DOB = "user_dob";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    //====================================================================================

    private String CREATE_RECORD_TABLE = "CREATE TABLE " + TABLE_NAME1+ "("
            + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HEIGHT + " TEXT,"
            + COLUMN_WEIGHT + " TEXT," + COLUMN_BMI + " TEXT," + COLUMN_DATE + " TEXT," + COLUMN_USER_USERNAME + " TEXT"
            + ")";

    private String DROP_RECORD_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME1;

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME2 + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_USERNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT,"
            + COLUMN_USER_HEALTHNUMBER + " TEXT," + COLUMN_USER_DOB + " TEXT" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME2;

    //=========================================================================================

    public SQLRecordHelper(Context context)
    {
        super(context,DB_NAME,null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
    }

    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_RECORD_TABLE);
        onCreate(db);
    }

    public void onChangePassword(String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update USER SET user_password = " + "'" + password + "'");

    }
    //=========================================USER==================================================

    public boolean addUser(String username, String password, String name, String dob, String healthNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_DOB, dob);
        values.put(COLUMN_USER_HEALTHNUMBER, healthNumber);

        long r = db.insert(TABLE_NAME2, null, values);
        db.close();

        if (r == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUser(String uname)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("SQL","select * from " + TABLE_NAME2 + " where user_name == " + "'" + uname + "'");
        Cursor data = db.query(TABLE_NAME2, null, "user_username=?", new String[] { uname }, null, null, null);
        return data;
        /*
        String[] projections={ COLUMN_RECORD_ID,COLUMN_HEIGHT,COLUMN_WEIGHT,COLUMN_BMI};
        cursor= db.query(TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
        */
    }

    public boolean checkUser(String username){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(TABLE_NAME2,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkPassword(String username, String password){
        un = username;
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(TABLE_NAME2,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkUser(String username, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(TABLE_NAME2,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    //=========================================RECORD=================================================

    public boolean addRecord(String height, String weight, String bmi, String username){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        String s = formatter.format(today.getTime());
        System.out.print(s); //2016/11/16
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_BMI, bmi);
        values.put(COLUMN_DATE,s);
        values.put(COLUMN_USER_USERNAME, username);
        long r = db.insert(TABLE_NAME1, null, values);
        db.close();
        if (r == -1) {
            return false;
        } else {
            return true;
        }


    }

    public Cursor getRecords(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(TABLE_NAME1, null, "user_username=?", new String[] { username }, null, null, null);
        return data;
        /*
        String[] projections={ COLUMN_RECORD_ID,COLUMN_HEIGHT,COLUMN_WEIGHT,COLUMN_BMI};
        cursor= db.query(TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
        */
    }
//==============================================================================================
}
