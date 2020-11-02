package uet.oop.bomberman.entities;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Bomber extends Entity {
    public static int bombSize = 1;
    public static int speed = 1;
    public static int bombs = 1;
    public static int life = 3;

    private boolean isAlive = true;

    public Bomber(double x, double y, Image img) {
        super( x, y, img);
    }

    public void isDead() {
        this.isAlive = false;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Scene scene, KeyEvent event, List<Entity> entities) {
        switch (event.getCode()) {
            case UP:
                if(y > 1.1) this.y = y - 0.1;
                this.img = Sprite.player_up.getFxImage();
                //Utility.delay(10);
                //System.out.println("UP");
                break;
            case DOWN:
                if(y < BombermanGame.HEIGHT - 2.1) this.y = this.y + 0.1;
                this.img = Sprite.player_down.getFxImage();
                //System.out.println("DOWN");
                break;
            case LEFT:
                if(x > 1.1) this.x = this.x - 0.1;
                this.img = Sprite.player_left.getFxImage();
                //System.out.println("LEFT");
                break;
            case RIGHT:
                if(x < BombermanGame.WIDTH - 2.1) this.x = this.x + 0.1;
                this.img = Sprite.player_right.getFxImage();
                //System.out.println("RIGHT");
                break;
            case SPACE:
                int bomb_x = (int) Math.round(x);
                int bomb_y = (int) Math.round(y);
                entities.add(0, new Bomb(bomb_x, bomb_y, Sprite.bomb.getFxImage()));
                break;
            default:
                break;
        }
    }

    public void Bomd() {

    }

    /*@Override
    public void update(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if(y > 1) this.y -= 1;
                    break;
                case DOWN:
                    if(y < new BombermanGame().HEIGHT - 1) this.y += 1;
                    break;
                case LEFT:
                    if(x > 1) this.x -= 1;
                    break;
                case RIGHT:
                    if(x < new BombermanGame().WIDTH - 1) this.x += 1;
                    break;
            }
        });
    }*/

}
