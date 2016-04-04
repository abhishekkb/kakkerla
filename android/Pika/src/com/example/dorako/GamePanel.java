package com.example.dorako;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GamePanel extends SurfaceView{		// implements SurfaceHolder.Callback{
	
	public GameThread thread = null;
	Paint drawPaint = null ;
	Canvas canvas = null;
	Context context = null;
	
	public GamePanel(Context context) {
		super(context);
		Log.i(Global.DEBUG_TAG,"GamePanel - constructor");
		this.context = context;
		//Global.gamePanel = this;

	}
	public void resume(){
		thread = new GameThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
		setFocusable(true);
	}

	public void pause(){
		Log.i(Global.DEBUG_TAG,"GamePanel - on surface destroy");
		thread.setRunning(false);
		while(true){
			try {
				thread.join();
				Log.i(Global.DEBUG_TAG,"GamePanel - on surface destroy - thread.join");
			} catch (InterruptedException ie) {
				Log.i(Global.DEBUG_TAG,"GamePanel - on surface destroy : interrupted exception");
			}
			break;
		}
		thread =  null;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.i(Global.DEBUG_TAG,"GamePanel - on touch event");
		int width  = Global.windowWidth;
		int height = Global.windowHeight;
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			float y = event.getY();
			
			if(Global.gameState == GameState.HOME){
				
				//if clicked on start 
					// change game state to RUN
				if(inBounds(event, Global.windowWidth/2 - Global.bm_button_start.getWidth()/2, Global.windowHeight/5, 
						Global.bm_button_start.getWidth(), Global.bm_button_start.getHeight())){
					
					Global.gameState = GameState.RUN;
					
				}				
				//if click on prefs
					// change game state to prefs
				if(inBounds(event, Global.windowWidth/2 - Global.bm_button_prefs.getWidth()/2, Global.windowHeight/5 + Global.bm_button_start.getHeight()+20, 
						Global.bm_button_prefs.getWidth(), Global.bm_button_prefs.getHeight())){
					Global.gameState = GameState.HOME2PREFS;
					Log.i(Global.DEBUG_TAG, "GamePanel - TouchEvent - x:"+event.getX()+", y = "+event.getY() );
					
				}	
			}
			if(Global.gameState == GameState.RUN){
				//if clicked on sunlight
				if(inBounds(event, Global.windowWidth - Global.bm_sunlight.getWidth()/5, 0,
						Global.bm_sunlight.getWidth()/5, Global.bm_sunlight.getHeight())){
					if(Global.entityState != EntityState.SLEEP){
						//if already in eat state
						if(Global.entityState == EntityState.EATING){
							Global.stateTimer = Global.stateTimer-10;
						}else{
							Global.entityState = EntityState.EATING;
							Global.stateTimer = 0;
						}
						Global.foodTouched = true;
					}
				}
				// if clicked on prefs button
				if(inBounds(event, 0, 0, Global.bm_button_prefs.getWidth(), Global.bm_button_prefs.getHeight())){
					Global.gameState = GameState.RUN2PREFS;
					
				}
			}
		}
		return true;
	}
	
	private boolean inBounds(MotionEvent event,int x, int y, int width, int height) {
    	float eX = event.getX();
    	float eY = event.getY();
        if(eX > x && eX < x + width - 1 && eY > y && eY < y + height - 1) 
            return true;
        else
            return false;
    }
	
	public void update(Canvas canvas) {
		Log.i(Global.DEBUG_TAG, "GamePanel - update - state : "+Global.gameState+", stage : "+Global.entityStage+", state : " +Global.entityState );
		
		if(Global.gameState == GameState.SPLASH){
			updateSplash(canvas);
		}else if(Global.gameState == GameState.HOME){
			//updateHome(canvas);
		}else if(Global.gameState == GameState.HOME2PREFS){
			Log.i(Global.DEBUG_TAG, "GamePanel - opening prefs");
			context.startActivity(new Intent(context, PrefsActivity.class));
			//Global.inPrefsScreen = true;
		}else if(Global.gameState == GameState.RUN2PREFS){
			Log.i(Global.DEBUG_TAG, "GamePanel - opening prefs");
			context.startActivity(new Intent(context, PrefsActivity.class));
			//Global.inPrefsScreen = true;
		}else if(Global.gameState == GameState.RUN){
			updateRun(canvas);
		}
	}
	public void draw(Canvas canvas){
		//canvas.drawColor(Color.BLUE);
		
		if(Global.gameState == GameState.SPLASH){
			drawSplash(canvas);
		}else if(Global.gameState == GameState.HOME){
			drawHome(canvas);
		}else if(Global.gameState == GameState.RUN){
			drawRun(canvas);
		}
		
	}
	
	
	public void updateRun(Canvas canvas){
		if(Global.entityState == EntityState.HUNGRY && Global.entityStage != EntityStage.EGG ){
			if(Global.tts.isSpeaking() == false){
				Global.mp_hungry.setVolume(0.5f, 0.5f);
			}else{
				Global.mp_hungry.setVolume(0, 0);
			}
		}else{
			Global.mp_hungry.setVolume(0, 0);
		}
		
		if(Global.entityState == EntityState.HAPPY && Global.entityStage != EntityStage.EGG ){
			if(Global.tts.isSpeaking() == false){
				Global.mp_happy.setVolume(1, 1);
			}else{
				Global.mp_happy.setVolume(0, 0);
			}
		}else{
			Global.mp_happy.setVolume(0, 0);
		}
		
		
		
		Global.stateTimer++;
		if(Global.entityStage == EntityStage.EGGCRACK ){
			Global.stageTimer++;
		}else{
			if(Global.foodTouched){
				Global.stageTimer++;
			}
		}
		switch(Global.entityStage){
		case EGG:
			updateEgg(canvas);
			if(Global.stageTimer >= Global.stageTime_egg){
				//change state
				Global.entityStage = EntityStage.EGGCRACK;
				Global.stageTimer = 0;
				Global.stateTimer = 0;
			}
			break;
		case EGGCRACK:
			Global.sprite_eggCrack.update(System.currentTimeMillis());
			if(Global.stageTimer >= Global.stageTime_eggCrack){
				//change state
				Global.entityStage = EntityStage.INFANT;
				Global.entityState = EntityState.HAPPY;
				Global.stageTimer = 0;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;
		case INFANT:
			updateInfant(canvas);
			if(Global.stageTimer >= Global.stageTime_infant){
				//change state
				Global.entityStage = EntityStage.YOUNG;
				Global.entityState = EntityState.HAPPY;
				Global.stageTimer = 0;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;
		case YOUNG:
			updateYoung(canvas);
			if(Global.stageTimer >= Global.stageTime_young){
				//change state
				Global.entityStage = EntityStage.ADULT;
				Global.entityState = EntityState.HAPPY;
				Global.stageTimer = 0;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;
		case ADULT:
			//TODO
			break;
		}
		if(Global.foodTouched == false){
			Global.sprite_sunlight.update(System.currentTimeMillis());
		}else{
			Global.sprite_sunlightPresssed.update(System.currentTimeMillis());
		}
		
	}
	public void drawRun(Canvas canvas){
		canvas.drawBitmap(Global.bm_background, 0, 0, null);
		canvas.drawBitmap(Global.bm_button_prefs, 0, 0, null);
		
		/*
		String text = "stage: " + Global.entityStage +", state : "+Global.entityState ;
		drawTextOnCanvas(canvas, text, Global.windowWidth/10, Global.windowHeight/2);
		
		text = "staGe Timer: " + Global.stageTimer +", staTe Timer: "+Global.stateTimer ;
		drawTextOnCanvas(canvas, text, Global.windowWidth/10 , Global.windowHeight/2 + 80);
		*/
		
		if(Global.foodTouched == false){
			Global.sprite_sunlight.draw(canvas);
		}else{
			Global.sprite_sunlightPresssed.draw(canvas);
		}
		
		switch(Global.entityStage){
		case EGG:
			drawEgg(canvas);
			//Log.i(Global.DEBUG_TAG, "GamePanel - Run - draw - egg");
			break;
		case EGGCRACK:
			Global.sprite_eggCrack.draw(canvas);
			break;
		case INFANT:
			drawInfant(canvas);
			break;
		case YOUNG:
			drawYoung(canvas);
			break;
		case ADULT:
			//TODO
			break;
		}
	}
	/*************************************/
	public void updateEgg(Canvas canvas){
		switch(Global.entityState){
		case SLEEP:
			Global.sprite_eggSleep.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HUNGRY;
				Global.stateTimer = 0;
			}
			break;
		case EATING:
			Global.sprite_egg.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HAPPY;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_eggUnstable.update(System.currentTimeMillis());
			break;
		case HUNGRY: // needs sunlight
			
			Global.sprite_eggUnstable.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_hungry){
				Global.entityState = EntityState.ANGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
			}
			break;
		case HAPPY:

			Global.sprite_egg.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_happy){
				Global.entityState = EntityState.HUNGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
				
			}
			break;
		}
		
	}
	public void drawEgg(Canvas canvas){
		switch(Global.entityState){
		case SLEEP:
			Global.sprite_eggSleep.draw(canvas);
			break;
		case EATING:
			Global.sprite_egg.draw(canvas);
			break;
		case HUNGRY: // needs sunlight
			Global.sprite_eggUnstable.draw(canvas);
			break;
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_eggUnstable.draw(canvas);
			break;
		case HAPPY:
			Global.sprite_egg.draw(canvas);
			break;
		}
	}
	/*************************************/
	public void updateInfant(Canvas canvas){
		switch(Global.entityState){
		case SLEEP:
			Global.sprite_infant_sleep.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HAPPY;
				Global.stateTimer = 0;
			}
			break;
		case EATING:
			Global.sprite_infant_eating.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HAPPY;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;	
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_infant_angry.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_hungry){
				Global.entityState = EntityState.SAD;
				Global.stateTimer = 0;
			}
			break;
		case HUNGRY: // needs sunlight
			if(Global.isttsplayed== false){
				if(Global.playtts){
					Global.tts.speak("I am very Hungry", true);
				}
				Global.isttsplayed = true;
			}
			Global.sprite_infant_hungry.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_hungry){
				Global.entityState = EntityState.ANGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
			}
			break;
		case HAPPY:
			if(Global.isttsplayed== false){
				if(Global.playtts){
					Global.tts.speak("I am very Happy", true);
				}
				Global.isttsplayed = true;
			}
			Global.sprite_infant_happy.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_happy){
				Global.entityState = EntityState.HUNGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
			}
			break;
		case SAD:
			Global.sprite_infant_sad.update(System.currentTimeMillis());
			break;
		}
	}
	public void drawInfant(Canvas canvas){
		switch(Global.entityState){
		case SLEEP:
			Global.sprite_infant_sleep.draw(canvas);
			break;
		case EATING:
			Global.sprite_infant_eating.draw(canvas);
			break;
		case HUNGRY: // needs sunlight
			Global.sprite_infant_hungry.draw(canvas);
			break;
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_infant_angry.draw(canvas);
			break;
		case HAPPY:
			Global.sprite_infant_happy.draw(canvas);
			break;
		case SAD:
			Global.sprite_infant_sad.draw(canvas);
			break;
		}
	}
	/*************************************/
	public void updateYoung(Canvas canvas){
		switch(Global.entityState){
		case SLEEP:
			Global.sprite_young_sleep.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HAPPY;
				Global.stateTimer = 0;
			}
			break;
		case EATING:
			Global.sprite_young_happy.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_sleep){
				Global.entityState = EntityState.HAPPY;
				Global.stateTimer = 0;
				Global.foodTouched = false;
			}
			break;	
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_young_angry.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_hungry){
				Global.entityState = EntityState.SAD;
				Global.stateTimer = 0;
			}
			break;
		case HUNGRY: // needs sunlight
			if(Global.isttsplayed== false){
				if(Global.playtts){
					Global.tts.speak("I am very Hungry", true);
				}
				Global.isttsplayed = true;
			}
			Global.sprite_young_hungry.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_hungry){
				Global.entityState = EntityState.ANGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
			}
			break;
		case HAPPY:
			if(Global.isttsplayed== false){
				if(Global.playtts){
					Global.tts.speak("I am very Happy", true);
				}
				Global.isttsplayed = true;
			}
			Global.sprite_young_happy.update(System.currentTimeMillis());
			if(Global.stateTimer >= Global.stateTime_happy){
				Global.entityState = EntityState.HUNGRY;
				Global.stateTimer = 0;
				Global.isttsplayed = false;
			}
			break;
		case SAD:
			Global.sprite_young_sad.update(System.currentTimeMillis());
			break;
		}
	}
	public void drawYoung(Canvas canvas){

		switch(Global.entityState){
		case SLEEP:
			Global.sprite_young_sleep.draw(canvas);
			break;
		case EATING:
			Global.sprite_young_happy.draw(canvas);
			break;
		case HUNGRY: // needs sunlight
			Global.sprite_young_hungry.draw(canvas);
			break;
		case ANGRY: // actually it is unstable, and needs some sunlight
			Global.sprite_young_angry.draw(canvas);
			break;
		case HAPPY:
			Global.sprite_young_happy.draw(canvas);
			break;
		case SAD:
			Global.sprite_young_sad.draw(canvas);
			break;
		}
	}
	/////////////////////////////////////////////
	/////////////////////////////////////////////
	/////////////////////////////////////////////
	
	/*************************************/
	public void updateSplash(Canvas canvas){
		Global.sprite_splash.update(System.currentTimeMillis());
		Global.sprite_splash_spin.update(System.currentTimeMillis());
		if(Global.exitSplash == true){
			//change state to game home
			Global.gameState = GameState.HOME;
			Global.exitSplash = false;
		}
	}
	public void drawSplash(Canvas canvas){
		canvas.drawColor(Color.BLACK);
		Global.sprite_splash.draw(canvas);
		//Global.sprite_splash_spin.draw(canvas);
	}
	/*************************************/
	public void updateHome(Canvas canvas){
		// do nothing
		//
		//
	}
	public void drawHome(Canvas canvas) {
		// background
		canvas.drawBitmap(Global.bm_background, 0, 0, null);
		
		//start button
		canvas.drawBitmap(Global.bm_button_start, Global.windowWidth/2 - Global.bm_button_start.getWidth()/2,
				Global.windowHeight/5, null);
		
		//prefs button
		canvas.drawBitmap(Global.bm_button_prefs, Global.windowWidth/2 - Global.bm_button_prefs.getWidth()/2,
				Global.windowHeight/5 + Global.bm_button_start.getHeight()+20, null);
	}
	/*************************************/
	public void drawTextOnCanvas(Canvas canvas, String text, float x, float y){
		Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paintText.setColor(Color.BLACK);
	    paintText.setTextSize(25);
	    paintText.setStyle(Style.FILL);
	    //paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
	    
	    Rect rectText = new Rect();
	    paintText.getTextBounds(text, 0, text.length(), rectText);
		canvas.drawText(text, x, y, paintText);
	}
	
	
}
