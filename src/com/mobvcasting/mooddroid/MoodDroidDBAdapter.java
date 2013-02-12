package com.mobvcasting.mooddroid;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple database access helper class. Defines the basic CRUD operations
 * for the MoodDroid application.
 * 
 */
public class MoodDroidDBAdapter 
{
	//http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/

	//The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.mobvcasting.mooddroid/databases/";

	public static final String MOOD_NAME = "mood_name";
    public static final String MOOD_VALUE = "mood_value";
    public static final String MOOD_ID = "_id";
    public static final String MOOD_DISPLAY = "display";

    public static final String UPDATE_ID = "_id";
    public static final String UPDATE_MOOD_ID = "mood_id";
    public static final String UPDATE_MOOD_TAG = "mood_tag";
    public static final String UPDATE_MOOD_LAT = "mood_lat";
    public static final String UPDATE_MOOD_LON = "mood_lon";
    public static final String UPDATE_TIMESTAMP = "ts";
    
    private static final String DATABASE_NAME = "mooddroid_db";
    private static final String MOODS_TABLE = "moods";
    private static final String MOODS_UPDATE_TABLE = "mood_updates"; 
    private static final int DATABASE_VERSION = 10;
    
    private static final String TAG = "MoodDroidDBAdapter";
    //private DatabaseHelper mDbHelper;
    private MoodDroidDBHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * Database creation sql statement
     */
    /*
    private static final String DATABASE_CREATE_1 =
            "create table moods (_id integer primary key autoincrement, "
                    + "mood_name text not null, mood_value int not null);";
    private static final String DATABASE_CREATE_2 = 
             "create table mood_updates (_id integer primary key autoincrement, "
                    + "mood_id int not null, mood_tag text null, "
                    + "mood_lat double null, mood_lon double null, "
                    + "ts datetime default current_timestamp);";
    private static final String DATABASE_CREATE_3 = 
    		"insert into moods (mood_name, mood_value) values ('Good',7);";
	*/
    private final Context mCtx;

    /*
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_1);
            db.execSQL(DATABASE_CREATE_2);
            db.execSQL(DATABASE_CREATE_3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS moods");
            db.execSQL("DROP TABLE IF EXISTS mood_updates");
            onCreate(db);
        }
    }
	*/
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public MoodDroidDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the mooddroid database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public MoodDroidDBAdapter open() throws SQLException 
    {
        //mDbHelper = new DatabaseHelper(mCtx);
    	mDbHelper = new MoodDroidDBHelper(mCtx,DATABASE_NAME, null, DATABASE_VERSION);
    	
    	try 
    	{
    		mDbHelper.createDataBase();
    		//mDbHelper.forceCreateDataBase();
      		Log.v("TAG","Create database");
    	} 
    	catch (IOException ioe) 
    	{
    		Log.v("TAG","Unable to create database");
    		throw new Error("Unable to create database");
    	}
  
    	try 
    	{
    		mDbHelper.openDataBase();
    		Log.v("TAG","Open database");
    	} 
    	catch(SQLException sqle)
    	{
    		Log.v("TAG","Unable to open database");
    		throw sqle;
    	}   	
    	
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() 
    {
        mDbHelper.close();
    }

    /**
     * Add a mood_update row using the mood_id (moods _id) value
     * 
     * @param mood_id the _id of the mood
     * @return rowId or -1 if failed
     */
    public long updateMood(int mood_id)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(UPDATE_MOOD_ID, mood_id);
    	
        return mDb.insert(MOODS_UPDATE_TABLE, null, initialValues);   	
    }
    
    /**
     * Add a mood_update row using the mood_id (moods _id) value and a tag
     * 
     * @param mood_id the _id of the mood
     * @param mood_tag the text of the mood_updates mood_tag field
     * @return rowId or -1 if fails 
     */
    public long updateMood(int mood_id, String mood_tag)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(UPDATE_MOOD_ID, mood_id);
    	initialValues.put(UPDATE_MOOD_TAG, mood_tag);
    	
