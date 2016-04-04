package com.kakkerla.hm5_kakkerla;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Crushy extends Activity {
	
	//GamePanel gamePanel;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.i(Global.DEBUG_TAG,"Crushy - on create");
		
		//set title on or off
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//set screen size
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Global.setGlobal(this);

		Global.gamePanel = new GamePanel(this);
		
		if(Global.mp_enable){
			Global.mp.start();
			Global.mp.setVolume(1.f, 1.f);
		}else{
			Global.mp.setVolume(0.f, 0.f);
			Global.mp.stop();
		}
	
		
		setContentView(Global.gamePanel);
	}
    @Override
    public void onPause(){
    	super.onPause();
    	Log.i(Global.DEBUG_TAG,"Crushy - on Pause");
    	Global.gamePanel.pause();
    	
    	if(Global.gameState != GameState.GAME_HOME || Global.gameState != GameState.GAME_PAUSE){
	    	if(Global.mp_enable){
	    		Global.mp.setVolume(0, 0);
	    	}
    	}
    	
    	//gamePanel = null;
    }
    @Override
    public void onResume(){
    	super.onResume();
    	Log.i(Global.DEBUG_TAG,"Crushy - on Resume");
    	Global.gamePanel.resume();
    	if(Global.gameState != GameState.GAME_HOME || Global.gameState != GameState.GAME_PAUSE){
	    	if(Global.mp_enable){
	    		Global.mp.setVolume(1, 1);
	    	}
    	}
    	
    }
    @Override
    public void onBackPressed(){
    	// super.onBackPressed(); // onBackPressed() will not work with this
    	// displaying a toast
    	
    	if(Global.gameState == GameState.GAME_HOME || Global.gameState == GameState.GAME_PAUSE){
    		String dialog = "You will Lose your progress!!";
    		if(Global.gameState == GameState.GAME_HOME){
    			dialog = "Are you sure";
    		}
			Toast.makeText(this, "back button pressed", Toast.LENGTH_LONG).show();
			new AlertDialog.Builder(this)
				.setTitle("Confirm Quitting")
				.setMessage(dialog)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dg, int id){
						Log.i(Global.DEBUG_TAG, "Crushy - dialog box - yes clicked");
						//isBackPressed = true;
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dg, int id){
						// return; // do nothing
						//isBackPressed = false;
					}
				}).show();
    	}
    	if(Global.gameState == GameState.GAME_RUN2PREF){
    		Global.gameState = GameState.GAME_PAUSE;
    	}
    	if(Global.gameState == GameState.GAME_HOME2PREF){
    		Global.gameState = GameState.GAME_HOME;
    	}
    	if(Global.gameState == GameState.GAME_RUN || Global.gameState == GameState.GAME_READY ){
    		Global.gameState = GameState.GAME_PAUSE;
    	}
    	if(Global.gameState == GameState.GAME_OVER){
    		Global.gameState = GameState.GAME_HOME;
    	}
    	//super.onBackPressed();
    }
}
