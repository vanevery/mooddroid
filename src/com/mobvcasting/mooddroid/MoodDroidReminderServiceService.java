package com.mobvcasting.mooddroid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MoodDroidReminderServiceService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Timer timer = new Timer();

	public void onCreate() 
	{
		super.onCreate();
		startservice();
	}

	private void startservice() 
	{
		timer.scheduleAtFixedRate( 
			new TimerTask() 
			{
				public void run() 
				{
					//Do whatever you want to do every ÒINTERVALÓ

				}
		}, 0, 6000);
	}
	
	private void stopservice() 
	{
		if (timer != null)
		{
			timer.cancel();
		}
	}
}
