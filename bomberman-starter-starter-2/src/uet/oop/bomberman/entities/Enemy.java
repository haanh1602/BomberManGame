package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class Enemy extends Entity{
    public static boolean left = false;
    public static boolean right = true;
    public static boolean up = false;
    public static boolean down = false;
    protected boolean isAlive;
    public static int speed = 1;
    public Enemy(double x, double y, Image img) {
        super(x, y, img);
    }

    public Enemy(double x, double y, Image img, int speed) {
        super(x, y, img);
        this.speed = speed;
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }

}
