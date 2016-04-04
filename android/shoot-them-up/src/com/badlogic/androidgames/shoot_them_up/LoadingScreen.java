package com.badlogic.androidgames.shoot_them_up;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.background = g.newPixmap("background2.png", PixmapFormat.RGB565);
        Assets.bullet = g.newPixmap("bullet-tr.png", PixmapFormat.ARGB4444);
        Assets.target = g.newPixmap("target2-small-tr.png", PixmapFormat.ARGB4444);
        Assets.targetDead = g.newPixmap("dead-target-tr.png", PixmapFormat.ARGB4444);
        Assets.startScreen = g.newPixmap("start-screen.png", PixmapFormat.RGB565);
        Assets.playButton = g.newPixmap("play-colorinvert-transparent.png", PixmapFormat.RGB565);
        Assets.restart = g.newPixmap("restart2-tr.png", PixmapFormat.RGB565);
        Assets.goHome = g.newPixmap("go-home-tr.png", PixmapFormat.RGB565);
        Assets.gameOver = g.newPixmap("game-over.png", PixmapFormat.ARGB4444);
        Assets.numbers = g.newPixmap("numbers-white.png", PixmapFormat.ARGB4444);
        Assets.score = g.newPixmap("score-w-tr.png", PixmapFormat.ARGB4444);
        Assets.scoreBlack = g.newPixmap("score.png", PixmapFormat.ARGB4444);
        Assets.youWin = g.newPixmap("you-win-w-tr.png", PixmapFormat.ARGB4444);

        Assets.shoot = game.getAudio().newSound("gun-shot.mp3");
        Assets.soundOn = g.newPixmap("sound2-on-tr.png", PixmapFormat.ARGB4444);
        Assets.soundOff = g.newPixmap("sound2-off-tr.png", PixmapFormat.ARGB4444);


        /*
        Assets.click = game.getAudio().newSound("click.ogg");
        Assets.eat = game.getAudio().newSound("eat.ogg");
        Assets.bitten = game.getAudio().newSound("bitten.ogg");
        */
        //Settings.load(game.getFileIO());
        game.setScreen(new StartScreen(game));
    }
    
    public void present(float deltaTime) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {

    }
}
