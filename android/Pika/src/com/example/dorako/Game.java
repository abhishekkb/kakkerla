package com.example.dorako;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class Game extends Activity{
	@Override
	public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        Global.gamePanel = new GamePanel(this);
        
        setContentView(Global.gamePanel);
        
        
        
	}
	@Override
	public void onResume(){
		super.onResume();
		
		
		
		Log.i(Global.DEBUG_TAG, "Game Activity - onResume");
		
		Global.intiGlobal(this);
		//end the background service if game is in RUN state
//		if(Global.isServiceRunning){
			Global.isServiceRunning = false;
			//while(Global.isProcessing);
			Global.isProcessing = false;
			Intent intent = new Intent(this, GameService.class);
			intent.setAction(GameService.ACTION_KILL_NOTIFY);
			startService(intent);
//		}
		
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (this);
		// get everything from preferences
		// get game state
		switch(sprefs.getInt("key_game_state", 0)){
		case 0:
			Global.gameState = GameState.SPLASH;
			break;
		case 1:
			Global.gameState = GameState.HOME;
			break;
		case 2:
			Global.gameState = GameState.HOME2PREFS;
			break;
		case 3:
			Global.gameState = GameState.RUN;
			break;
		case 4:
			Global.gameState = GameState.RUN2PREFS;
			break;
		}
		// get entity state
		switch(sprefs.getInt("key_entity_state", 0)){
		case 0:
			Global.entityState = EntityState.SLEEP;
			break;
		case 1:
			Global.entityState = EntityState.HAPPY;
			break;
		case 2:
			Global.entityState = EntityState.HUNGRY;
			break;
		case 3:
			Global.entityState = EntityState.ANGRY;
			break;
		case 4:
			Global.entityState = EntityState.SAD;
			break;
		case 5:
			Global.entityState = EntityState.EATING;
			break;			
		}
		// get entity stage
		switch(sprefs.getInt("key_entity_stage", 0)){
		case 0:
			Global.entityStage = EntityStage.EGG;
			break;
		case 1:
			Global.entityStage = EntityStage.INFANT;
			break;
		case 2:
			Global.entityStage = EntityStage.YOUNG;
			break;
		case 3:
			Global.entityStage = EntityStage.ADULT;
			break;
		}
		// get position x, y
		Global.posX = sprefs.getInt("key_pos_x", 0);
		Global.posY = sprefs.getInt("key_pos_y", 0);
		
		// get stateTimer
		Global.stateTimer = sprefs.getInt("key_state_timer", 0);
		// get stageTimer
		Global.stageTimer = sprefs.getInt("key_stage_timer", 0);
		// get foodTouched
		if(sprefs.getInt("key_food_touched", 0) == 0){
			Global.foodTouched = false;
		}else{
			Global.foodTouched = true;
		}
		
		if(Global.entityState == EntityState.HUNGRY && Global.entityStage != EntityStage.EGG){
			Global.mp_hungry.setVolume(1, 1);
		}
		/*
		if(Global.entityState == EntityState.HAPPY && Global.entityStage != EntityStage.EGG){
			Global.mp_happy.setVolume(1, 1);
		}
		*/
		
		//start the game thread again
		Global.gamePanel.resume();	
	}
	@Override
	public void onPause(){
		super.onPause();
		Log.i(Global.DEBUG_TAG, "Game Activity - onPause");
		
		Global.isProcessing = true;
		
		//stop sounds
		Global.mp_hungry.setVolume(0, 0);
		Global.mp_happy.setVolume(0, 0);
		Global.mp_happy = null;
		Global.mp_hungry = null;
		
		//pause the game thread again
		Global.gamePanel.pause();
		
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (this);
		Editor editor = sprefs.edit();
		// save everything in preference
		// set game state
		switch(Global.gameState){
		case SPLASH:
			editor.putInt("key_game_state", 0);
			break;
		case HOME:
			editor.putInt("key_game_state", 1);
			break;
		case HOME2PREFS:
			editor.putInt("key_game_state", 2);
			break;
		case RUN:
			editor.putInt("key_game_state", 3);
			break;
		case RUN2PREFS:
			editor.putInt("key_game_state", 4);
			break;
		}
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
		// set position x, y
		editor.putInt("key_pos_x", Global.posX);
		editor.putInt("key_pos_y", Global.posY);
		// set stateTimer
		editor.putInt("key_state_timer", Global.stateTimer);
		// set stageTimer
		editor.putInt("key_stage_timer", Global.stageTimer);
		// set foodTouched
		if(Global.foodTouched){
			editor.putInt("key_food_touched", 1);
		}else{
			editor.putInt("key_food_touched", 0);
		}
		
		editor.commit();
		
		
		
		//TODO: start up a service in the background if game is in RUN state
		if(Global.gameState == GameState.RUN){
			Intent intent = new Intent(this, GameService.class);
			intent.setAction(GameService.ACTION_START);
			startService(intent);
			Global.isServiceRunning = true;
		}
		
	}
	@Override
	public void onBackPressed() {
		Log.i(Global.DEBUG_TAG, "Game Activity - onBackPressed");
	    moveTaskToBack(true);
	}
	
}
