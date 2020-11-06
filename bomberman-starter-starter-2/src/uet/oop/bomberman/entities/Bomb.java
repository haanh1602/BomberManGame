package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomb extends Entity {
    private int timeLeft = 180; // 1 frame takes 16ms, 60 frame = 960 ms ~ 1s, 120 frame ~ 2s
    private int explodeTime = 24;

    public Bomb(int x, int y, Image img) {
        super(x, y, img);
    }

    private void bombAnimation() {
        int k = Math.abs(this.timeLeft / 18 % 4 - 1); // 10 images , 12 * 16 = 192ms per image
        switch (k) {
            case 2:
                this.img = Sprite.bomb_2.getFxImage();
                break;
            case 1:
                this.img = Sprite.bomb_1.getFxImage();
                break;
            case 0:
                this.img = Sprite.bomb.getFxImage();
                break;
            default:
                break;
        }
    }

    public static boolean checkInstanceStill(Entity object) {
        List<Entity> entityList = BombermanGame.stillObjects;
        for(int i = 0; i < entityList.size(); i++) {
            if((int) Math.round(object.x) == (int) Math.round(entityList.get(i).x)
            && (int) Math.round(object.y) == (int) Math.round(entityList.get(i).y)) {
                if(entityList.get(i) instanceof Wall) return true;
                if(entityList.get(i) instanceof Brick) {
                    if(entityList.get(i) instanceof Item) return true;
                    if(!(object instanceof Bomber)) entityList.get(i).destroy();
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkInstanceBomb(Entity object) {
        for(int i = 0; i < BombermanGame.entities.size(); i++) {
            if((int) Math.round(object.x) == (int) Math.round(BombermanGame.entities.get(i).x)
                    && (int) Math.round(object.y) == (int) Math.round(BombermanGame.entities.get(i).y)) {
                if(BombermanGame.entities.get(i) instanceof Bomb) {
                    return true;
                }
                /*if(BombermanGame.entities.get(i) instanceof Bomber) {
                    return false;
                }*/
            }
        }
        return false;
    }

    public void bombChain(Fire fire) {

    }

    public void makeFireCustom(String type, String trend) {
        int kx = 1, ky = 1;
        if(type == "horizontal") {
            ky = 0;
            if(trend == "right") kx = -1;
        } else {
            kx = 0;
            if(trend == "down") ky = -1;
        }
        for(int i = 1; i <= Bomber.flame; i++) {
            Fire fire;
            if(i == Bomber.flame) {
                fire = new Fire((int) Math.round(this.x) - i * kx, (int) Math.round(this.y) - i * ky, Sprite.grass.getFxImage(), type, true, trend + "_last");
            } else {
                fire = new Fire((int) Math.round(this.x) - i * kx, (int) Math.round(this.y) - i * ky, Sprite.grass.getFxImage(), type, false);
            }
            if(!checkInstanceStill(fire)) {
                BombermanGame.damagesObjects.add(fire);
            } else {
                return;
            }
        }
    }

    public void makeFire() {
        makeFireCustom("horizontal", "left");
        makeFireCustom("horizontal", "right");
        makeFireCustom("vertical", "down");
        makeFireCustom("vertical", "top");
    }

    public void bombExploded() {
        explodeTime--;
        int k = explodeTime / 6;
        switch (k) {
            case 3:
                this.img = Sprite.bomb_exploded.getFxImage();
                break;
            case 2: case 0:
                this.img = Sprite.bomb_exploded1.getFxImage();
                break;
            default:
                this.img = Sprite.bomb_exploded2.getFxImage();
                break;
        }
        if(explodeTime < 0) {
            BombermanGame.damagesObjects.remove(this);
        }
    }

    @Override
    public void update() {
        timeLeft--;
        bombAnimation();
        if(timeLeft <= 0) {
            if(timeLeft == 0) {
                BombermanGame.damagesObjects.add(this);
                BombermanGame.entities.remove(this);
                Bomber.currentBombs--;
                makeFire();
            }
            bombExploded();
        }
    }

    @Override
    public void destroy() {
        timeLeft = 1;
    }
}
