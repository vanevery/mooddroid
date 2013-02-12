
package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MoodDroidEditMoods extends Activity 
{
	public static final int ACTIVITY_EDIT_MOOD = 0;
	
	private MoodDroidDBAdapter mDbHelper;
	private ListView moodsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.moods_edit);
		
		mDbHelper = new MoodDroidDBAdapter(this);
        mDbHelper.open();
            
        moodsListView = (ListView) this.findViewById(R.id.moodsListView);
        
        Cursor c = mDbHelper.fetchAllMoods();
        startManagingCursor(c);
  
        String[] from = new String[] { MoodDroidDBAdapter.MOOD_NAME, MoodDroidDBAdapter.MOOD_VALUE };
        int[] to = new int[] { R.id.mood_text_value, R.id.mood_text };
        
        SimpleCursorAdapter moods = new SimpleCursorAdapter(this, R.layout.mood_list_row, c, from, to);
       
        moodsListView.setAdapter(moods);
		
        moodsListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView  adapterView, View view, int position, long id) {
				Log.v("CLICKER", "Item clicked " + position + " " + id);
				launchEditMood((int) id);
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

    private void launchEditMood(int id)
    {		
		Intent i = new Intent(this, MoodDroidEditMood.class);
		i.putExtra(MoodDroidDBAdapter.MOOD_ID, id);
		
		Cursor moodNameC = mDbHelper.fetchMood(id);
		
		String moodName = moodNameC.getString(1);
		i.putExtra(MoodDroidDBAdapter.MOOD_NAME, moodName);
		
		int moodValue = moodNameC.getInt(2);
		i.putExtra(MoodDroidDBAdapter.MOOD_VALUE, moodValue);
		
		int moodDisplay = moodNameC.getInt(3);
		i.putExtra(MoodDroidDBAdapter.MOOD_DISPLAY, moodDisplay);
		
		startActivityForResult(i, ACTIVITY_EDIT_MOOD);				
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {       		
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle extras = intent.getExtras();
		
		switch(requestCode) 
		{
			case ACTIVITY_EDIT_MOOD:

				if (resultCode == RESULT_OK)
				{
					int id = extras.getInt(MoodDroidDBAdapter.MOOD_ID);
					int display = extras.getInt(MoodDroidDBAdapter.MOOD_DISPLAY);
					int value = extras.getInt(MoodDroidDBAdapter.MOOD_VALUE);

					// Save it to the database
					if (mDbHelper.updateEditMood((int)id, display, value) != -1)
					{
						Toast toast = Toast.makeText(this, R.string.mood_saved, Toast.LENGTH_LONG);
						toast.show();
					}
					else
					{
						Toast toast = Toast.makeText(this, R.string.failed, Toast.LENGTH_LONG);
						toast.show();
					}
				}
				break;
		}        
    }

}
