
package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MoodDroidReminderService extends Activity 
{
	public static final String PREFS_NAME = "MoodDroidReminderService";
	
	private CheckBox moodReminderServiceEnabledCB;
	private Button moodReminderServiceSaveButton;
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mood_reminder_service);
		
		moodReminderServiceSaveButton = (Button) findViewById(R.id.save_reminder_settings);
		moodReminderServiceEnabledCB = (CheckBox) findViewById(R.id.reminders_on);
		
		
	    settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean moodReminderServiceEnabled = settings.getBoolean("moodReminderServiceEnabled", false);
	       
		if (moodReminderServiceEnabled)
		{
			moodReminderServiceEnabledCB.setChecked(true);
		}
		    		
		moodReminderServiceSaveButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				SharedPreferences.Editor editor = settings.edit();

				if (moodReminderServiceEnabledCB.isChecked())
				{
				    editor.putBoolean("moodReminderServiceEnabled", true);

				    // Launch Service
				    
				}
				else
				{
				    editor.putBoolean("moodReminderServiceEnabled", false);
				    
				    // Kill Service
				    
				}
			    editor.commit();					


			    
	            Intent mIntent = new Intent();
	            setResult(RESULT_OK, mIntent);
	            finish();				
			}
		});
	}

    //onBackPressed() 
	public boolean onKeyDown(int keyCode, KeyEvent kevent)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
            Intent mIntent = new Intent();
            setResult(RESULT_CANCELED, mIntent);
            finish();				
			
			return true;
		}
		return false;
	}
}
