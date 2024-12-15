package com.example.carpooling_f;

import static com.example.carpooling_f.PassengerDBHelper.COLUMN_EMAIL;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class DriverDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CarPooling.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "drivers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_DRIVING_TIME = "driving_time";

    private static final String COLUMN_DRIVING_TIME2 = "driving_time2";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_VEHICLE_TYPE = "vehicle_type";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    private static final String COLUMN_RATING_SUM = "rating_sum";
    private static final String COLUMN_RATING_COUNT = "rating_count";

    public DriverDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_DRIVING_TIME + " TEXT, "
                + COLUMN_DRIVING_TIME2 + " TEXT, "
                + COLUMN_PRICE + " TEXT, "
                + COLUMN_VEHICLE_TYPE + " TEXT, "
                + COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGITUDE + " REAL,"
                + COLUMN_RATING_SUM + " REAL DEFAULT 0, "
                + COLUMN_RATING_COUNT + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DRIVING_TIME + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_DRIVING_TIME2 + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PRICE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_VEHICLE_TYPE + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_LATITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_LONGITUDE + " REAL");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_RATING_SUM + " REAL DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_RATING_COUNT + " INTEGER DEFAULT 0");
        }
    }

    public void updateDriverRating(String driverEmail, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_RATING_SUM, COLUMN_RATING_COUNT},
                COLUMN_EMAIL + " = ?", new String[]{driverEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") float currentRatingSum = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING_SUM));
            @SuppressLint("Range") int currentRatingCount = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING_COUNT));

            float newRatingSum = currentRatingSum + rating;
            int newRatingCount = currentRatingCount + 1;

            ContentValues values = new ContentValues();
            values.put(COLUMN_RATING_SUM, newRatingSum);
            values.put(COLUMN_RATING_COUNT, newRatingCount);

            db.update(TABLE_NAME, values, COLUMN_EMAIL + " = ?", new String[]{driverEmail});
        }
        cursor.close();
        db.close();
    }

    public float getDriverRating(String driverEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_RATING_SUM, COLUMN_RATING_COUNT},
                COLUMN_EMAIL + " = ?", new String[]{driverEmail}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") float ratingSum = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING_SUM));
            @SuppressLint("Range") int ratingCount = cursor.getInt(cursor.getColumnIndex(COLUMN_RATING_COUNT));

            if (ratingCount == 0) {
                return 0;
            }
            return ratingSum / ratingCount;
        }

        cursor.close();
        db.close();
        return 0;
    }


    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_EMAIL}, COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean loginDriver(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_EMAIL, COLUMN_PASSWORD}, COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password}, null, null, null);

        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isLoggedIn;
    }
    public boolean updateDriverInfo(String email, String drivingTime, String drivingTime2, String price, String vehicleType, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVING_TIME, drivingTime);
        values.put(COLUMN_DRIVING_TIME2, drivingTime2);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_VEHICLE_TYPE, vehicleType);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();

        return rowsAffected > 0;
    }
    public Cursor getAllDrivers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    public boolean addDriver(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    public List<Vehicle> getVehiclesNearby(double userLat, double userLng) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Vehicle> nearbyVehicles = new ArrayList<>();

        double tolerance = 0.1;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_LATITUDE + " BETWEEN ? AND ? AND " +
                COLUMN_LONGITUDE + " BETWEEN ? AND ?";

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(userLat - tolerance),
                String.valueOf(userLat + tolerance),
                String.valueOf(userLng - tolerance),
                String.valueOf(userLng + tolerance)
        });

        if (cursor != null) {
            while (cursor.moveToNext()) {

                @SuppressLint("Range") String drivermail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String vehicleType = cursor.getString(cursor.getColumnIndex(COLUMN_VEHICLE_TYPE));
                @SuppressLint("Range") double lat = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") double lng = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                @SuppressLint("Range") float rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRICE));

                if (Math.abs(lat - userLat) < tolerance && Math.abs(lng - userLng) < tolerance) {

                    Vehicle vehicle = new Vehicle(drivermail, vehicleType, price, lat, lng, rating);
                    nearbyVehicles.add(vehicle);
                }
            }
            cursor.close();
        }

        db.close();
        return nearbyVehicles;
    }


}
