package com.cs360.jamiehorner.portfolio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class ProjectDBHandler extends SQLiteOpenHelper{

    // database name and version
    private static final int DB_VER = 1;
    private static final String DB_NAME = "ProjectDB.db";

    // table
    public static final String TABLE_PROJECT = "Project";

    // columns
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PROJECT_TITLE = "project_title";
    public static final String COLUMN_PROJECT_PATH = "project_path";
    public static final String COLUMN_PROJECT_STATUS = "project_status";

    // constructor
    public ProjectDBHandler(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, factory, DB_VER);
    }
    // This method creates the Dogs table when the DB is initialized.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROJECT_TABLE = "CREATE TABLE " +
            TABLE_PROJECT + "(" +
            COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY," +
            COLUMN_USER_ID + " INTEGER," +
            COLUMN_PROJECT_TITLE + " TEXT," +
            COLUMN_PROJECT_PATH + " TEXT," +
            COLUMN_PROJECT_STATUS + " TEXT" +")";
        db.execSQL(CREATE_PROJECT_TABLE);
    }
    // This method closes an open DB if a new one is created.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        onCreate(db);
    }
    // This method is used to add a com.cs360.jamiehorner.portfolio.Project record to the database.
    public void addProject(Project project) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROJECT_ID, project.getProjectID());
        values.put(COLUMN_USER_ID, project.getUserID());
        values.put(COLUMN_PROJECT_TITLE, project.getProject_title());
        values.put(COLUMN_PROJECT_PATH, project.getProject_path());
        values.put(COLUMN_PROJECT_STATUS, project.getProject_staus());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PROJECT, null, values);
        db.close();
    }
    // implements the search/find functionality
    public Project searchProject(String project_title) {
        String query = "SELECT * FROM " +
            TABLE_PROJECT + " WHERE " + COLUMN_PROJECT_TITLE +
            " = \"" + project_title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Project project = new Project();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            project.setProjectID(Integer.parseInt(cursor.getString(0)));
            project.setUserID(Integer.parseInt(cursor.getString(1)));
            project.setProject_title(cursor.getString(2));
            project.setProject_path(cursor.getString(3));
            project.setProject_status(cursor.getString(4));
            cursor.close();
        } else {
            project = null;
        }
        db.close();
        return project;
    }
    // implements the delete function
    public boolean deleteProject(String project_title) {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PROJECT +
            " WHERE " + COLUMN_PROJECT_TITLE + " = \"" + project_title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Project project = new Project();
        if (cursor.moveToFirst()) {
            project.setProjectID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PROJECT, COLUMN_PROJECT_ID + " = ?",
                new String[] { String.valueOf(project.getProjectID())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}

