package com.example.msg_future;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class WebService extends Service{

	private final String TAG = "WebService";
	private final int RUN_INTERVAL = 30*1000;	// 30 seconds
//	private Timer timer = new Timer();
//	private NotificationManager notify;
	private boolean isRunning = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void onCreate() {
        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
    }
 
    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");    
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);
            
            //Announcement about starting
            Toast.makeText(this, "Starting the WebService", Toast.LENGTH_SHORT).show();
            
            //Start a Background thread
            isRunning = true;
            Thread backgroundThread = new Thread(new BackgroundThread());
            backgroundThread.start();
            
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
 
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	isRunning = false;
    	Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }
    
    private class BackgroundThread implements Runnable{
    	
		@Override
		public void run() {
			try {
	            while(isRunning) {
	            	new HTTPconnect().execute(		// AsyncTask<Object, Void, Boolean>
    					getApplicationContext(),	// Activity Context
    					2,							// Type
    					"get_messages.php",			// URL 				(file/script)
    					getMyPhoneNumber()			// from 			(devices phone number)
    				);
                    Thread.sleep(RUN_INTERVAL);
	            }
	            Log.d(TAG, "Background Thread is finished.........");
            }
            catch(Exception e){
                e.printStackTrace();
                Log.e(TAG, "Error: "+e.toString());
            }
		}
    	
    }
    
    private String getMyPhoneNumber(){
	    return ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
	}
}
