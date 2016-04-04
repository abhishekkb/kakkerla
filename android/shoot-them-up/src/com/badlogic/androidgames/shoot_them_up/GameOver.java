package com.badlogic.androidgames.shoot_them_up;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class GameOver extends Screen {
    public GameOver(Game game) {
        super(game);               
    }   

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	
            	//enable or disable sound
                if(inBounds(event, 319-80, 479-80, 80, 80) ) {
                    if(Assets.soundEnable)
                    	Assets.soundEnable = false;
                    else
                    	Assets.soundEnable = true;
                }
                //set score to zero when returned to a new game
                if(inBounds(event, 10, 120, 63, 36) ) {
                    game.setScreen(new StartScreen(game));
                    GameStats.score = 0;
                    return;
                }
              //set score to zero when returned to home/start screen
                if(inBounds(event, 320-63-10, 120, 63, 36) ) {
                    game.setScreen(new GameScreen(game));
                    GameStats.score = 0;
                    return;
                }
            }
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
        
        // background, image stored in Assets.startScreen
        g.drawPixmap(Assets.startScreen, 0, 0);

        // display scores
        g.drawPixmap(Assets.score, 50, 260);
        drawText(g, GameStats.score+"" , 50+130,260);

        // display text(image) depending on GameStats.win flag, 
        // which will be set to true when numBullets>=1 and targetsLeft==0
        if(GameStats.win)
            g.drawPixmap(Assets.youWin, 20, 20);
        else
        	g.drawPixmap(Assets.gameOver, 2, 20);
        
        g.drawPixmap(Assets.goHome, 10, 120);// go to home screen
        g.drawPixmap(Assets.restart, 320-10-63, 120);// restart game
        
        // for displaying on/off button for sound
        if(Assets.soundEnable)
            g.drawPixmap(Assets.soundOn, 319-80, 479-80);
        else
            g.drawPixmap(Assets.soundOff, 319-80, 479-80);

    }

    public void pause() {        
        //Settings.save(game.getFileIO());
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