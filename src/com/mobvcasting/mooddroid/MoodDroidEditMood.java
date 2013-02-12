
package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class MoodDroidEditMood extends Activity 
{
	private CheckBox moodDisplayCheckbox;
	private Spinner moodValueSpinner;
	private TextView moodNameView;
	private Button moodEditSaveButton;
	private int moodId;
	private int moodDisplay;
	private int moodValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mood_edit);

		moodNameView = (TextView) findViewById(R.id.mood_name);
		moodEditSaveButton = (Button) findViewById(R.id.save_mood);
		moodDisplayCheckbox = (CheckBox) findViewById(R.id.display_mood);
		moodValueSpinner = (Spinner) findViewById(R.id.mood_value);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String moodName = extras.getString(MoodDroidDBAdapter.MOOD_NAME);
		    moodId = extras.getInt(MoodDroidDBAdapter.MOOD_ID);
		    moodDisplay = extras.getInt(MoodDroidDBAdapter.MOOD_DISPLAY);
		    moodValue = extras.getInt(MoodDroidDBAdapter.MOOD_VALUE);
		           
		    if (moodName != null) {
		        moodNameView.setText(moodName);
		    }
		    
		    if (moodDisplay > 0)
		    {
		    	moodDisplayCheckbox.setChecked(true);
		    }
		    
		    ArrayAdapter moodValueSpinnerAdapter = ArrayAdapter.createFromResource(
		            this, R.array.mood_values, android.R.layout.simple_spinner_item);
		    moodValueSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    moodValueSpinner.setAdapter(moodValueSpinnerAdapter);
		    
		    moodValueSpinner.setSelection(moodValue);
		}
		
		moodEditSaveButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				if (moodDisplayCheckbox.isChecked())
				{
					moodDisplay = 1;
				}
				
				String moodValueS = (String)moodValueSpinner.getSelectedItem();
				moodValue = Integer.parseInt(moodValueS);
				
				Bundle bundle = new Bundle();
		        
	            bundle.putInt(MoodDroidDBAdapter.MOOD_ID, moodId);
	            bundle.putInt(MoodDroidDBAdapter.MOOD_DISPLAY, moodDisplay);
	            bundle.putInt(MoodDroidDBAdapter.MOOD_VALUE, moodValue);
	            
	            Intent mIntent = new Intent();
	            mIntent.putExtras(bundle);
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
