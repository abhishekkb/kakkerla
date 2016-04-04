package com.badlogic.androidgames.shoot_them_up;

import java.util.List;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class StartScreen extends Screen {
    public StartScreen(Game game) {
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
                if(inBounds(event, 32, 20, 116, 74) ) {
                	if(Assets.soundEnable)
                		Assets.shoot.play(1);
                    game.setScreen(new GameScreen(game));
                }
                if(inBounds(event, 319-80, 479-80, 80, 80) ) {
                    if(Assets.soundEnable)
                    	Assets.soundEnable = false;
                    else
                    	Assets.soundEnable = true;
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
        
        // code
        g.drawPixmap(Assets.startScreen, 0, 0);
        g.drawPixmap(Assets.playButton, 32, 20);
        
        if(Assets.soundEnable)
            g.drawPixmap(Assets.soundOn, 319-80, 479-80);
        else
            g.drawPixmap(Assets.soundOff, 319-80, 479-80);

        
        /*
        if(Settings.soundEnabled)
            g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        else
            g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
        */
    }

    public void pause() {        
        //Settings.save(game.getFileIO());
    }

    public void resume() {

    }

    public void dispose() {

    }
}

