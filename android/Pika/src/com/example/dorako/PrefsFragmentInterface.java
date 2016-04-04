package com.example.dorako;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

/*___________________________________________________________________
|
| Class: PrefsFragmentInterface
|__________________________________________________________________*/
public class PrefsFragmentInterface extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
    final static String TAG = "PrefsFragmentInterface";
    Thread thread = null;
    boolean b = true;
    Timer myTimer=null;
    Handler handler = null;

    
	/*___________________________________________________________________
	|
	| Function: PrefsFragmentInterface (constructor) 
	|__________________________________________________________________*/
    public PrefsFragmentInterface () 
    {
    }
    
	/*___________________________________________________________________
	|
	| Function: onCreate 
	|__________________________________________________________________*/
	@Override
    public void onCreate (Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);   	
    	// Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_interface);
        
    }
	
	/*___________________________________________________________________
	|
	| Function: onResume 
	|__________________________________________________________________*/
    @Override
    public void onResume() 
    {
    	super.onResume();
    	b = true;
    	
    	
    	
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    	
    	
    	// Get the value of number from default preferences
    	SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(Global.context);
    	Editor editor = sprefs.edit();
    	if(Global.gameState == GameState.HOME2PREFS){
    		Global.gameState = GameState.HOME;
    		editor.putInt("key_game_state", 1); //key value = 1 for HOME
    	}else if(Global.gameState == GameState.RUN2PREFS){
    		Global.gameState = GameState.RUN;
    		editor.putInt("key_game_state", 3); //key value = 3 for RUN
    	}
		editor.commit();


    	/*
    	getActivity().runOnUiThread(new Runnable(){
			public void run() {
				while(b){
			    	Preference prefs = getPreferenceScreen().findPreference("key_restart");
					boolean s =false;
					//Preference sprefs = getPreferenceScreen().findPreference("key_restart");
					if(s){
						prefs.setIcon(R.drawable.icon_restart);
						s = false;
					}else{
						prefs.setIcon(R.drawable.icon_restart2);
						s = true;
					}
					prefs = null;
				}
			}
    	});
   		*/
		
		/*
		handler = new Handler();
		handler.postDelayed(new Runnable() {
		     public void run() {
		    	 Log.i(Global.DEBUG_TAG, "Preferences - handler method started, b = "+b);
		    	 while(b){
			    	Preference prefs = getPreferenceScreen().findPreference("key_restart");
					boolean s =false;
					//Preference sprefs = getPreferenceScreen().findPreference("key_restart");
					if(s){
						prefs.setIcon(R.drawable.icon_restart);
						s = false;
					}else{
						prefs.setIcon(R.drawable.icon_restart2);
						s = true;
					}
					prefs = null;
				}
		     }
		}, 100);
		*/
		
    	// Set up a click handler
    	Preference prefs = getPreferenceScreen().findPreference("key_restart");
    	prefs.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick (Preference preference) {
    			new AlertDialog.Builder(getActivity())
    				.setTitle("Restart Game?")
    				.setMessage("Are you sure?")
    				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						// exit the preferences screen
    						//getActivity().finish();
    						SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (Global.context);
    						Editor editor = sprefs.edit();
    						editor.clear();
    						editor.commit();
    						getActivity().startActivity(new Intent(getActivity(),Game.class));
    					}
    				})
    				.setNegativeButton("No", new DialogInterface.OnClickListener() {
    					public void onClick (DialogInterface dialog, int which) {
    						// do nothing
    					}
    				})
    				.setIcon(android.R.drawable.ic_dialog_alert)
    				.show();
    			return (true);
    		}
    	});

    }

    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	 b = false;
    	
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		//if(key.equals("key_play_tts")){
			Global.playtts = sharedPreferences.getBoolean("key_play_tts", true);
			Log.i(Global.DEBUG_TAG,"OncheckBoxChanged : value = "+Global.playtts);
			
		//}
	}
}
