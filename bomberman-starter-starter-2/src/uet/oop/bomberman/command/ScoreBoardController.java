package uet.oop.bomberman.command;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;

public class ScoreBoardController {

    @FXML
    AnchorPane scoreBoard;
    @FXML
    Label score, flame, bombs, speed, flamePassTime, invincibilityTime, level;
    @FXML
    ImageView flamePass, invincibility;
    @FXML
    HBox life;

    void life_update() {
        life.getChildren().clear();
        for(int i = 0; i < BombermanGame.life; i++) {
            Image heart = new Image("sprites/heart.png", 32, 32, false, false);
            life.getChildren().add(new ImageView(heart));
        }
    }

    public void update() {
        this.score.setText(String.valueOf(BombermanGame.score));
        this.flame.setText(String.valueOf(BombermanGame.flame));
        this.bombs.setText(String.valueOf(BombermanGame.bombs));
        this.speed.setText(String.valueOf(BombermanGame.speed));
        if(BombermanGame.getBomber().invincibility) {
            invincibility.setOpacity(1);
            int time = 1 + BombermanGame.getBomber().invincibilityTime / (1000/16);
            if(time == 11) time = 10;
            invincibilityTime.setText(String.valueOf(time));
            invincibilityTime.setOpacity(1);
        } else {
            invincibility.setOpacity(0.3);
            invincibilityTime.setOpacity(0);
        }
        if(BombermanGame.getBomber().flamePass) {
            flamePass.setOpacity(1);
            int time = 1 + BombermanGame.getBomber().flamePassTime / (1000/16);
            if(time == 11) time = 10;
            flamePassTime.setText(String.valueOf(time));
            flamePassTime.setOpacity(1);
        } else {
            flamePass.setOpacity(0.3);
            flamePassTime.setOpacity(0);
        }
        level.setText("Level " + BombermanGame.level);
        life_update();
    }
}
