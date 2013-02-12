package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MoodDroidUpdateMood extends Activity {

	private TextView moodNameView;
	private EditText moodUpdateTagView;
	private Button moodUpdateSaveButton;
	private CheckBox moodUpdateUseLocationCheckbox;
	private int moodId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mood_update);

		moodNameView = (TextView) findViewById(R.id.mood_name);
//http://developer.android.com/reference/android/widget/AutoCompleteTextView.html
		moodUpdateTagView = (EditText) findViewById(R.id.mood_tag);
		moodUpdateSaveButton = (Button) findViewById(R.id.save_mood);
		moodUpdateUseLocationCheckbox = (CheckBox) findViewById(R.id.use_location);
					
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String moodName = extras.getString(MoodDroidDBAdapter.MOOD_NAME);
		    moodId = extras.getInt(MoodDroidDBAdapter.MOOD_ID);
		           
		    if (moodName != null) {
		        moodNameView.setText(moodName);
		    }
		}
		
		moodUpdateSaveButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				Bundle bundle = new Bundle();
		        
				bundle.putString(MoodDroidDBAdapter.UPDATE_MOOD_TAG, moodUpdateTagView.getText().toString());
	            bundle.putInt(MoodDroidDBAdapter.MOOD_ID, moodId);

	            double lat = 0;
	            double lon = 0;
	            
	            if (moodUpdateUseLocationCheckbox.isChecked())
	            {
	            	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	            	Criteria criteria = new Criteria();
	            	criteria.setAccuracy(Criteria.ACCURACY_FINE);
	            	String provider = lm.getBestProvider(criteria, true);
	            	Location l = lm.getLastKnownLocation(provider);
	            	if (l == null)
	            	{
	            		showLocationError();
	            	}
	            	else
	            	{
	            		lat = l.getLatitude();
	            		lon = l.getLongitude();	            		
	            	}
	            }
	            
	            bundle.putDouble(MoodDroidDBAdapter.UPDATE_MOOD_LAT, lat);
	            bundle.putDouble(MoodDroidDBAdapter.UPDATE_MOOD_LON, lon);	            	
	            
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
	
	
	private void showLocationError()
	{
		Toast toast = Toast.makeText(this, "Location Finding Failed", Toast.LENGTH_SHORT);
		toast.show();	            				
	}
}
