package com.mobvcasting.mooddroid;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;

public class CustomDrawableView extends View 
{
    //private ShapeDrawable mDrawable;
    private Paint paint;

    //MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_ID + ", " + 
    //MOODS_UPDATE_TABLE + "." + UPDATE_TIMESTAMP + ", " + 
    //MOODS_TABLE + "." + MOOD_NAME +
	//MOODS_TABLE + "." + MOOD_VALUE + ", " +
	//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_TAG + ", " + 
	//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LAT + ", " +
	//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LON + ", " +
	
    int idColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_ID);
    int tsColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_TIMESTAMP);
    int nameColumn;// = c.getColumnIndex(MoodDroidDBAdapter.MOOD_NAME);
    int tagColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_TAG);
    int lonColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LON);
    int latColumn;// = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LAT);
    int valueColumn;// = c.getColumnIndex(MoodDroidDBAdapter.MOOD_VALUE);
    
    Cursor c;
    
    public CustomDrawableView(Context context, Cursor c) 
    {
        super(context);
        this.c = c;
        
        
        //MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_ID + ", " + 
        //MOODS_UPDATE_TABLE + "." + UPDATE_TIMESTAMP + ", " + 
        //MOODS_TABLE + "." + MOOD_NAME +
		//MOODS_TABLE + "." + MOOD_VALUE + ", " +
		//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_TAG + ", " + 
		//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LAT + ", " +
		//MOODS_UPDATE_TABLE + "." + UPDATE_MOOD_LON + ", " +
		
        idColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_ID);
        tsColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_TIMESTAMP);
        nameColumn = c.getColumnIndex(MoodDroidDBAdapter.MOOD_NAME);
        tagColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_TAG);
        lonColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LON);
        latColumn = c.getColumnIndex(MoodDroidDBAdapter.UPDATE_MOOD_LAT);
        valueColumn = c.getColumnIndex(MoodDroidDBAdapter.MOOD_VALUE);

        //String moodName = c.getString(0);
        /*
        int x = 0;
        int y = 0;
        int width = 100;
        int height = 100;
        int heightInc = height/10;
        int widthInc = width/c.getColumnCount();
        
        Path path = new Path();
        path.moveTo(height,0);

        c.moveToFirst();
        
        int count = 0;
        if (c != null) 
        { 
             if (c.moveToFirst()) 
             { 
                  int i = 0; 

                  do { 
                       i++; 

                       int value = c.getInt(valueColumn);
                       count++;
 
                       path.lineTo(count*widthInc,height-value*heightInc);
                       
                  } while (c.moveToNext()); 
             } 
        }
        
        mDrawable = new ShapeDrawable(new PathShape(path,(float)width,(float)height));
        mDrawable.getPaint().setColor(0xff74AC23);
        mDrawable.setBounds(x, y, x + width, y + height);
        */
		paint = new Paint();
		paint.setColor(Color.WHITE);
    }

    protected void onDraw(Canvas canvas) 
    {
        //mDrawable.draw(canvas);

        int x = 0;
        int y = 0;
        int width = 100;
        int height = 100;
        int heightInc = height/10;
        int widthInc = width/c.getColumnCount();
        
        c.moveToFirst();        
        int count = 0;
        int lastValue = height;
        int value = height;
        String moodName = "";
        if (c != null) 
        { 
             if (c.moveToFirst()) 
             { 
                  int i = 0; 

                  do { 
                       i++; 
                       
                       lastValue = value;
                       value = c.getInt(valueColumn);
                       count++;
 
                       //path.lineTo(count*widthInc,height-value*heightInc);
                       canvas.drawLine(count-1*widthInc,height-lastValue*heightInc,count*widthInc,height-value*heightInc,paint);
               		
                       moodName = c.getString(nameColumn);
                       canvas.drawText(moodName, count*widthInc, height-value*heightInc, paint);
                       //canvas.drawText("" + count*widthInc + "," + (height-value*heightInc), count*widthInc, height-value*heightInc, paint);
                       
                  } while (c.moveToNext()); 
             } 
        }
    }
}
