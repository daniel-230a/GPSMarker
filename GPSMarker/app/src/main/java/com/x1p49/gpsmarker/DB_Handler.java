package com.x1p49.gpsmarker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DB_Handler extends SQLiteOpenHelper {

    private static final String DB_NAME = "GPS_locations";
    private static final String TBL_NAME = "TBL_LOCATIONS";

    public DB_Handler(Context context) {

        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TBL_NAME + "("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "DESCRIPTION TEXT, "
                + "LONGITUDE TEXT, "
                + "LATITUDE TEXT, "
                + "ALTITUDE TEXT);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertLocation(String name, String description, String longitude, String latitude, String altitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues recordValues = new ContentValues();

        recordValues.put("NAME", name);
        recordValues.put("DESCRIPTION", description);
        recordValues.put("LONGITUDE", longitude);
        recordValues.put("LATITUDE", latitude);
        recordValues.put("ALTITUDE", altitude);

        long new_location = db.insert(TBL_NAME, null, recordValues);

        if (new_location == -1) {
            return false;
        } else {
            return true;
        }
    }

    public  ArrayList<String> getLocationByID(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ TBL_NAME + " WHERE ID=" + id, null);
        //Cursor first_ID = db.rawQuery("SELECT ID FROM "+ TBL_NAME, null);

        ArrayList<String> list_item = new ArrayList<String>();

        if(cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                for (int i=0; i < cursor.getColumnCount(); i++) {

                    list_item.add(cursor.getString(i));
                }

                cursor.moveToNext();
            }

            if(cursor != null) {
                cursor.close();
            }

        }

        return list_item;
    }

    public Object[] getListView() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TBL_NAME, null, "ID", null, null, null,"ID DESC");

        Object[] return_list_view = new Object[2];
        ArrayList<String> list_view = new ArrayList<String>();
        ArrayList<String> list_view_ids = new ArrayList<String>();

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                list_view.add(cursor.getString(1));
                list_view_ids.add(cursor.getString(0));

                cursor.moveToNext();
            }

            if(cursor != null) {
                cursor.close();
            }


        }

        return_list_view[0] = list_view_ids;
        return_list_view[1] = list_view;

        return return_list_view;
    }

    public void updateLocation(String id, String name, String description) {

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues updateValues = new ContentValues();

        updateValues.put("NAME", name);
        updateValues.put("DESCRIPTION", description);

        db.update(TBL_NAME, updateValues, "ID=" + id, null);
    }

    public void deleteLocation(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TBL_NAME, "ID=" + id, null);
    }

    public void deleteAllLocations() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TBL_NAME, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TBL_NAME + "'");
    }

}
