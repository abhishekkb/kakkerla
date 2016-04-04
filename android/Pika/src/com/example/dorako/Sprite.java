package com.example.dorako;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


public class Sprite {
	private static final String TAG = Sprite.class.getSimpleName();

	private Bitmap bitmap;		// the animation sequence
	private Rect frameToDraw;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;	// the height of the sprite
	
	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)
	
	public Sprite(Bitmap bitmap, int x, int y, int frameCount) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		currentFrame = 0;
		frameNr = frameCount;
		spriteWidth = bitmap.getWidth() / frameCount;
		spriteHeight = bitmap.getHeight();
		frameToDraw = new Rect(0, 0, spriteWidth, spriteHeight);
		framePeriod = 1000 / Global.FPS;
		frameTicker = 0l;
	}
	
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Rect getframeToDraw() {
		return frameToDraw;
	}
	public void setframeToDraw(Rect frameToDraw) {
		this.frameToDraw = frameToDraw;
	}
	public int getFrameNr() {
		return frameNr;
	}
	public void setFrameNr(int frameNr) {
		this.frameNr = frameNr;
	}
	public int getCurrentFrame() {
		return currentFrame;
	}
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}
	public int getFramePeriod() {
		return framePeriod;
	}
	public void setFramePeriod(int framePeriod) {
		this.framePeriod = framePeriod;
	}
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	// the update method 
	public void update(long gameTime) {
		if (gameTime > frameTicker + framePeriod) {
			frameTicker = gameTime;
			// increment the frame
			currentFrame++;
			if (currentFrame >= frameNr) {
				currentFrame = 0;
			}
		}
		// define the rectangle to cut out sprite
		this.frameToDraw.left = currentFrame * spriteWidth;
		this.frameToDraw.right = this.frameToDraw.left + spriteWidth;
	}
	
	// the draw method which draws the corresponding frame
	public void draw(Canvas canvas) {
		// where to draw the sprite
		Rect whereToDraw = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
		canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
		//canvas.drawBitmap(bitmap, 20, 150, null);
		//Paint paint = new Paint();
		//paint.setARGB(50, 0, 255, 0);
		//canvas.drawRect(20 + (currentFrame * whereToDraw.width()), 150, 20 + (currentFrame * whereToDraw.width()) + whereToDraw.width(), 150 + whereToDraw.height(),  paint);
	}
	public void drawAt(Canvas canvas,int x, int y) {
		// where to draw the sprite
		Rect whereToDraw = new Rect(x, y, x + spriteWidth, y + spriteHeight);
		canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
		//canvas.drawBitmap(bitmap, 20, 150, null);
		//Paint paint = new Paint();
		//paint.setARGB(50, 0, 255, 0);
		//canvas.drawRect(20 + (currentFrame * whereToDraw.width()), 150, 20 + (currentFrame * whereToDraw.width()) + whereToDraw.width(), 150 + whereToDraw.height(),  paint);
	}
}