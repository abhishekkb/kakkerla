package com.example.dorako;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class GameService extends MyIntentService {

	public static final String ACTION_START = "com.example.gator.START";
	public static final String ACTION_KILL_NOTIFY = "com.example.gator.KILL_NOTIFY";
	
	NotificationManager mNotifyManager;
	Notification mNotify = null;
	final int NOTIFICATION_ID = 1;
	
	public GameService() {
		super("GatorService");
	}
	@Override
	public void onCreate ()
	{
		super.onCreate();
		Global.serviceIsProcessing = false;
		// Get a handle to the notification manager
		mNotifyManager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
		
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		
		if (action.equals(ACTION_START))
			processStart();
		else if (action.equals(ACTION_KILL_NOTIFY))
			processStopNotify();
		
	}
	void processStart () {
		float curTime;
		
		//if (Global.serviceIsProcessing)
		//	return;
		
		if(Global.gameState == GameState.SPLASH || Global.gameState == GameState.HOME){
			return;
		}
		//Global.serviceIsProcessing = true;
		
		String title = null;
		String text = null;
		
		///////////////////////////////////////////////////////
		while(Global.entityState != EntityState.HUNGRY){
			if(Global.entityState == EntityState.ANGRY || Global.entityState == EntityState.SAD){
				text = "I'm " + Global.entityState + " : I need energy";
				break;
			}
			
			int t = Global.stateTimer;
			if(Global.isServiceRunning == false){
				Global.isProcessing = false;
				return;
			}
			
			long x = 0;
			if(Global.entityState == EntityState.SLEEP){
				x = (Global.stateTime_eating - Global.stateTimer)/Global.FPS;
			}else if(Global.entityState == EntityState.HAPPY){
				x = (Global.stateTime_happy - Global.stateTimer)/Global.FPS;
			}else if(Global.entityState == EntityState.EATING){
				x = (Global.stateTime_happy - Global.stateTimer)/Global.FPS;
			}
			
			Log.i(Global.DEBUG_TAG, "GameService - stateTimer :"+ x);
			float initTime = System.nanoTime() / 1000000000f;	
			while(true) {
				curTime = System.nanoTime() / 1000000000f;
				if(Global.isServiceRunning == false){
					Global.stateTimer = (int) ((x - (curTime - initTime)))*Global.FPS;
					Global.isProcessing = false;
					commitChanges();
					return;
				}
				//Log.i(Global.DEBUG_TAG, "GameService - stateTimer :"+ x);
				//Log.i(Global.DEBUG_TAG, "GameService - stateTimer :"+ (curTime-initTime));
				if((curTime-initTime) > x){
					break;
				}
			}		
			
			if(Global.entityState == EntityState.SLEEP){
				Log.i(Global.DEBUG_TAG, "GameService - stateChanging : sleep -> happy");
				Global.entityState = EntityState.HAPPY;
			}else if(Global.entityState == EntityState.HAPPY){
				Log.i(Global.DEBUG_TAG, "GameService - stateChanging : happy - > hungry");
				Global.entityState = EntityState.HUNGRY;
				Global.stateTimer = 0;
				commitChanges();
				break;
			}else if(Global.entityState == EntityState.EATING){
				Log.i(Global.DEBUG_TAG, "GameService - stateChanging : eating -> happy");
				Global.stageTimer = Global.stageTimer + t;
				Global.foodTouched = false;
				Global.entityState = EntityState.HAPPY;
			}
			Global.stateTimer = 0;
			commitChanges();
			
			if(Global.isServiceRunning == false){
				Global.isProcessing = false;
				return;
			}			
			
		}
		///////////////////////////////////////////////////
		
		/*
		if(Global.entityState == EntityState.HAPPY){
			while(Global.stateTimer <= Global.stateTime_happy){
				Global.stateTimer++;
			}
			Global.stateTimer = 0;
			Global.entityState = EntityState.HUNGRY;
		}
		*/
		commitChanges();
		
		text = "I'm " + Global.entityState + " : I need energy";
		Global.isProcessing = false;
		
		//Global.stateTimer = 0;
		// Prepare the pending Intent
		Intent intent = new Intent (this, Game.class);
		PendingIntent pIntent = PendingIntent.getActivity(this,  0,  intent,  
				     PendingIntent.FLAG_UPDATE_CURRENT);
		// Show the notification
		Notification n = new Notification.Builder(this)
			.setContentTitle("pika : " )
			.setContentText(text)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent)
			.setAutoCancel(true)
			.build();
		mNotifyManager.notify(NOTIFICATION_ID, n);
		
		Global.serviceIsProcessing = false;
	}
	
	public void commitChanges(){
		// set entity state in prefs 
		// set stateTimer in prefs
		
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(Global.context);
    	Editor editor = sprefs.edit();
    	// set entity state
		switch(Global.entityState){
		case SLEEP:
			editor.putInt("key_entity_state", 0);
			break;
		case HAPPY:
			editor.putInt("key_entity_state", 1);
			break;
		case HUNGRY:
			editor.putInt("key_entity_state", 2);
			break;
		case ANGRY:
			editor.putInt("key_entity_state", 3);
			break;
		case SAD:
			editor.putInt("key_entity_state", 4);
			break;
		case EATING:
			editor.putInt("key_entity_state", 5);
			break;			
		}
		// set entity stage
		switch(Global.entityStage){
		case EGG:
			editor.putInt("key_entity_stage", 0);
			break;
		case INFANT:
			editor.putInt("key_entity_stage", 1);
			break;
		case YOUNG:
			editor.putInt("key_entity_stage", 2);
			break;
		case ADULT:
			editor.putInt("key_entity_stage", 3);
			break;
		}
		
		editor.putInt("key_state_timer", Global.stateTimer);
		editor.putInt("key_stage_timer", Global.stageTimer);
		if(Global.foodTouched){
			editor.putInt("key_food_touched", 1);
		}else{
			editor.putInt("key_food_touched", 0);
		}
		editor.commit();
	}
	void processStopNotify() 
	{
		mNotifyManager.cancel(NOTIFICATION_ID);
	}
	@Override
	public void onDestroy ()
	{
		mNotifyManager = null;
		super.onDestroy();
	}
}