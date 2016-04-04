package com.kakkerla.hm5_kakkerla;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.util.Log;

/*___________________________________________________________________
|
| Class: PrefsFragmentSettings
|__________________________________________________________________*/
public class PrefsFragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener 
{
    final static String TAG = "PrefsFragmentSettings";
    public String DEBUG_TAG = "ProjectLogging"; 
	/*___________________________________________________________________
	|
	| Function: PrefsFragmentSettings (constructor) 
	|__________________________________________________________________*/
    public PrefsFragmentSettings () 
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
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }
	
	/*___________________________________________________________________
	|
	| Function: onResume 
	|__________________________________________________________________*/
    @Override
    public void onResume() 
    {
    	super.onResume();

    	
	    // Set up a listener whenever a key changes 
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	    
       	// Get the value of number from the preferences
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (Global.context);	
		int number = sprefs.getInt("key_high_score", 0);
		
		// Write the value of number in the summary text so it shows up on the preferences screen
    	Preference pref;
    	pref = getPreferenceScreen().findPreference("key_high_score");
    	pref.setSummary("" + Global.highScore);
    	
    }
    
    @Override 
    public void onPause(){
    	Global.inPrefsScreen = false;
    	if(Global.gameState == GameState.GAME_HOME2PREF){
    		Global.gameState = GameState.GAME_HOME;
    	}else if(Global.gameState == GameState.GAME_RUN2PREF){
    		Global.gameState = GameState.GAME_PAUSE;
    	}
    	super.onPause();
    }

	/*___________________________________________________________________
	|
	| Function: onSharedPreferenceChanged 
	|__________________________________________________________________*/
	public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)  
	{ 	
		if (key.equals("prefs_music_enabled")) {		
			boolean b = sharedPreferences.getBoolean("prefs_music_enabled", true);
			Global.mp_enable = b;
			if (b == false) {
				if (Global.mp != null)
					Global.mp.setVolume(0, 0);
			}
			else {
				if (Global.mp != null)
					Global.mp.setVolume(1, 1);				
			}
		}
		if (key.equals("prefs_soundFX_enabled")) {		
			boolean b = sharedPreferences.getBoolean("prefs_music_enabled", true);
			Global.sp_enable = b;
		}
	}
}
