package com.almasb.battleship;

import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;



public class Ship extends Parent {
    public int type;
    public boolean vertical = true;
    private static MediaPlayer mediaPlayer;
    private static final String EXPLO_SOUND = "sound/ExplosionSound.mp3";

    private int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;

        /*VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }

        getChildren().add(vbox);*/
    }

    public void hit() {
        playHitMusic(EXPLO_SOUND);
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }
    public void playHitMusic(String sound_path) {
        //MUST Include javafx.graphics,javafx.media to --add-modules to play media
        Media menuMusic = new Media(new File(sound_path).toURI().toString());
        mediaPlayer = new MediaPlayer(menuMusic);
//        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); //loop
        mediaPlayer.play();
    }
}