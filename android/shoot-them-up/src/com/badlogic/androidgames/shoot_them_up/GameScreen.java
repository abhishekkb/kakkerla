package com.badlogic.androidgames.shoot_them_up;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class GameScreen extends Screen {
	
	
	// no. of bullets
	int MAX  = 10;
	int numBullets;
	int targetsLeft;
	boolean[] dead = new boolean [MAX];
	int[][] pos = new int [MAX][2];
	int[] direction = new int [MAX]; // will be 1 if moving right else -1
	int[] sine = new int [MAX];
	
    public GameScreen(Game game) {
        super(game);
    	numBullets = MAX;
    	targetsLeft = MAX;
    	
    	//setting initial positions for targets
    	// half of the total targets are set to move in one direction (to right)
    	for(int i=0; i<MAX/2;i++){
    		dead[i]=false;
    		direction[i] = 1; // 1 for right and -1 for left diretion movement

    		pos[i][0] = i*20+20;
			pos[i][1] = 80+ i*40;
    	}
    	// rest of total targets are set to move in another direction (to left)
    	for(int i=0; i<MAX/2;i++){
    		dead[MAX/2 + i]=false;
    		direction[MAX/2 + i] = -1; // 1 for right and -1 for left diretion movement

    		pos[MAX/2 + i][0] = i*20+30;
			pos[MAX/2 + i][1] = 80+i*40;
    	}
    	
    }   

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	//touch-up event
                if(inBounds(event, 319-80, 479-80, 80, 80) ) {
                    if(Assets.soundEnable)
                    	Assets.soundEnable = false;
                    else
                    	Assets.soundEnable = true;
                    continue;
                }
                
            	if(numBullets == 0 && targetsLeft >=1){
            		//if no. of bullets is zero and targets are 
            		//still left then its game over
            		GameStats.win = false;
                	game.setScreen(new GameOver(game));
                }else if(targetsLeft == 0 && numBullets >=0){
                	//if targets are all dead and no. of bullets
                	//are either zero or some of them left
                	//then the game is won
            		GameStats.win = true;
                	game.setScreen(new GameOver(game));
                }

            	numBullets--;
            	if(Assets.soundEnable)
            		Assets.shoot.play(1);
            	
            	//loop for making targets dead when touched on them
            	//targets may die with single bullet when they are
            	//in bounds of the touched area
            	for(int j=0; j<MAX; j++){
            		if( !dead[j] ){
	            		if( inBounds(event, pos[j][0], pos[j][1] , 43, 43)) {
	            				++GameStats.score;
	            				--targetsLeft;
	            				dead[j] = true;;
	        			}
            		}
            	}// end inner for
            }// end outer for
        }
        
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }

    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.background, 0, 0);
        
        g.drawPixmap(Assets.score, 0, 0);
        drawText(g, GameStats.score+"" , 130,0);

        //display sound button
        if(Assets.soundEnable)
            g.drawPixmap(Assets.soundOn, 319-80, 479-80);
        else
            g.drawPixmap(Assets.soundOff, 319-80, 479-80);
        
        // draw targets
        for(int i = 0; i<MAX;i++){
        	if(dead[i]){ 
        		//if target is dead
        		g.drawPixmap(Assets.targetDead, pos[i][0], pos[i][1]);
        	}else{
        		// if target is not already shot to dead
        		setXPosition(i);
        		g.drawPixmap(Assets.target, pos[i][0], pos[i][1]);
        	}
        }
        // draw bullets
        for(int i = 0; i<numBullets; i++){
        	g.drawPixmap(Assets.bullet, 14*i, 400);
        }

    }

    public void pause() {        
        //Settings.save(game.getFileIO());
    }
    
    // setting the position of each target
    public void setXPosition(int i){
    	if(pos[i][0]==319-43 && direction[i]== 1){
    		direction[i] = -1; //if the target reaches right corner then reverse direction 
    	}else if(pos[i][0]==0 && direction[i]== -1){
    		direction[i] = 1; //if the target reaches left corner then reverse direction
    	}
    	pos[i][0] = pos[i][0] + direction[i];
    	
    	//y = amplitude * Math.sin(frequency * x)
    	pos[i][1] = pos[i][1] + (int) (10 * Math.sin(0.5 * pos[i][0]));
    	
    }
    public void resume() {
    }

    public void dispose() {

    }

    public void drawText(Graphics g, String s, int x, int y) {
    	// code to print numbers
        int len = s.length();
        int srcX = 0;
        int srcY = 0;
        int srcWidth = 23;
        int srcHeight = 54;
        String c;
        for (int i =0; i<len ; i++){
    			for(int j=0; j<=9; j++){
    				c = ""+j;
    				if(c.charAt(0) == s.charAt(i)){
    		        	srcX = j * 23;
    		            g.drawPixmap(Assets.numbers, x, y, srcX, srcY, srcWidth, srcHeight);    
    				}
    			}
				x += 23;
        	}
    }
}