package com.example.carpooling_f;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PassengerDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "passenger.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "passengers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CURRENT_LAT = "current_lat";
    public static final String COLUMN_CURRENT_LNG = "current_lng";
    public static final String COLUMN_DEST_LAT = "dest_lat";
    public static final String COLUMN_DEST_LNG = "dest_lng";

    private static final String COLUMN_PASSENGER_RATING_SUM = "passenger_rating_sum";
    private static final String COLUMN_PASSENGER_RATING_COUNT = "passenger_rating_count";

    public PassengerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CURRENT_LAT + " REAL, " +
                COLUMN_CURRENT_LNG + " REAL, " +
                COLUMN_DEST_LAT + " REAL, " +
                COLUMN_DEST_LNG + " REAL,"+
                COLUMN_PASSENGER_RATING_SUM+ " REAL DEFAULT 0,"+
                COLUMN_PASSENGER_RATING_COUNT+" INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveLocations(String email, double currentLat, double currentLng, double destLat, double destLng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CURRENT_LAT, currentLat);
        values.put(COLUMN_CURRENT_LNG, currentLng);
        values.put(COLUMN_DEST_LAT, destLat);
        values.put(COLUMN_DEST_LNG, destLng);
        db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[]{email});
        db.close();
    }


    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    @SuppressLint("Range")
    public double[] getPassengerCoordinates(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_CURRENT_LAT, COLUMN_CURRENT_LNG, COLUMN_DEST_LAT, COLUMN_DEST_LNG},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            double[] coordinates = new double[4];
            coordinates[0] = cursor.getDouble(cursor.getColumnIndex(COLUMN_CURRENT_LAT));
            coordinates[1] = cursor.getDouble(cursor.getColumnIndex(COLUMN_CURRENT_LNG));
            coordinates[2] = cursor.getDouble(cursor.getColumnIndex(COLUMN_DEST_LAT));
            coordinates[3] = cursor.getDouble(cursor.getColumnIndex(COLUMN_DEST_LNG));
            cursor.close();
            return coordinates;
        } else {
            return new double[]{0.0, 0.0, 0.0, 0.0};
        }
    }


    public void updatePassengerRating(String passengerEmail, String driverEmail, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("passenger.db", new String[]{COLUMN_PASSENGER_RATING_SUM, COLUMN_PASSENGER_RATING_COUNT},
                COLUMN_EMAIL + " = ? AND driver_email = ?", new String[]{passengerEmail,driverEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") float currentRatingSum = cursor.getFloat(cursor.getColumnIndex(COLUMN_PASSENGER_RATING_SUM));
            @SuppressLint("Range") int currentRatingCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_RATING_COUNT));

            float newRatingSum = currentRatingSum + rating;
            int newRatingCount = currentRatingCount + 1;

            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSENGER_RATING_SUM, newRatingSum);
            values.put(COLUMN_PASSENGER_RATING_COUNT, newRatingCount);

            db.update("passenger.db", values, COLUMN_EMAIL + " = ? AND driver_email = ?", new String[]{passengerEmail, driverEmail});
        } else {

            ContentValues values = new ContentValues();
            values.put(COLUMN_PASSENGER_RATING_SUM, rating);
            values.put(COLUMN_PASSENGER_RATING_COUNT, 1);
            db.insert("passenger.db", null, values);
        }
        cursor.close();
        db.close();
    }


    public float getPassengerRating(String passengerEmail, String driverEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("passenger.db", new String[]{COLUMN_PASSENGER_RATING_SUM, COLUMN_PASSENGER_RATING_COUNT},
                COLUMN_EMAIL + " = ? AND driver_email = ?", new String[]{passengerEmail, driverEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") float ratingSum = cursor.getFloat(cursor.getColumnIndex(COLUMN_PASSENGER_RATING_SUM));
            @SuppressLint("Range") int ratingCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PASSENGER_RATING_COUNT));

            if (ratingCount == 0) {
                return 0;
            }
            return ratingSum / ratingCount;
        }

        cursor.close();
        db.close();
        return 0;
    }

}
