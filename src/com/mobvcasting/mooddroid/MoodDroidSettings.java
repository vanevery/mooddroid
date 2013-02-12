package com.mobvcasting.mooddroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.mobvcasting.mooddroid.R;

public class MoodDroidSettings extends Activity 
{
	private ListView settingsListView;
	private ArrayList<String> listItems = new ArrayList();
	
     @Override 
     public void onCreate(Bundle icicle) 
     { 
          super.onCreate(icicle); 
          setContentView(R.layout.mood_settings);

          listItems.add("test " + MoodDroid.DISPLAY_MODE_ALPHA);
          listItems.add("test " + MoodDroid.DISPLAY_MODE_VALUE);
          listItems.add("test " + MoodDroid.DISPLAY_MODE_VALUE_REVERSE);
          
          settingsListView = (ListView) this.findViewById(R.id.moodSettingsListView);
          
          settingsListView.setAdapter(new ArrayAdapter<String>(this,
                  R.layout.settings_list_row, R.id.settings_list_item_tv,listItems));
          
          settingsListView.setOnItemClickListener(new OnItemClickListener() {
  			public void onItemClick(AdapterView  adapterView, View view, int position, long id) {
  				Log.v("CLICKER", "Item clicked " + position + " " + id);
  				
				Bundle bundle = new Bundle();
	            bundle.putInt(MoodDroid.DISPLAY_MODE_KEY, position);
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