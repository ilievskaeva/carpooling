
package com.example.carpooling_f;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RideHistoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CarPooling.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RIDE_HISTORY = "ride_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_VEHICLE_TYPE = "vehicle_type";
    private static final String COLUMN_START_LAT = "start_lat";

    private static final String COLUMN_START_LON = "start_lon";

    private static final String COLUMN_END_LAT = "end_lat";

    private static final String COLUMN_END_LON = "end_lon";
    private static final String COLUMN_DRIVER_EMAIL = "driver_email";
    private static final String COLUMN_PASSENGER_EMAIL = "passenger_email";

    public RideHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_RIDE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VEHICLE_TYPE + " TEXT, "
                + COLUMN_START_LAT + " TEXT, "
                + COLUMN_START_LON + " TEXT, "
                + COLUMN_END_LAT+" TEXT, "
                + COLUMN_END_LON+" TEXT, "
                + COLUMN_DRIVER_EMAIL + " TEXT, "
                + COLUMN_PASSENGER_EMAIL + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDE_HISTORY);
        onCreate(db);
    }

    public void addRideHistory(RideHistory rideHistory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_TYPE, rideHistory.getVehicleType());
        values.put(COLUMN_START_LAT, rideHistory.getStartLat());
        values.put(COLUMN_END_LAT, rideHistory.getEndLat());
        values.put(COLUMN_START_LON, rideHistory.getStartlon());
        values.put(COLUMN_END_LON, rideHistory.getEndlon());
        values.put(COLUMN_DRIVER_EMAIL, rideHistory.getDriverEmail());
        values.put(COLUMN_PASSENGER_EMAIL, rideHistory.getPassengerEmail());

        db.insert(TABLE_RIDE_HISTORY, null, values);
        db.close();
    }

    public List<RideHistory> getRidesByPassenger(String passengerEmail) {
        List<RideHistory> rideHistoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RIDE_HISTORY, null, COLUMN_PASSENGER_EMAIL + " = ?", new String[]{passengerEmail}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String vehicleType = cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_TYPE));
                @SuppressLint("Range") String startLon = cursor.getString(cursor.getColumnIndex(COLUMN_START_LON));
                @SuppressLint("Range") String startLat = cursor.getString(cursor.getColumnIndex(COLUMN_START_LAT));
                @SuppressLint("Range") String endLat = cursor.getString(cursor.getColumnIndex(COLUMN_END_LAT));
                @SuppressLint("Range") String endLon = cursor.getString(cursor.getColumnIndex(COLUMN_END_LON));
                @SuppressLint("Range") String driverEmail = cursor.getString(cursor.getColumnIndex(COLUMN_DRIVER_EMAIL));

                RideHistory rideHistory = new RideHistory(id, vehicleType, startLat, startLon, endLat, endLon, driverEmail, passengerEmail);
                rideHistoryList.add(rideHistory);
            }
            cursor.close();
        }

        db.close();
        return rideHistoryList;
    }
}