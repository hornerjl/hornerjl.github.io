package com.cs360.jamiehorner.portfolio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class UserDBHandler extends SQLiteOpenHelper{

    // database name and version
    private static final int DB_VER = 1;
    private static final String DB_NAME = "UserDB.db";

    // table
    public static final String TABLE_USERS = "Users";

    // columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_EMAIL_ADDRESS = "email_address";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_MIDDLE_INITIAL = "middle_initial";
    public static final String COLUMN_JOB_TITLE = "job_title";
    public static final String COLUMN_FREELANCER_ADDRESS = "freelancer_address";
    public static final String COLUMN_TWITTER_ADDRESS = "twitter_address";
    public static final String COLUMN_FACEBOOK_ADDRESS = "facebook_address";
    public static final String COLUMN_LINKEDIN_ADDRESS = "linkedin_address";
    public static final String COLUMN_RESUME_IMPORT_PATH = "resume_import_path";
    public static final String COLUMN_USER_IMAGE_PATH = "user_image_path";

    // constructor
    public UserDBHandler(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, factory, DB_VER);
    }
    // This method creates the Dogs table when the DB is initialized.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " +
            TABLE_USERS + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
            COLUMN_PHONE_NUMBER + " TEXT," +
            COLUMN_EMAIL_ADDRESS + " TEXT," +
            COLUMN_FIRST_NAME + " TEXT," +
            COLUMN_LAST_NAME + " TEXT," +
            COLUMN_MIDDLE_INITIAL + " TEXT," +
            COLUMN_JOB_TITLE + " TEXT," +
            COLUMN_FREELANCER_ADDRESS + " TEXT," +
            COLUMN_TWITTER_ADDRESS + " TEXT," +
            COLUMN_FACEBOOK_ADDRESS + " TEXT," +
            COLUMN_LINKEDIN_ADDRESS + " TEXT," +
            COLUMN_RESUME_IMPORT_PATH + " TEXT," +
            COLUMN_USER_IMAGE_PATH + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }
    // This method closes an open DB if a new one is created.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    // This method is used to add a com.cs360.jamiehorner.portfolio.User record to the database.
    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
        values.put(COLUMN_EMAIL_ADDRESS, user.getEmailAddress());
        values.put(COLUMN_FIRST_NAME, user.getFirstName());
        values.put(COLUMN_LAST_NAME, user.getLastName());
        values.put(COLUMN_MIDDLE_INITIAL, user.getMiddleInitial());
        values.put(COLUMN_JOB_TITLE, user.getJobTitle());
        values.put(COLUMN_FREELANCER_ADDRESS, user.getFreelancerAddress());
        values.put(COLUMN_TWITTER_ADDRESS, user.getTwitterAddress());
        values.put(COLUMN_FACEBOOK_ADDRESS, user.getFacebookAddress());
        values.put(COLUMN_LINKEDIN_ADDRESS, user.getLinkedinAddress());
        values.put(COLUMN_RESUME_IMPORT_PATH, user.getResumeImportPath());
        values.put(COLUMN_USER_IMAGE_PATH, user.getUserImagePath());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }
    // implements the search/find functionality
    public User searchUser(String username) {
        String query = "SELECT * FROM " +
            TABLE_USERS + " WHERE " + COLUMN_EMAIL_ADDRESS +
            " = \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setUserID(Integer.parseInt(cursor.getString(0)));
            user.setPhoneNumber(cursor.getString(1));
            user.setEmailAddress(cursor.getString(2));
            user.setFirstName(cursor.getString(3));
            user.setLastName(cursor.getString(4));
            user.setMiddleInitial(cursor.getString(5));
            user.setJobTitle(cursor.getString(6));
            user.setFreelancerAddress(cursor.getString(7));
            user.setTwitterAddress(cursor.getString(8));
            user.setFacebookAddress(cursor.getString(9));
            user.setLinkedinAddress(cursor.getString(10));
            user.setResumeImportPath(cursor.getString(11));
            user.setUserImagePath(cursor.getString(12));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }
    // implements the delete dog functionality
    public boolean deleteUser(String username) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_USERS +
            " WHERE " + COLUMN_EMAIL_ADDRESS + " = \"" + username + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = new User();
        if (cursor.moveToFirst()) {
            user.setUserID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserID())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
