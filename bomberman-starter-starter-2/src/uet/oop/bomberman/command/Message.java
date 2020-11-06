package uet.oop.bomberman.command;

import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;

public class Message {
    public static Pane pause() {
        Pane res = new Pane();
        res.setPrefSize(BombermanGame.WIDTH * Sprite.SCALED_SIZE, BombermanGame.HEIGHT * Sprite.SCALED_SIZE);
        res.setOpacity(0.5);
        //res.getChildren().add(new Image("/sprites/"));
        res.setStyle("-fx-background-color: black");
        Label pause = new Label("PAUSE");
        pause.setPrefSize(160, 80);
        //pause.setStyle("-fx-background-color: whitesmoke");
        pause.setTextFill(Paint.valueOf("WHITE"));
        //pause.setFont();
        pause.relocate((res.getPrefWidth() - pause.getPrefWidth()) / 2,
                (res.getPrefHeight() - pause.getPrefHeight()) / 2);
        res.getChildren().add(pause);
        //res.relocate((BombermanGame.WIDTH * Sprite.SCALED_SIZE - res.getPrefWidth()) / 2,(BombermanGame.HEIGHT * Sprite.SCALED_SIZE - res.getPrefHeight()) / 2);
        res.setVisible(false);
        return res;
    }
}
