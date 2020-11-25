package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

import java.util.Random;

public class Ballom extends Enemy {
    public static Random random = new Random();

    public Ballom(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.point = 100;
        this.type = "balloom";
    }

    private void balloom_setNewDirect() {

    }

//    public void moveHandle() {
//        setNewDirect();
//        if (this.left) {
//            if(x > 1 && Bomber.checkInstanceStill(this, "left")) {
//                x -= 0.02 + 0.02 * (speed - 1);
//                if(x < 1) x = 1;
//                setImg("balloom_left");
//            }
//        }
//        else if (this.right) {
//            if(x < BombermanGame.WIDTH - 2 && Bomber.checkInstanceStill(this, "right")) {
//                x += 0.02 + 0.02 * (speed - 1);
//                if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
//                setImg("balloom_right");
//            }
//        }
//        else if (this.down) {
//            if(y < BombermanGame.HEIGHT - 2 && Bomber.checkInstanceStill(this, "down")) {
//                y += 0.02 + 0.02 * (speed - 1);
//                if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
//                setImg("balloom_right");
//            }
//        }
//        else if (up) {
//            if(y > 1 && Bomber.checkInstanceStill(this, "up")) {
//                y -= 0.02 + 0.02 * (speed - 1);
//                if(y < 1) y = 1;
//                setImg("balloom_left");
//            }
//        }
//    }

    @Override
    public void update() {
        if(dying) deadHandle();
        else {
            moveHandle();
            Bomber.checkInstanceDamages(this);
        }
    }
}
