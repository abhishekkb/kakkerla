package com.kakkerla.hm5_kakkerla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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
	
	public GamePanel(Context context) {
		super(context);
		Log.i(Global.DEBUG_TAG,"GamePanel - constructor");
		
		// for showing lives
		drawPaint = new Paint();
        drawPaint.setColor(Color.GREEN);
        drawPaint.setAntiAlias(true);
        drawPaint.setTextSize((int) (20));
        
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
			Log.i(Global.DEBUG_TAG,"GamePanel - onTouchEvent - touched at x: "+x+" y: "+y);
			
			
			if(Global.gameState == GameState.GAME_RUN){
				boolean flag = false;
				if(Global.gameRunState == GameRunState.GAMERUN_PAUSED){
					if(true != inBounds(event, Global.windowWidth/2, 0, Global.pause_bm.getWidth(), Global.pause_bm.getHeight())){
						// if click event did not happen on the resume button then do nothing and return true
						return true;
					}
				}
				if(inBounds(event, Global.windowWidth/2, 0, Global.pause_bm.getWidth(), Global.pause_bm.getHeight())){
					if(Global.gameRunState == GameRunState.GAMERUN_PAUSED){
						Global.gameRunState = GameRunState.GAMERUN_RUNNING;
					}else{
						Global.gameRunState = GameRunState.GAMERUN_PAUSED;
					}
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
					flag = true;
					return true;
				}
				for(Entity roach: Global.roaches){
					if(roach.inBounds(event)){
						if(roach.hitIncrease()){
							Global.currentScore += 1;
							Global.sp.play(Global.spId_squishRoach, 1, 1, 1, 0, 1);
							flag = true;
						}
						Log.i(Global.DEBUG_TAG,"GamePanel - onTouchEvent - eww!! crushed a roach :D");
					}
				}
				if(Global.bigRoach.es == EntityState.ACTIVE){
					if(Global.bigRoach.inBounds(event)){
						if(Global.bigRoach.hitIncrease()){
							Global.currentScore += 10;
						}
						Global.sp.play(Global.spId_squishBigRoach, 1, 1, 1, 0, 1);
						flag = true;
						Log.i(Global.DEBUG_TAG,"GamePanel - onTouchEvent - eww!! crushed a big roach :D");
					}
				}
				if(Global.ladyBug.es == EntityState.ACTIVE){
					if(Global.ladyBug.inBounds(event)){
						Global.ladyBug.hitIncrease();
						Log.i(Global.DEBUG_TAG,"GamePanel - onTouchEvent - oh! no, crushed a lady bug :(");
						//change game state to gameover
						// TODO change game to game over state
						Global.sp.play(Global.spId_squishLadyBug, 1, 1, 1, 0, 1);
						Global.gameState = GameState.GAME_OVER;
						flag = true;
					}
				}
				if(Global.oneUp.es == EntityState.ACTIVE){
					if(Global.oneUp.inBounds(event)){
						Global.oneUp.hitIncrease();
						Global.livesLeft++;
						Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
						flag = true;
					}
				}
				
				if(flag != true){
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
				}
			}else if(Global.gameState == GameState.GAME_HOME){
				//check if the touch is inbounds to the buttons
				//for both "new game" and "highscore"
				
				// for new game
				if( inBounds(event, width/4, height/4, Global.newGame_bm.getWidth(),Global.newGame_bm.getHeight()) ){
					Global.resetGame();
					Global.gameState = GameState.GAME_READY;
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
					Global.bigRoachTimer_start = Global.ladyBugTimer_start = Global.ladyBugTimer_start = System.nanoTime();
				}
				// for high score
				// open preferences
				if( inBounds(event, width/4, height/4 + Global.newGame_bm.getHeight(), Global.highScore_bm.getWidth(),Global.highScore_bm.getHeight()) ){
					Global.gameState = GameState.GAME_HOME2PREF;
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
				}
				
			}else if(Global.gameState == GameState.GAME_PAUSE){
				//check if the touch is inbounds to the buttons
				//for all "new game" , "continue game" and "highscore"
				if( inBounds(event, width/4, height/4, Global.continueGame_bm.getWidth(),Global.continueGame_bm.getHeight()) ){
					Global.gameState = GameState.GAME_RUN;
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
				}
				if( inBounds(event, width/4, height/4+Global.continueGame_bm.getHeight(), Global.newGame_bm.getWidth(),Global.newGame_bm.getHeight()) ){
					Global.resetGame();
					Global.gameState = GameState.GAME_READY;
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
				}
				// for high score
				// open preferences
				if( inBounds(event, width/4, height/4 + Global.continueGame_bm.getHeight()+ Global.newGame_bm.getHeight(), Global.highScore_bm.getWidth(),Global.highScore_bm.getHeight()) ){
					Global.gameState = GameState.GAME_HOME2PREF;
					Global.sp.play(Global.spId_click, 1, 1, 1, 0, 1);
				}
			}else if(Global.gameState == GameState.GAME_OVER){
				Global.gameState = GameState.GAME_HOME;
			}
		}
		return true;//super.onTouchEvent(event);
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
		if(Global.currentScore > Global.highScore){
			Global.highScore = Global.currentScore;
			SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (Global.context);
			Editor editor = sprefs.edit();
			editor.putInt("key_high_score", Global.highScore);
			editor.commit();
		}
		if(Global.gameState == GameState.GAME_HOME){
			updateHome(canvas);
			Global.getReadyOrGameOverPlayed = false;
			Global.highScorePlayed = false;
		}else if(Global.gameState == GameState.GAME_READY){
			updateReady(canvas);
		}else if(Global.gameState == GameState.GAME_RUN){
			updateRun(canvas);
			Global.getReadyOrGameOverPlayed = false;
			Global.highScorePlayed = false;
		}else if(Global.gameState == GameState.GAME_PAUSE){
			updatePause(canvas);
		}else if(Global.gameState == GameState.GAME_RUN2PREF || Global.gameState == GameState.GAME_HOME2PREF){
			// open an intent to preferences activity
			if(Global.inPrefsScreen == false){
				Intent intent = new Intent(Global.context, PrefsActivity.class);
				Global.context.startActivity(intent);
				Global.inPrefsScreen = true;
			}
		}else if(Global.gameState == GameState.GAME_OVER){
			if(Global.currentScore >= Global.highScore){
				if(Global.highScorePlayed == false){
					Global.sp.play(Global.spId_highScore, 1, 1, 1, 0, 1);
					Global.highScorePlayed = true;
				}
			}
		}
	}

	@Override
	public void draw(Canvas canvas){
		if(Global.currentScore > Global.highScore){
			Global.highScore = Global.currentScore;
			SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences (Global.context);
			Editor editor = sprefs.edit();
			editor.putInt("key_high_score", Global.highScore);
			editor.commit();
		}
		if(Global.gameState == GameState.GAME_HOME){
			drawHome(canvas);
		}else if(Global.gameState == GameState.GAME_READY){
			drawReady(canvas);
		}else if(Global.gameState == GameState.GAME_RUN){
			drawRun(canvas);
		}else if(Global.gameState == GameState.GAME_PAUSE){
			drawPause(canvas);
		}else if(Global.gameState == GameState.GAME_OVER){
			drawGameOver(canvas);
		}
	}
	public void drawGameOver(Canvas canvas){
		canvas.drawBitmap(Global.backGround_bm, 0, 0, null);
		canvas.drawBitmap(Global.gameOver_bm, 0, Global.windowHeight/5,null);
		canvas.drawBitmap(Global.gameOverRoach_bm, Global.windowWidth/3, Global.windowHeight/5+Global.getReady_bm.getHeight(),null);
		if(Global.getReadyOrGameOverPlayed == false){
			Global.sp.play(Global.spId_gameOver, 1, 1, 1, 0, 1);
			Global.getReadyOrGameOverPlayed = true;
		}
		String text = "You Scored " + Global.currentScore + " points";
		
		drawTextOnCanvas(canvas, text, Global.windowWidth/8, Global.windowHeight/3+Global.gameOverRoach_bm.getHeight()+10);
		
		text = "Click anywhere to continue ...";
		drawTextOnCanvas(canvas, text, Global.windowWidth/8, Global.windowHeight/3+Global.gameOverRoach_bm.getHeight()+100);
		if(Global.currentScore >= Global.highScore){
			canvas.drawBitmap(Global.highScoreReach_bm, 0, Global.windowHeight-Global.highScoreReach_bm.getHeight(),null);
		}
		/*
		if(Global.currentScore > Global.highScore){
			Toast.makeText(Global.context, "You have reached the new high score", Toast.LENGTH_LONG).show();
		}else if(Global.currentScore == Global.highScore){
			Toast.makeText(Global.context, "You have reached the (not new) high score", Toast.LENGTH_LONG).show();
		}
		*/
	}
	private void drawReady(Canvas canvas) {	
		canvas.drawBitmap(Global.backGround_bm, 0, 0, null);
		canvas.drawBitmap(Global.getReady_bm, 0, Global.windowHeight/5,null);
		canvas.drawBitmap(Global.getReadyRoach_bm, Global.windowWidth/3, Global.windowHeight/5+Global.getReady_bm.getHeight(),null);
		if(Global.getReadyOrGameOverPlayed == false){
			Global.sp.play(Global.spId_getReady, 1, 1, 1, 0, 1);
			Global.getReadyOrGameOverPlayed = true;
		}
	}
	private void drawHome(Canvas canvas) {
		canvas.drawBitmap(Global.backGround_bm, 0, 0, null);
		canvas.drawBitmap(Global.logo_bm, 0, 0, null);
		Bitmap newGame = Global.newGame_bm;
		int width  = Global.windowWidth;
		int height = Global.windowHeight;
		//Log.i(Global.DEBUG_TAG,"GamePanel - ondrawhome - draw leaf at"+ (Global.windowWidth/5) +", "+ (Global.windowHeight/5 + newGame.getHeight()));
		canvas.drawBitmap(newGame, width/4, height/4 , null);
		Bitmap highScore = Global.highScore_bm;
		canvas.drawBitmap(highScore, width/4, height/4 + newGame.getHeight(), null);
		
	}
	private void drawPause(Canvas canvas) {
		canvas.drawBitmap(Global.backGround_bm, 0, 0, null);
		canvas.drawBitmap(Global.logo_bm, 0, 0, null);
		int width  = Global.windowWidth;
		int height = Global.windowHeight;
		//Log.i(Global.DEBUG_TAG,"GamePanel - ondrawhome - drawleafat"+ (Global.windowWidth/5) +", "+ (Global.windowHeight/5 + newGame.getHeight()));
		Bitmap continueGame = Global.continueGame_bm;
		canvas.drawBitmap(continueGame, width/4, height/4 , null);
		Bitmap newGame = Global.newGame_bm;
		canvas.drawBitmap(newGame, width/4, height/4+continueGame.getHeight() , null);
		Bitmap highScore = Global.highScore_bm;
		canvas.drawBitmap(highScore, width/4, height/4 + continueGame.getHeight()+newGame.getHeight(), null);
	}

	private void drawRun(Canvas canvas) {
		//drawing background
		canvas.drawBitmap(Global.backGround_bm, 0, 0, null);
		String text = "Score : " + Global.currentScore;
		//drawTextOnCanvas(canvas, text, 0, 0)
		Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paintText.setColor(Color.BLUE);
	    paintText.setTextSize(25);
	    paintText.setStyle(Style.FILL);
	    paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
	    
	    Rect rectText = new Rect();
	    paintText.getTextBounds(text, 0, text.length(), rectText);
		canvas.drawText(text, 0, rectText.height(), paintText);

		// draw lives
		if(Global.livesLeft != 0){
			for(int i = 0 ; i < Global.livesLeft ; i++){
				canvas.drawCircle(Global.windowWidth*3/4 + 2*i*10+20 , 14f , 10, drawPaint);
			}
		}
		if(Global.gameRunState == GameRunState.GAMERUN_RUNNING){
			canvas.drawBitmap(Global.pause_bm, Global.windowWidth/2, 0, null);
		}else{
			canvas.drawBitmap(Global.resume_bm, Global.windowWidth/2, 0, null);
		}
		canvas.drawBitmap(Global.food_bm, 0, Global.windowHeight - Global.food_bm.getHeight() , null);
		
		EntityState es_a = EntityState.ACTIVE;
		//draw big roach
		//if(Global.bigRoach.es == es_a){
			Global.bigRoach.draw(canvas);
		//}
		
		// draw lady bug
		//if(Global.ladyBug.es == es_a){
			Global.ladyBug.draw(canvas);
		//}
		
		// draw one up
		//if(Global.oneUp.es == es_a){
			Global.oneUp.draw(canvas);
		//}
		
		//Drawing roaches
		//Global.roaches.get(0).draw(canvas);
		for(Entity roach : Global.roaches){
			roach.draw(canvas);
		}
		
	}

	public void updateHome(Canvas canvas) {
		// TODO Auto-generated method stub
	}	
	private void updateReady(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	private void updateRun(Canvas canvas) {
		if(Global.gameRunState == GameRunState.GAMERUN_RUNNING){
			//Global.roaches.get(0).move();
			for(Entity roach : Global.roaches){
				roach.update();
			}
			Global.bigRoach.update();
			Global.ladyBug.update();
			Global.oneUp.update();
		}
	}
	private void updatePause(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	public void drawTextOnCanvas(Canvas canvas, String text, float x, float y){
		Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paintText.setColor(Color.GREEN);
	    paintText.setTextSize(25);
	    paintText.setStyle(Style.FILL);
	    paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
	    
	    Rect rectText = new Rect();
	    paintText.getTextBounds(text, 0, text.length(), rectText);
		canvas.drawText(text, x, y, paintText);
	}
	
}
