package com.kakkerla.hm5_kakkerla;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

public class GameThread extends Thread{
	public int FPS = Global.FPS;
	public double averageFPS;
	public SurfaceHolder surfaceHolder;
	public GamePanel gamePanel;
	public boolean running;
	public static Canvas canvas;
	public boolean start1 = false, start2 = false;
	public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
		super();
		Log.i(Global.DEBUG_TAG,"MainThread - constructor");
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		
		
	}
	@Override
	public void run(){
		//Log.i(Global.DEBUG_TAG,"MainThread - on run");
		long startTime = 0;
		long waitTime;
		long totalTime = 0;
		long targetTime = 1000/FPS;  // = 1 millisecond / framerate
		int frameCount = 0;
		long time; 
		
		while(running){
			if(Global.gameState == GameState.GAME_READY || Global.gameState == GameState.GAME_OVER ){
				//Log.i(Global.DEBUG_TAG,"MainThread - running");
				if(start1 == false){
					startTime = System.nanoTime(); // in nanoseconds
					start1 = true;
				}
				
				canvas = null;
				
				try{
					canvas = this.surfaceHolder.lockCanvas();
					synchronized (surfaceHolder) {
						this.gamePanel.update(canvas);
						this.gamePanel.draw(canvas);
					}
				}catch(Exception e){
					//Log.i(Global.DEBUG_TAG,"Running - Error while rendering or updating");
				}finally{
					if(canvas != null){
						try{
							surfaceHolder.unlockCanvasAndPost(canvas);
						}catch(Exception e){
							//Log.i(Global.DEBUG_TAG,"Running - Error on unlocking canvas");
						}
					}
				}
				
				time = (System.nanoTime() - startTime)/1000000; // time in milliseconds
				
				if(time >= 3000){ // wait 3 seconds and go to GAME_RUN mode
					if(Global.gameState == GameState.GAME_READY){
						Global.gameState = GameState.GAME_RUN;
					}else if(Global.gameState == GameState.GAME_OVER){
						Global.gameState = GameState.GAME_HOME;
					}
					start1 = false;
				}
				continue;
			}
		
			//Log.i(Global.DEBUG_TAG,"MainThread - running");
			startTime = System.nanoTime(); // in nanoseconds
			canvas = null;
			
			try{
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					this.gamePanel.update(canvas);
					this.gamePanel.draw(canvas);
					
				}
			}catch(Exception e){
				//Log.i(Global.DEBUG_TAG,"Running - Error while rendering or updating");
			}finally{
				if(canvas != null){
					try{
						surfaceHolder.unlockCanvasAndPost(canvas);
					}catch(Exception e){
						//Log.i(Global.DEBUG_TAG,"Running - Error on unclocking canvas");
					}
				}
			}
			
			time = (System.nanoTime() - startTime)/1000000; // time in milliseconds
			waitTime = targetTime - time;
			
			try{
				this.sleep(waitTime);
			}catch(Exception e){
				//Log.i(Global.DEBUG_TAG,"Running - Error while sleeping");
			}
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if(frameCount == FPS){
				averageFPS = 1000/((totalTime/frameCount)/1000000);
				frameCount = 0;
				totalTime = 0;
				//Log.i(Global.DEBUG_TAG,"Running - average FPS = "+averageFPS);
			}
			//Log.i(Global.DEBUG_TAG,"Running - average FPS = "+averageFPS);
			//System.out.println("average FPS = "+averageFPS);
		}
		
	}
	
	public void setRunning(boolean x){
		running = x;
	}
	
}
