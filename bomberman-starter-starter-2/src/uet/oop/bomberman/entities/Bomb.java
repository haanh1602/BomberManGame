package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
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
        for (Entity entity : entityList) {
            if ((int) Math.round(object.x) == (int) Math.round(entity.x)
                    && (int) Math.round(object.y) == (int) Math.round(entity.y)) {
                if (entity instanceof Wall) return true;
                if (entity instanceof Brick) {
                    entity.destroy();
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
                /*if(object instanceof Fire) {
                    if(BombermanGame.entities.get(i) instanceof Bomber) {
                        BombermanGame.entities.get(i).destroy();
                    }
                    if(BombermanGame.entities.get(i) instanceof Bomb) {
                        // bomb chain
                        BombermanGame.entities.get(i).destroy();
                    }
                    return true;
                }*/
                if(BombermanGame.entities.get(i) instanceof Bomb) {
                    // duplicate bomb
                    if(object instanceof Fire) {
                        BombermanGame.entities.get(i).destroy();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void makeFireCustom(String type, String direction) {
        int kx = 1, ky = 1;
        if(type.equals("horizontal")) {
            ky = 0;
            if(direction.equals("right")) kx = -1;
        } else {
            kx = 0;
            if(direction.equals("down")) ky = -1;
        }
        for(int i = 1; i <= BombermanGame.flame; i++) {
            Fire fire;
            if(i == BombermanGame.flame) {
                fire = new Fire((int) Math.round(this.x) - i * kx, (int) Math.round(this.y) - i * ky, Sprite.grass.getFxImage(), type, true, direction + "_last");
            } else {
                fire = new Fire((int) Math.round(this.x) - i * kx, (int) Math.round(this.y) - i * ky, Sprite.grass.getFxImage(), type, false);
            }
            if(!checkInstanceStill(fire)) {
                checkInstanceBomb(fire);
                BombermanGame.damagesObjects.add(fire);
            } else {
                return;
            }
        }
    }

    public void makeFire() {
        Fire center = new Fire((int) Math.round(this.x), (int) Math.round(this.y), Sprite.grass.getFxImage());
        BombermanGame.damagesObjects.add(center);
        checkInstanceBomb(center);
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
        if(explodeTime < -1) {
            BombermanGame.damagesObjects.remove(this);
        }
    }

    @Override
    public void update() {
        timeLeft--;
        bombAnimation();
        if(timeLeft <= 0) {
            if(timeLeft == 0) {
                BombermanGame.entities.remove(this);
                Bomber.currentBombs--;
                makeFire();
                BombermanGame.damagesObjects.add(this);
            }
            bombExploded();
        }
    }

    @Override
    public void destroy() {
        this.timeLeft = 5;
    }
}
