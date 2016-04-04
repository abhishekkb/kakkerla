package com.kakkerla.hm5_kakkerla;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
	public static GameState gameState;
	public static GameRunState gameRunState;
	public static GamePanel gamePanel;
	public static int FPS = 10; 
	public static String DEBUG_TAG = "ProjectLogging";
	public static Canvas canvas = null;
	public static Bitmap backGround_bm;
	public static Bitmap logo_bm;
	public static Bitmap newGame_bm;
	public static Bitmap continueGame_bm;
	public static Bitmap highScore_bm;
	public static Bitmap food_bm;
	public static Bitmap getReady_bm;
	public static Bitmap getReadyRoach_bm;
	public static Bitmap resume_bm;
	public static Bitmap pause_bm;
	public static Bitmap gameOver_bm;
	public static Bitmap gameOverRoach_bm;
	public static Bitmap highScoreReach_bm;
	
	public static List<Entity> roaches;
	public static Entity bigRoach;
	public static Entity ladyBug;
	public static Entity oneUp;
	//public static int height, width;
	
	public static MediaPlayer mp;
	public static boolean mp_enable;
	
	public static SoundPool sp;
	public static boolean sp_enable;
	public static int spId_getReady;
	public static int spId_squishRoach;
	public static int spId_squishBigRoach;
	public static int spId_squishLadyBug;
	public static int spId_click;
	public static int spId_highScore;
	public static int spId_gameOver;
	
	public static int idGen = 0;
	public static int highScore, currentScore=0, livesLeft=3;
	public static int windowWidth, windowHeight;
	public static Context context;
	public static boolean getReadyOrGameOverPlayed = false;
	public static boolean highScorePlayed = false;
	public static boolean inPrefsScreen = false;
	
	
	public static long bigRoachTimer_start = 0;
	public static long ladyBugTimer_start = 0;
	public static long oneUpTimer_start = 0;
	public static long bigRoachTimer_end = 0;
	public static long ladyBugTimer_end = 0;
	public static long oneUpTimer_end = 0;
	
	public static void setGlobal(Context context){
		Global.context = context;
		Log.i(Global.DEBUG_TAG,"Global - setting Global values");
		
		// setting game state
		gameState = GameState.GAME_HOME;
		gameRunState = GameRunState.GAMERUN_RUNNING;
		
		// getting the dimensions of the window
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		windowWidth  = metrics.widthPixels;
		windowHeight = metrics.heightPixels;
		/*
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        */
		
		// Preparing bitmaps
		backGround_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
		//backGround_bm = getResizedBitmap(backGround_bm, windowWidth, backGround_bm.getHeight()*windowWidth/backGround_bm.getWidth());
		logo_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo1);
		logo_bm = getResizedBitmap(logo_bm, windowWidth, logo_bm.getHeight()*windowWidth/logo_bm.getWidth());
		newGame_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.leaf_newgame1);
		highScore_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.leaf_highscore1);
		continueGame_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.leaf_continue1);
		food_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.food);
		food_bm = getResizedBitmap(food_bm, windowWidth, food_bm.getHeight()*windowWidth/food_bm.getWidth());
		getReady_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.getready1);
		getReady_bm = getResizedBitmap(getReady_bm, windowWidth, getReady_bm.getHeight()*windowWidth/getReady_bm.getWidth());
		getReadyRoach_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.roach_worried);
		resume_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.resume1);
		pause_bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause1);
		gameOver_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.gameover);
		gameOver_bm = getResizedBitmap(gameOver_bm, windowWidth, gameOver_bm.getHeight()*windowWidth/gameOver_bm.getWidth());
		gameOverRoach_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.roach_happy);
		highScoreReach_bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.highscore);
		highScoreReach_bm = getResizedBitmap(highScoreReach_bm, windowWidth, highScoreReach_bm.getHeight()*windowWidth/highScoreReach_bm.getWidth());
       
		// preparing bugs and their parameters
		roaches = new ArrayList<Entity>();
		for(int i = 0; i < 5 ; i++){
			roaches.add(new Entity(EntityType.ROACH, context, 2*i+1));
		}
		bigRoach = new Entity(EntityType.BIG_ROACH, context, 10);
		ladyBug = new Entity(EntityType.LADY_BUG, context, 10);
		oneUp = new Entity(EntityType.ONE_UP, context, 5);
		
		// preparing audio options
        // TODO get these from preferences.
		sp_enable = true;
		mp_enable = true;
		mp = MediaPlayer.create(context, R.raw.background);
		mp.setLooping(true);
		mp.start();
		
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		spId_getReady = sp.load(context, R.raw.getreadytoexterminate, 1);
		spId_squishRoach = sp.load(context, R.raw.squish_roach, 1);
		spId_squishBigRoach = sp.load(context, R.raw.squish_bigroach, 1);
		spId_squishLadyBug = sp.load(context, R.raw.squish_ladybug2, 1);
		spId_click = sp.load(context, R.raw.click, 1);
		spId_highScore = sp.load(context, R.raw.highscore, 1);
		spId_gameOver = sp.load(context, R.raw.gameover, 1);
		
		// getting high scores from shared preferencess
		SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (context);
		highScore = sprefs.getInt("key_high_score", 0);
		Log.i(DEBUG_TAG, "Global : Getting HighScore = " + highScore);	
		
	}
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	public static void resetGame(){
		// to start a new game
		
		// resetting entities
		for(Entity roach : Global.roaches){
			roach.resetEntity();
		}
		bigRoach.resetEntity();
		ladyBug.resetEntity();
		oneUp.resetEntity();
		
		// resetting scores
		livesLeft = 3;
		if(currentScore > highScore){
			highScore = currentScore;
			SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (Global.context);
			Editor editor = sprefs.edit();
			editor.putInt("key_high_score", highScore);
			editor.commit();
		}
		currentScore = 0;
	}

}
