package com.kakkerla.hm5_kakkerla;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

public class Entity {
	EntityType et;
	int from_x, to_x;			// the X coordinate
	int from_y, to_y;			// the Y coordinate
	SoundPool sp;
	int MAX_HITS;
	int HITS = 0;
	int speed;
	double angle;
	EntityState es;
	int respawn  = 0; // respawn after this many game loops
	int respawn2 = 0;
	
	Bitmap bitmap1, bitmap2, bitmap3;	// the actual bitmap
	boolean bitmap1drawn = false;
	boolean touched;	// if Entity is touched
	int spriteNumber = 0;
	int deadTime = 0;
	int deadTime2 = 0;
	int id;
	
	Entity(EntityType et, Context context, int speed){
		id = Global.idGen++;
		this.et = et;
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		switch(et){
		case ROACH:
			this.bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_small_1s);
			this.bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_small_2s);
			this.bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_small_dead_1s);
			sp.load(context, R.raw.squish_roach, 1);
			MAX_HITS = 1;
			es = EntityState.ACTIVE;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 10; 
			break;
		case BIG_ROACH:
			this.bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_big_1);
			this.bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_big_2);
			this.bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.roach_big_dead);
			sp.load(context, R.raw.squish_bigroach, 1);
			es = EntityState.RESPAWN;
			MAX_HITS = 10;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 20;
			break;
		case LADY_BUG:
			this.bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ladybug1_1);
			this.bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ladybug2_1);
			this.bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.ladybug_dead);
			sp.load(context, R.raw.squish_ladybug, 1);
			es = EntityState.RESPAWN;
			MAX_HITS = 1;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 20; //;
			break;
		case ONE_UP:
			this.bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenleaf1_1);
			this.bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenleaf1_2);
			this.bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenleaf1_3);
			sp.load(context, R.raw.one_up, 1);
			es = EntityState.RESPAWN;
			MAX_HITS = 1;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 20;//30;
			break;
		}

		this.from_x = (int) ( Math.random() * ( Global.windowWidth-bitmap1.getWidth() ) ); //+ bitmap1.getWidth());
		this.from_y = 0;
		this.speed = speed;
		
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public boolean hitIncrease(){
		if(es == EntityState.ACTIVE){
			if(HITS < MAX_HITS){
				HITS++;
			}
			if(HITS >= MAX_HITS){
				es = EntityState.INACTIVE;
				return true;
			}
		}
		return false;
	}

	public void draw(Canvas canvas) {
		if(es == EntityState.ACTIVE){
			if(bitmap1drawn){
				canvas.drawBitmap(bitmap2, from_x, from_y, null);
				bitmap1drawn = false;
			}else{
				canvas.drawBitmap(bitmap1, from_x, from_y, null);
				bitmap1drawn = true;
			}
		}else if (es == EntityState.INACTIVE){
			canvas.drawBitmap(bitmap3, from_x, from_y, null);
		}
	}
	public void update(){
		/*
		 * for every frame (every game loop in GAME_RUN state) this update method is called
		 */
		if(et == EntityType.ROACH){
			if(es == EntityState.ACTIVE){
				//from_x = (int) (from_x + (Math.random()-0.5)*8);
				from_y = from_y + speed;
			}else if(es == EntityState.INACTIVE){
				deadTime2--;
				if(deadTime2 <=0){
					es = EntityState.ACTIVE;
					deadTime2 = deadTime;
					resetEntity();
				}
			}
		}else{
			if(es == EntityState.ACTIVE){
				//from_x = (int) (from_x + (Math.random()-0.5)*8);
				from_y = from_y + speed;
			}else if(es == EntityState.INACTIVE){
				deadTime2--;
				if(deadTime2 <=0){
					es = EntityState.RESPAWN;
					deadTime2 = deadTime;
					from_x = (int) ( Math.random() * ( Global.windowWidth-bitmap1.getWidth() ) );
					from_y = 0;
					HITS = 0;
				}
			}else if(es == EntityState.RESPAWN){
				respawn2--;
				if(respawn2 <=0){
					es = EntityState.ACTIVE;
					respawn2 = respawn;
					from_x = (int) ( Math.random() * ( Global.windowWidth-bitmap1.getWidth() ) );
					from_y = 0;
					HITS = 0;
				}
			}
		}
		// one ups should appear only when there are less lives available
		if(et == EntityType.ONE_UP){
			// if oneUp is in active state
			if(es == EntityState.ACTIVE){
				//if total number of lives < 3
				if(Global.livesLeft == 3){
					// go to respawn/rebirth state
					es = EntityState.RESPAWN;
					resetEntity();
				}
				
			}
			
		}
		
		int head_y = from_y+bitmap1.getHeight();
		if(head_y >= Global.windowHeight - Global.food_bm.getHeight()){
			if(et != EntityType.ONE_UP || et != EntityType.LADY_BUG ){
				Global.livesLeft --;
				
			}
			if(Global.livesLeft <= 0){
				Global.gameState = GameState.GAME_OVER;
			}
			resetEntity();
		}
	}

	public boolean inBounds(MotionEvent event) {
    	float eX = event.getX();
    	float eY = event.getY();
    	int width  = bitmap1.getWidth();
    	int height = bitmap1.getHeight();
        if(eX > from_x && eX < from_x + width - 1 && eY > from_y && eY < from_y + height - 1){
            return true;
		}else{
            return false;
		}
    }

	public void resetEntity() {
		HITS = 0;
		switch(et){
		case ROACH:
			es = EntityState.ACTIVE;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 10; //(int) (Math.random()*2*Global.FPS + Global.FPS); // 1s to 3 s
			break;
		case BIG_ROACH:
			es = EntityState.RESPAWN;
			MAX_HITS = 4;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 20;//(int) (Math.random()*2*Global.FPS + 4*Global.FPS); // 4s to 6 s
			break;
		case LADY_BUG:
			es = EntityState.RESPAWN;
			MAX_HITS = 1;
			deadTime = deadTime2 = 10;
			respawn2 = respawn = 20; //(int) (Math.random()*2*Global.FPS + 8*Global.FPS); // 8s to 10 s
			break;
		case ONE_UP:
			es = EntityState.RESPAWN;
			MAX_HITS = 1;
			deadTime = deadTime2 = 4;
			respawn2 = respawn = 20; //(int) (Math.random()*2*Global.FPS + 14*Global.FPS); // 14s to 16 s
			break;
		}
		from_x = (int) ( Math.random() * ( Global.windowWidth-bitmap1.getWidth() ) ); //+ bitmap1.getWidth());
		from_y = 0;
		
	}
	
}