    	return mDb.insert(MOODS_UPDATE_TABLE, null, initialValues);
    }

    //updateEditMood((int)id, display, value) != -1)
    /**
     * Update a mood row using the mood_id (moods _id) value and display
     * 
     * @param mood_id the _id of the mood
     * @param display the display value of the mood (0 or 1)
     * @param value the value of the mood (0 through 9)
     * @return rowId or -1 if fails 
     */
    public long updateEditMood(int mood_id, int display, int value)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(MOOD_DISPLAY, display);
    	initialValues.put(MOOD_VALUE, value);
    	
    	return mDb.update(MOODS_TABLE, initialValues, MOOD_ID + "=?", new String[] {""+mood_id});    	
    }
    
    /**
     * Add a mood_update row using the mood_id (moods _id) value and a tag and location lat, lon
     * 
     * @param mood_id the _id of the mood
     * @param mood_tag the text of the mood_updates mood_tag field
     * @param lat double 
     * @param lon double
     * @return rowId or -1 if fails 
     */
    public long updateMood(int mood_id, String mood_tag, double lat, double lon)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(UPDATE_MOOD_ID, mood_id);
    	initialValues.put(UPDATE_MOOD_TAG, mood_tag);
    	initialValues.put(UPDATE_MOOD_LAT, lat);
    	initialValues.put(UPDATE_MOOD_LON, lon);
    	
    	return mDb.insert(MOODS_UPDATE_TABLE, null, initialValues);
    }
   
    
    /**
     * Return a Cursor over the list of all mood updates in the database
     * 
     * @return Cursor over all moods updates
     */
    public Cursor fetchAllMoodUpdates() 
    {
    	String query = "";
    	
		query = "select " + MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_ID + ", " + 
					MOODS_UPDATE_TABLE + "." + UPDATE_TIMESTAMP + ", " + 
					MOODS_TABLE + "." + MOOD_NAME + ", " +
					MOODS_TABLE + "." + MOOD_VALUE + ", " +
					MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_TAG + ", " + 
					MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LAT + ", " +
					MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LON + 
					" from " + MOODS_UPDATE_TABLE + ", " + MOODS_TABLE + " where " + MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_ID + " = " + MOODS_TABLE + "." + MOOD_ID + ";";
    	
    	Log.w(TAG, query);    	
    	return mDb.rawQuery(query, new String[] {});
    }
       
    /**
     * Create a new mood using the name and value provided. If the mood is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param mood_name the name of the mood
     * @param mood_value the value of the note
     * @return rowId or -1 if failed
     */
    public long createMood(String mood_name, int mood_value) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(MOOD_NAME, mood_name);
        initialValues.put(MOOD_VALUE, mood_value);

        return mDb.insert(MOODS_TABLE, null, initialValues);
    }

    /**
     * Delete the mood with the given rowId
     * 
     * @param rowId id of mood to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteMood(long rowId) 
    {
        return mDb.delete(MOODS_TABLE, MOOD_ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all moods in the database
     * 
     * @return Cursor over all moods
     */
    public Cursor fetchAllMoods() 
    {
    	//public Cursor query (String table, String[] columns, 
    	//		String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
    	Log.w(TAG, "Should be getting all moods");
    	String query = "select " + MOOD_ID + " ," +  MOOD_NAME + " ," + MOOD_VALUE + 
		" from " + MOODS_TABLE + " order by " + MOOD_DISPLAY + " DESC, " + MOOD_VALUE + " ASC;";
    	Log.w(TAG, query);  
    	return mDb.rawQuery(query, new String[] {});

        //return mDb.query(MOODS_TABLE, new String[] {MOOD_ID, MOOD_NAME,
        //        MOOD_VALUE}, null, null, null, null, MOOD_VALUE);
    }
    
    /**
     * Return a Cursor over the list of moods to display
     * 
     * @return Cursor over all moods marked to display
     */
    public Cursor fetchDisplayMoods(int displayMode)
    {
    	/*
        private static final int DISPLAY_MODE_ALPHA = 0;
        private static final int DISPLAY_MODE_VALUE = 1;
        private static final int DISPLAY_MODE_FREQ = 2;
        */
    	
    	String query = "";
    	
    	if (displayMode == MoodDroid.DISPLAY_MODE_ALPHA)
    	{
    		query = "select " + MOOD_ID + " ," +  MOOD_NAME + " ," + MOOD_VALUE + 
    			" from " + MOODS_TABLE + " where " + MOOD_DISPLAY + " = 1 order by " + MOOD_NAME + ";";
    	}
    	else if (displayMode == MoodDroid.DISPLAY_MODE_VALUE_REVERSE)
    	{
    		query = "select " + MOOD_ID + " ," +  MOOD_NAME + " ," + MOOD_VALUE + 
    			" from " + MOODS_TABLE + " where " + MOOD_DISPLAY + " = 1 order by " + MOOD_VALUE + " DESC;";    		
    	}
    	else if (displayMode == MoodDroid.DISPLAY_MODE_VALUE)
    	{
    		query = "select " + MOOD_ID + " ," +  MOOD_NAME + " ," + MOOD_VALUE + 
    			" from " + MOODS_TABLE + " where " + MOOD_DISPLAY + " = 1 order by " + MOOD_VALUE + " ASC;";
    	}
    	/* MoodDroid.DISPLAY_MODE_FREQ */
    	else
    	{
    		query = "select " + MOOD_ID + " ," +  MOOD_NAME + " ," + MOOD_VALUE + 
				" from " + MOODS_TABLE + " where " + MOOD_DISPLAY + " = 1;";
    	}
    	    	
    	Log.w(TAG, query);    	
    	return mDb.rawQuery(query, new String[] {});
    }

    /**
     * Return a Cursor positioned at the mood that matches the given rowId
     * 
     * @param rowId id of mood to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchMood(long rowId) throws SQLException 
    {
        Cursor mCursor =
                mDb.query(true, MOODS_TABLE, new String[] {MOOD_ID,
                        MOOD_NAME, MOOD_VALUE, MOOD_DISPLAY}, MOOD_ID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateMood(long rowId, String mood_name, int mood_value) 
    {
        ContentValues args = new ContentValues();
        args.put(MOOD_NAME, mood_name);
        args.put(MOOD_VALUE, mood_value);

        return mDb.update(MOODS_TABLE, args, MOOD_ID + "=" + rowId, null) > 0;
    }
}
