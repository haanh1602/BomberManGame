package uet.oop.bomberman.entities.moveEntities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.moveEntities.Bomber;

import java.util.Random;

public class Oneal extends Enemy {
    // TODO: đuổi theo bomber ( phạm vi bé ), speed ngẫu nhiên
    public Oneal(double x, double y, Image img) {
        super(x, y, img);
        this.speed = 1;
        this.point = 200;
        this.type = "oneal";
    }

//    @Override
//    public void moveHandle() {
//        if (this.left) {
//            if(x > 1 && Bomber.checkInstanceStill(this, "left")) {
//                x -= 0.02 + 0.01 * (speed - 1);
//                if(x < 1) x = 1;
//                setImg(type + "_left");
//            } else {
//                setNewDirect();
//                speed = random.nextInt(3) + 1;
//            }
//        }
//        else if (this.right) {
//            if(x < BombermanGame.WIDTH - 2 && Bomber.checkInstanceStill(this, "right")) {
//                x += 0.02 + 0.01 * (speed - 1);
//                if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
//                setImg(type + "_right");
//            } else {
//                setNewDirect();
//                speed = random.nextInt(3) + 1;
//            }
//        }
//        else if (this.down) {
//            if(y < BombermanGame.HEIGHT - 2 && Bomber.checkInstanceStill(this, "down")) {
//                y += 0.02 + 0.01 * (speed - 1);
//                if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
//                setImg(type + "_right");
//            } else {
//                setNewDirect();
//                speed = random.nextInt(3) + 1;
//            }
//        }
//        else if (this.up) {
//            if(y > 1 && Bomber.checkInstanceStill(this, "up")) {
//                y -= 0.02 + 0.01 * (speed - 1);
//                if(y < 1) y = 1;
//                setImg(type + "_left");
//            } else {
//                setNewDirect();
//                speed = random.nextInt(3) + 1;
//            }
//        }
//    }

    public void moveHandle() {
        if (this.left) {
            if (x > 1 && Bomber.checkInstanceStill(this, "left")) {
                if (this.x - BombermanGame.getBomber().getX() >= 4) {
                    if (x > 1 && Bomber.checkInstanceStill(this, "left")) {
                        speed += 0.1;
                        x -= 0.02 + 0.01 * (speed - 1);
                        if(x < 1) x = 1;
                        setImg(type + "_left");
                    } else {
                        setNewDirect();
                        speed = random.nextInt(2) + 1;
                    }
                }
                x -= 0.02 + 0.01 * (speed - 1);
                if(x < 1) x = 1;
                setImg(type + "_left");
            } else {
                setNewDirect();
                speed = random.nextInt(2) + 1;
            }
        }
        else if (this.right) {
            if(x < BombermanGame.WIDTH - 2 && Bomber.checkInstanceStill(this, "right")) {
                if (this.x - BombermanGame.getBomber().getX() <= -4) {
                    if (x < BombermanGame.WIDTH - 2 && Bomber.checkInstanceStill(this, "right")) {
                        speed += 0.1;
                        x += 0.02 + 0.01 * (speed - 1);
                        if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
                        setImg(type + "_right");
                    } else {
                        setNewDirect();
                        speed = random.nextInt(2) + 1;
                    }

                }
                x += 0.02 + 0.01 * (speed - 1);
                if(x > BombermanGame.WIDTH - 2) x = BombermanGame.WIDTH - 2;
                setImg(type + "_right");
            } else {
                setNewDirect();
                speed = random.nextInt(2) + 1;
            }
        }
        else if (this.down) {
            if(y < BombermanGame.HEIGHT - 2 && Bomber.checkInstanceStill(this, "down")) {
                if (this.y - BombermanGame.getBomber().getY() <= -4) {
                    if (y < BombermanGame.HEIGHT - 2 && Bomber.checkInstanceStill(this, "down")) {
                        speed += 0.1;
                        y += 0.02 + 0.01 * (speed - 1);
                        if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
                        setImg(type + "_right");
                    } else {
                        setNewDirect();
                        speed = random.nextInt(2) + 1;
                    }
                }
                y += 0.02 + 0.01 * (speed - 1);
                if(y > BombermanGame.HEIGHT - 2) y = BombermanGame.HEIGHT - 2;
                setImg(type + "_right");
            } else {
                setNewDirect();
                speed = random.nextInt(2) + 1;
            }
        }
        else if (this.up) {
            if(y > 1 && Bomber.checkInstanceStill(this, "up")) {
                if (this.y - BombermanGame.getBomber().getY() >= 4) {
                    if (y > 1 && Bomber.checkInstanceStill(this, "up")) {
                        speed += 0.1;
                        y -= 0.02 + 0.01 * (speed - 1);
                        if(y < 1) y = 1;
                        setImg(type + "_left");
                    } else {
                        setNewDirect();
                        speed = random.nextInt(2) + 1;
                    }
                }
                y -= 0.02 + 0.01 * (speed - 1);
                if(y < 1) y = 1;
                setImg(type + "_left");
            } else {
                setNewDirect();
                speed = random.nextInt(2) + 1;
            }
        }
    }
}
