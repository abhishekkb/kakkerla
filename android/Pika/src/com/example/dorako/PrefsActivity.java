package com.example.dorako;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
 
/*___________________________________________________________________
|
| Class: PrefsActivity
|__________________________________________________________________*/
public class PrefsActivity extends PreferenceActivity
{
    // Tag for debug messages
    final static String TAG = "PrefsActivity";

	/*___________________________________________________________________
	|
	| Function: onCreate 
	|__________________________________________________________________*/
    @Override
    public void onCreate (Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
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
    }

	/*___________________________________________________________________
	|
	| Function: isValidFragment 
	|__________________________________________________________________*/
    @Override
    protected boolean isValidFragment (String fragmentName)
    {
    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
    		return true;
    	else if (PrefsFragmentAbout.class.getName().equals(fragmentName) ||
    			 PrefsFragmentInterface.class.getName().equals(fragmentName))
    		return true;
   
    	return false;
    }
    
	/*___________________________________________________________________
	|
	| Function: onBuildHeaders 
	|__________________________________________________________________*/
    @Override
    public void onBuildHeaders (List<Header> target) 
    {
    	// Use this to load an XML file containing references to multiple fragments (a multi-screen preferences screen)
    	loadHeadersFromResource(R.xml.prefs_headers, target);	
     }  
}
