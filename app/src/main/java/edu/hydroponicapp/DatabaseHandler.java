package edu.hydroponicapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DatabaseHandler {
    //    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    //    super(context, name, factory, version);
//    }
    DatabaseHelper dbHelper;
    public DatabaseHandler(Context context) {

        try
        {
            dbHelper = new DatabaseHelper(context);
            dbHelper.openDataBase();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public long insertData(String timestamp, String ph, String name)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TIMESTAMP, timestamp);
        contentValues.put(DatabaseHelper.PH, ph);
        contentValues.put(DatabaseHelper.UNIT_NAME, name);

        return db.insert(DatabaseHelper.TABLE_NAME, null , contentValues);
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        //ph, water sensor pump time,
        //potentially more sensors
        public SQLiteDatabase myDb;

        private static final String DATABASE_NAME = "HydroponicDB";
        private static final String TABLE_NAME = "sensorValues";
        private static final int DATABASE_Version = 1;
        private static final String TIMESTAMP = "timestamp";
        private static final String UNIT_NAME = "name";
        private static final String PH = "ph";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "( " + TIMESTAMP + " TEXT PRIMARY KEY, "
                + UNIT_NAME + " TEXT, " + PH + " TEXT);"
                ;
        // private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private final Context context;

        public void openDataBase() throws SQLException {
            //Open the database
            String myPath = "./././././assets";
            myDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            //System.out.println("success");
        }

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
           Message.message(context, "Started...");
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
                Message.message(context,"TABLE CREATED");

            } catch (Exception e) {
                Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          /* try {
               Message.message(context,"OnUpgrade");
               db.execSQL(DROP_TABLE);
               onCreate(db);
           }catch (Exception e) {
               Message.message(context,""+e);
                          }*/
        }

        //TODO: CREATE CRUD METHODS/FUNCTIONALITY
    }
}
