package com.mobvcasting.mooddroid;

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler; 
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MoodDroidSplash extends Activity {

     private final int SPLASH_DISPLAY_LENGTH = 10000; 

     @Override 
     public void onCreate(Bundle icicle) { 
    	 
          super.onCreate(icicle); 
         
          setContentView(R.layout.mood_splash);

          /* Wonder why I can't make the View itself touchable?  Get a null pointer exception */
          
          /*
           * Make image touchable
           * 
           */
          ImageView currentView = (ImageView) findViewById(R.id.SplashImageView);
          currentView.setOnTouchListener(new OnTouchListener() 
          {
			public boolean onTouch(View v, MotionEvent event) 
			{
				Intent mainIntent = new Intent(MoodDroidSplash.this,MoodDroid.class); 
                MoodDroidSplash.this.startActivity(mainIntent); 
                MoodDroidSplash.this.finish(); 
                
				return true;
			}
          });
          
          /* New Handler to start the Menu-Activity 
           * and close this Splash-Screen after some seconds.*/ 
          new Handler().postDelayed(new Runnable(){ 
               public void run() { 
                    /* Create an Intent that will start the Menu-Activity. */ 
                    Intent mainIntent = new Intent(MoodDroidSplash.this,MoodDroid.class); 
                    MoodDroidSplash.this.startActivity(mainIntent); 
                    MoodDroidSplash.this.finish(); 
               } 
          }, SPLASH_DISPLAY_LENGTH); 
     } 
}