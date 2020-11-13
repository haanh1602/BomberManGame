package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Ballom extends Enemy {
    public static Random random = new Random();
    public int direct;
    private int animMoveCount = 0;
    public static int point = 100;
    public Ballom(double x, double y, Image img) {
        super(x, y, img);
        direct = Math.abs(random.nextInt());
    }
    public Ballom(double x, double y, Image img, int speed) {
        super(x, y, img);
        direct = Math.abs(random.nextInt());
    }

    public void resetDirect() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    @Override
    public void update() {
        if (x > 1 && left) {
            if (Bomber.checkInstanceStill_Bomber(this, "left")) {

                x -= 0.025;
                if (x < 1) x = 1;
            } else {
                direct = direct % 3 + 1;
                switch (direct) {
                    case 1:
                        resetDirect();
                        right = true;
                    case 2:
                        resetDirect();
                        up = true;
                    case 3:
                        resetDirect();
                        down = true;
                }
                direct = Math.abs(random.nextInt());
            }
            setBallomImg("balloom_left");
        }
        if (x < BombermanGame.WIDTH - 2 && right) {
            if (Bomber.checkInstanceStill_Bomber(this, "right")) {
                x += 0.025;
                if (x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
            } else {
                direct = direct % 3 + 1;
                switch (direct) {
                    case 1:
                        resetDirect();
                        left = true;
                    case 2:
                        resetDirect();
                        up = true;
                    case 3:
                        resetDirect();
                        down = true;
                }
                direct = Math.abs(random.nextInt());
            }
            setBallomImg("balloom_right");
        }
        if (y > 1 && up) {
            if (Bomber.checkInstanceStill_Bomber(this, "up")) {
                y -= 0.025;
                if (y < 1) y = 1;
            } else {
                direct = direct % 3 + 1;
                switch (direct) {
                    case 1:
                        resetDirect();
                        right = true;
                    case 2:
                        resetDirect();
                        left = true;
                    case 3:
                        resetDirect();
                        down = true;
                }
                direct = Math.abs(random.nextInt());
            }
            setBallomImg("balloom_right");
        }
        if (y < BombermanGame.HEIGHT - 2 && down) {
            if (Bomber.checkInstanceStill_Bomber(this, "down")) {
                y += 0.025;
                if (y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
            } else {
                direct = direct % 3 + 1;
                switch (direct) {
                    case 1:
                        resetDirect();
                        up = true;
                    case 2:
                        resetDirect();
                        left = true;
                    case 3:
                        resetDirect();
                        right = true;
                }
                direct = Math.abs(random.nextInt());
            }
            setBallomImg("balloom_left");
        }
        System.out.println(direct % 3 + 1);
    }

    @Override
    public void destroy() {
        BombermanGame.entities.remove(this);
    }

    public void setBallomImg(String type) {
        int frames = (20 - speed > 0)? (20 - speed) : 1;
        if(animMoveCount == 3 * frames) animMoveCount = 0;
        int k = animMoveCount++ / frames;
        try {
            switch (k % 3) {
                case 0:
                    this.img = Sprite.sprite(type + "1").getFxImage();
                    return;
                case 1:
                    this.img = Sprite.sprite(type + "2").getFxImage();
                    return;
                case 2:
                    this.img = Sprite.sprite(type + "3").getFxImage();
                    return;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
