package com.example.dorako;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Global {

	public static boolean isServiceRunning = false;
	public static boolean isProcessing = false;
	
	public static final String DEBUG_TAG = "ProjectLogging";
	public static final int FPS = 5;
	public static int windowWidth;
	public static int windowHeight;
	
	public static GameState gameState;
	
	//flags
	public static boolean isGameNew;
	public static boolean exitSplash = false;
	public static GamePanel gamePanel = null;
	
	public static boolean isttsplayed = false;
	
	//contexts
	public static Context context;

	public static boolean foodTouched = false;
	
	// Bitmaps
	public static Bitmap bm_splash = null;
	public static Bitmap bm_background = null;
	public static Bitmap bm_button_start = null;
	public static Bitmap bm_button_prefs = null;
	public static Bitmap bm_egg = null;
	public static Bitmap bm_eggCrack = null;
	public static Bitmap bm_eggUnstable = null;
	public static Bitmap bm_eggSleep = null;
	public static Bitmap bm_young_angry = null;
	public static Bitmap bm_young_happy = null;
	public static Bitmap bm_young_hungry = null;
	public static Bitmap bm_young_sad = null;
	public static Bitmap bm_young_sleep = null;
	public static Bitmap bm_young_walkingLeft = null;
	public static Bitmap bm_young_walkingRight = null;
	public static Bitmap bm_infant_angry = null;
	public static Bitmap bm_infant_eating = null;
	public static Bitmap bm_infant_happy = null;
	public static Bitmap bm_infant_hungry = null;
	public static Bitmap bm_infant_sad = null;
	public static Bitmap bm_infant_sleep = null;
	public static Bitmap bm_infant_walkingLeft = null;
	public static Bitmap bm_infant_walkingRight = null;
	public static Bitmap bm_adult = null;
	public static Bitmap bm_sunlight = null;
	public static Bitmap bm_sunlightPressed = null;
	public static Bitmap bm_splash_spin = null;
	
	
	// entities
	public static Sprite sprite_splash= null;
	public static Sprite sprite_egg = null;
	public static Sprite sprite_eggCrack = null;
	public static Sprite sprite_eggUnstable = null;
	public static Sprite sprite_eggSleep = null;
	public static Sprite sprite_young_angry = null;
	public static Sprite sprite_young_happy = null;
	public static Sprite sprite_young_hungry = null;
	public static Sprite sprite_young_sad = null;
	public static Sprite sprite_young_sleep = null;
	public static Sprite sprite_young_walkingLeft = null;
	public static Sprite sprite_young_walkingRight = null;
	public static Sprite sprite_infant_angry = null;
	public static Sprite sprite_infant_eating = null;
	public static Sprite sprite_infant_happy = null;
	public static Sprite sprite_infant_hungry = null;
	public static Sprite sprite_infant_sad = null;
	public static Sprite sprite_infant_sleep = null;
	public static Sprite sprite_infant_walkingLeft = null;
	public static Sprite sprite_infant_walkingRight = null;
	public static Sprite sprite_adult = null;
	public static Sprite sprite_sunlight = null;
	public static Sprite sprite_sunlightPresssed = null;
	public static Sprite sprite_splash_spin = null;
	
	//entity stage
	public static EntityStage entityStage = EntityStage.EGG; 
	
	//entity state
	public static EntityState entityState = EntityState.SLEEP;
	
	
	//sound effects
	public static SoundPool sp = null;
	public static int sp_hungry;
	public static boolean sp_hungry_played = false;
	
	public static MediaPlayer mp_hungry;
	public static MediaPlayer mp_happy;
	
	
	//position of the entity
	public static int posX = 0, posY = 0;
	
	// state times
	public static final int stateTime_sleep = FPS*10; // total FPS == 1 second
	public static final int stateTime_happy = FPS*10;
	public static final int stateTime_hungry = FPS*10;
	public static final int stateTime_angry = FPS*10;
	public static final int stateTime_sad = FPS*10;
	public static final int stateTime_eating = FPS*5;
	
	// stage times
	public static final int stageTime_egg = FPS*20;
	public static final int stageTime_eggCrack = FPS;
	public static final int stageTime_infant = FPS*40;
	public static final int stageTime_young = FPS*300;
	public static final int stageTime_adult = FPS*200;
	
	//state Timers
	public static int stateTimer = 0;
	
	//stage timers
	public static int stageTimer = 0;
	
	
	public static AndroidTTS tts = null;
	public static boolean playtts = true;
	
	// for the service
	public static boolean serviceIsProcessing = false;
	
	public static void intiGlobal(Context context){
		
		tts = null;
		tts = new AndroidTTS(context);
		Global.context = context;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		windowWidth  = metrics.widthPixels;
		windowHeight = metrics.heightPixels;
		
		//preparing soundpool audio
		mp_hungry = MediaPlayer.create(context, R.raw.hungry);
		mp_hungry.setLooping(true);
		mp_hungry.setVolume(0, 0);
		mp_hungry.start();
		
		mp_happy = MediaPlayer.create(context, R.raw.happy);
		mp_happy.setLooping(true);
		mp_happy.setVolume(0, 0);
		mp_happy.start();
		
		
		// trying to know if the game has already been started before or if it is started anew
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (context);
		if(sprefs.getInt("key_is_game_new", 0) == 0){
			isGameNew = true;
			entityStage = EntityStage.EGG;
		}else{	
			isGameNew = false;
		}
		//Log.i(DEBUG_TAG, "Global : Getting isGameNew = " + isGameNew);
		

		playtts = sprefs.getBoolean("key_play_tts", true);
				
		// preparing bitmaps
		bm_splash = BitmapFactory.decodeResource(context.getResources(),R.drawable.splash2);
		bm_background = BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
		bm_button_start = BitmapFactory.decodeResource(context.getResources(),R.drawable.button_start);
		bm_button_prefs = BitmapFactory.decodeResource(context.getResources(),R.drawable.button_prefs);
		bm_egg = BitmapFactory.decodeResource(context.getResources(),R.drawable.egg);
		bm_eggUnstable = BitmapFactory.decodeResource(context.getResources(),R.drawable.egg_unstable);
		bm_eggSleep = BitmapFactory.decodeResource(context.getResources(),R.drawable.egg_sleep);
		bm_eggCrack = BitmapFactory.decodeResource(context.getResources(),R.drawable.egg_crack);
		bm_sunlight = BitmapFactory.decodeResource(context.getResources(),R.drawable.sunlight);
		bm_sunlightPressed = BitmapFactory.decodeResource(context.getResources(),R.drawable.sunlight_pressed);
		bm_young_angry = BitmapFactory.decodeResource(context.getResources(),R.drawable.young_angry);
		bm_young_happy = BitmapFactory.decodeResource(context.getResources(),R.drawable.young_happy);
		bm_young_hungry = BitmapFactory.decodeResource(context.getResources(),R.drawable.young_hungry);
		bm_young_sad = BitmapFactory.decodeResource(context.getResources(),R.drawable.young_sad);
		bm_young_sleep = BitmapFactory.decodeResource(context.getResources(),R.drawable.young_sleep);
		bm_infant_angry = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_angry);
		bm_infant_happy = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_happy);
		bm_infant_hungry = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_hungry);
		bm_infant_sad = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_sad);
		bm_infant_sleep = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_sleep);
		bm_infant_eating = BitmapFactory.decodeResource(context.getResources(),R.drawable.infant_eating);
		bm_splash_spin = BitmapFactory.decodeResource(context.getResources(),R.drawable.spinsplash);
		
		posX = windowWidth/2;
		posY = 4*windowHeight/5 - 80;
		
		//preparing sprites
		Global.sprite_splash = new Sprite(
				Global.bm_splash
				, Global.windowWidth/2-(Global.bm_splash.getWidth()/13)/2, Global.windowHeight/2-Global.bm_splash.getHeight()/2	// initial position
				, 13);	//number of frames in the animation
		Global.sprite_splash_spin = new Sprite(
				Global.bm_splash_spin
				, Global.windowWidth/2-(Global.bm_splash.getWidth()/13)/2, Global.windowHeight/4-Global.bm_splash.getHeight()/2	// initial position
				, 13);	//number of frames in the animation
		
		Global.sprite_egg = new Sprite(Global.bm_egg
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		
		Global.sprite_eggUnstable = new Sprite(Global.bm_eggUnstable
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_eggSleep = new Sprite(Global.bm_eggSleep
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_eggCrack = new Sprite(Global.bm_eggCrack
				, posX, posY	// initial position
				, 6);	//number of frames in the animation)
		
		Global.sprite_sunlight = new Sprite(Global.bm_sunlight
				, windowWidth - Global.bm_sunlight.getWidth()/5, 0	// initial position
				, 5);	//number of frames in the animation)

		Global.sprite_sunlightPresssed = new Sprite(Global.bm_sunlightPressed
				, windowWidth - Global.bm_sunlightPressed.getWidth()/5, 0	// initial position
				, 5);	//number of frames in the animation)
		
		Global.sprite_young_angry = new Sprite(Global.bm_young_angry
				, posX, posY	// initial position
				, 3);	//number of frames in the animation)
		Global.sprite_young_happy = new Sprite(Global.bm_young_happy
				, posX, posY	// initial position
				, 4);	//number of frames in the animation)
		Global.sprite_young_hungry = new Sprite(Global.bm_young_hungry
				, posX, posY	// initial position
				, 3);	//number of frames in the animation)
		Global.sprite_young_sad = new Sprite(Global.bm_young_sad
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_young_sleep = new Sprite(Global.bm_young_sleep
				, posX, posY	// initial position
				, 4);	//number of frames in the animation)
		
		
		Global.sprite_infant_angry = new Sprite(Global.bm_infant_angry
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_infant_eating = new Sprite(Global.bm_infant_eating
				, posX, posY	// initial position
				, 3);	//number of frames in the animation)
		Global.sprite_infant_happy = new Sprite(Global.bm_infant_happy
				, posX, posY	// initial position
				, 3);	//number of frames in the animation)
		Global.sprite_infant_hungry = new Sprite(Global.bm_infant_hungry
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_infant_sad = new Sprite(Global.bm_infant_sad
				, posX, posY	// initial position
				, 5);	//number of frames in the animation)
		Global.sprite_infant_sleep = new Sprite(Global.bm_infant_sleep
				, posX, posY	// initial position
				, 4);	//number of frames in the animation)
		
		
		
	}
}