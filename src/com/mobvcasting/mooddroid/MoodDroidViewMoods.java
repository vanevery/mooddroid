package com.mobvcasting.mooddroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MoodDroidViewMoods extends Activity 
{
	private MoodDroidDBAdapter mDbHelper;
	//private CustomDrawableView mCustomDrawableView;
	private GraphView graphView;
	
    int idColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_ID);
    int tsColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_TIMESTAMP);
    int nameColumn;// = c.getColumnIndex(MoodDroidDBAdapter.MOOD_NAME);
    int tagColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_TAG);
    int lonColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LON);
    int latColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LAT);
    int valueColumn;// = c.getColumnIndex(MoodDroidDBAdapter.MOOD_VALUE);
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        mDbHelper = new MoodDroidDBAdapter(this);
    	mDbHelper.open();
            
    	Cursor c = mDbHelper.fetchAllMoodUpdates();

    	//mCustomDrawableView = new CustomDrawableView(this,c);
        //setContentView(mCustomDrawableView);

        idColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_ID);
        tsColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_TIMESTAMP);
        nameColumn = c.getColumnIndex(MoodDroidDBAdapter.MOOD_NAME);
        tagColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_TAG);
        lonColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LON);
        latColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LAT);
        valueColumn = c.getColumnIndex(MoodDroidDBAdapter.MOOD_VALUE);
    	
        int values[] = new int[c.getCount()];

        c.moveToFirst();        
        int count = 0;
        if (c != null) 
        { 
             if (c.moveToFirst()) 
             { 
                  do {                        
                       values[count] = c.getInt(valueColumn);
                       count++;               		
                  } while (c.moveToNext()); 
             } 
        }        
        
		//float[] values = new float[] { 2.0f,1.5f, 2.5f, 1.0f , 3.0f };
		String[] verlabels = new String[] { "great", "ok", "bad" };
		String[] horlabels = new String[] { "today", "tomorrow", "next week", "next month" };

        graphView = new GraphView(this, values, "Saved Moods",horlabels, verlabels, GraphView.LINE);
		setContentView(graphView);       
        
    }
	
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
