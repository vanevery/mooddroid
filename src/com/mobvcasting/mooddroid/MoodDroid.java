/*
 * MoodDroid 2.0 Android Mood Tracker
 * 
 * To Do:
 * 	Nicer Looking Graphs - Maybe use External Library
 * 		GraphView or others (in delicious)
 * 
 *  Graphical Splash Screen - Create Graphics - Icon - Karen Working on it
 *  
 *  	Better Interface Design
 *  		Larger Mood Update Buttons
 * 
 *  	Edit Moods that Display - Preferences? - View? - User Driven
 *  
 *  Service/Timer Development, Ask Periodically - Interface?
 *  
 *  Export/Email CSV File of Mood Updates
 *  
 *  Release as Lite Version 1.0
 *  
 *  Present it
 *  Determine if it downloads
 *  
 * 	Cleanup Database Code
 * 		Make DBHelper and DBAdapter work together better and deal with upgrades correctly
 * 
 * 			Populate Moods Database - Use MoodDroid DB on Desktop - SQL Lite Database - Loading this Database seems to be a pain
 * 				http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
 *  
 *  Create online Presence, Database
 *  Web Service, Website
 */

package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MoodDroid extends Activity 
{
	// Activity Tracking
    private static final int ACTIVITY_UPDATE = 0;
    private static final int ACTIVITY_VIEW = 1;	
    private static final int ACTIVITY_EDIT = 2;
    private static final int ACTIVITY_SERVICE = 3;
    private static final int ACTIVITY_SETTINGS = 4;
    private static final int ACTIVITY_ABOUT = 5;
    
    // Menu Item Tracking
    private static final int MENU_EDIT = 0;
    private static final int MENU_VIEW = 1;
    private static final int MENU_SERVICE = 2;
    private static final int MENU_SETTINGS = 3;
    private static final int MENU_ABOUT = 4;
    
    // Display Modes
    public static final int DISPLAY_MODE_ALPHA = 0;
    public static final int DISPLAY_MODE_VALUE = 1;
    public static final int DISPLAY_MODE_VALUE_REVERSE = 2;
    //public static final int DISPLAY_MODE_FREQ = 3;
    
    public static final String DISPLAY_MODE_KEY = "displayMode";
    
	private MoodDroidDBAdapter mDbHelper;
	private ListView moodsListView;
	
	public static final String SETTINGS_FILE = "MoodDroidSettings";
		
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
     

        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE);
        //SharedPreferences.Editor editor = settings.edit();
        //editor.putString
        
        int displayMode = settings.getInt(DISPLAY_MODE_KEY, DISPLAY_MODE_VALUE_REVERSE);
        
        mDbHelper = new MoodDroidDBAdapter(this);
        mDbHelper.open();
        Cursor c = mDbHelper.fetchDisplayMoods(displayMode);
        startManagingCursor(c);
        String[] from = new String[] { MoodDroidDBAdapter.MOOD_NAME, MoodDroidDBAdapter.MOOD_VALUE };
        int[] to = new int[] { R.id.mood_text, R.id.mood_text_value };
        SimpleCursorAdapter moods = new SimpleCursorAdapter(this, R.layout.mood_list_row, c, from, to);
       
        moodsListView = (ListView) this.findViewById(R.id.moodsListView);
        moodsListView.setAdapter(moods);		
        moodsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView  adapterView, View view, int position, long id) {
				Log.v("CLICKER", "Item clicked " + position + " " + id);
				launchUpdateMood((int) id);
			}
		});             
    }
    
    /*
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
    {
    	super.onCreateContextMenu(menu, v, menuInfo);
        
    	menu.add(0, DISPLAY_MODE_ALPHA, 0, "Alphabetically");
    	menu.add(0, DISPLAY_MODE_VALUE, 0,  "By Value");
    	menu.add(0, DISPLAY_MODE_VALUE_REVERSE, 0, "By Value Descending");
    }
    */
    /*
    public boolean onContextItemSelected(MenuItem item)
    {
        SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(DISPLAY_MODE_KEY, item.getItemId());
        editor.commit();   	
        return true;
    }
	*/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        menu.add(Menu.NONE, MENU_VIEW, Menu.NONE, R.string.view_mood_updates);
        menu.add(Menu.NONE, MENU_EDIT, Menu.NONE, R.string.edit_moods);
        menu.add(Menu.NONE, MENU_SERVICE, Menu.NONE, R.string.edit_service);
        //menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, R.string.menu_settings);
        menu.add(Menu.NONE, MENU_ABOUT, Menu.NONE, R.string.menu_about);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) {
        	case MENU_VIEW:
        		launchViewMoodUpdates();
        		return true;
        	case MENU_EDIT:
        		launchEditMoods();
        		return true;
        	case MENU_SERVICE:
        		launchService();
        		return true;
        	case MENU_SETTINGS:
        		launchSettings();
        		return true;
        	case MENU_ABOUT:
        		launchAbout();
        		return true;
        }
        return false;
    }
    
    private void launchAbout()
    {
    	Intent i = new Intent(this,MoodDroidAbout.class);
    	startActivityForResult(i, ACTIVITY_ABOUT);
    }
    
    private void launchSettings()
    {
		Intent i = new Intent(this, MoodDroidSettings.class);
		startActivityForResult(i, ACTIVITY_SETTINGS);				    	
    }
    
    private void launchService()
    {
		Intent i = new Intent(this, MoodDroidReminderService.class);
		startActivityForResult(i, ACTIVITY_SERVICE);				    	
    }
    
    private void launchUpdateMood(int id)
    {		
		Intent i = new Intent(this, MoodDroidUpdateMood.class);
		i.putExtra(MoodDroidDBAdapter.MOOD_ID, id);
		
		Cursor moodNameC = mDbHelper.fetchMood(id);
		String moodName = moodNameC.getString(1);
		i.putExtra(MoodDroidDBAdapter.MOOD_NAME, moodName);
		
		startActivityForResult(i, ACTIVITY_UPDATE);				
    }
    
    private void launchEditMoods()
    {
    	Intent i = new Intent(this, MoodDroidEditMoods.class);    	
    	startActivityForResult(i, ACTIVITY_EDIT);    	
    }
    
    private void launchViewMoodUpdates()
    {
    	Intent i = new Intent(this, MoodDroidViewMoods.class);
    	
    	// Can't really put extras..  Need to select from database in MoodDroidViewMoods
    	//i.putExtra(MoodDroidDBAdapter., value)
    	
    	startActivityForResult(i, ACTIVITY_VIEW);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {       		
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = intent.getExtras();
		
		switch(requestCode) 
		{
			case ACTIVITY_UPDATE:
				if (resultCode == RESULT_OK)
				{
					int id = extras.getInt(MoodDroidDBAdapter.MOOD_ID);
					String tag = extras.getString(MoodDroidDBAdapter.UPDATE_MOOD_TAG);
					double lat = extras.getDouble(MoodDroidDBAdapter.UPDATE_MOOD_LAT);
					double lon = extras.getDouble(MoodDroidDBAdapter.UPDATE_MOOD_LON);
	
					// Save it to the database
					if (mDbHelper.updateMood((int)id, tag, lat, lon) != -1)
					{
						Toast toast = Toast.makeText(this, "Mood Saved", Toast.LENGTH_LONG);
						toast.show();
					}
					else
					{
						Toast toast = Toast.makeText(this, "Failed", Toast.LENGTH_LONG);
						toast.show();
					}
				}
				break;
				
			case ACTIVITY_VIEW:
				
				break;
			
			case ACTIVITY_SERVICE:
				
				break;
				
			case ACTIVITY_SETTINGS:
				if (resultCode == RESULT_OK)
				{
					int displayMode = extras.getInt(MoodDroid.DISPLAY_MODE_KEY);

					SharedPreferences settings = getSharedPreferences(SETTINGS_FILE, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt(DISPLAY_MODE_KEY, displayMode);
					editor.commit();
				}
				break;
			
			case ACTIVITY_ABOUT:
				
				break;
		}        
    }

}