package uet.oop.bomberman.sound;

import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Sound {
    // Sound effect
    public MediaPlayer startSound = SoundController.makeSound("Level_Start.mp3");
    public MediaPlayer walking = SoundController.makeSound("Walk.mp3");
    public MediaPlayer makeBomb = SoundController.makeSound("Make_Bomb.mp3");
    public MediaPlayer bombExplodes = SoundController.makeSound("Bomb_Explodes.mp3");
    public MediaPlayer getItem = SoundController.makeSound("Get_Item.mp3");
    public MediaPlayer getDamage = SoundController.makeSound("Get_Damage.mp3");
    public MediaPlayer kill_Enemy = SoundController.makeSound("Kill_Enemy.mp3");
    public MediaPlayer death = SoundController.makeSound("Death.mp3");

    // Soundtrack
    public MediaPlayer findTheExit = SoundController.makeSound("Find_The_Exit.mp3");
    public MediaPlayer currentThemeSound;

    public void repeat(MediaPlayer mediaPlayer) {
        SoundController.repeatMedia(mediaPlayer);
    }
    public void replay(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        mediaPlayer.play();
    }

    public Sound() {
        makeBomb.setVolume(4);
        bombExplodes.setVolume(4);
        findTheExit.setVolume(0.7);
    }

    public void stopAll() {
        SoundController.stopAllSounds(this);
    }
}
