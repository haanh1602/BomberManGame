package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Bomb extends Entity {
    private int timeLeft = 120; // 1 frame takes 16ms, 60 frame = 960 ms ~ 1s, 120 frame ~ 2s
    private int explodeTime = 24;

    public Bomb(int x, int y, Image img) {
        super(x, y, img);
    }

    private void bombAnimation() {
        int k = Math.abs(this.timeLeft / 12 % 4 - 1); // 10 images , 12 * 16 = 192ms per image
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
        if(explodeTime < 0)
        BombermanGame.entities.remove(this);
        BombermanGame.damagesObjects.remove(this);
    }

    @Override
    public void update() {
        timeLeft--;
        bombAnimation();
        if(timeLeft <= 0) {
            if(timeLeft == 0) {
                BombermanGame.damagesObjects.add(
                        this);
            }
            bombExploded();
        }
    }

    @Override
    public void update(Scene scene, KeyEvent event, List<Entity> entities) {

    }

    @Override
    public boolean isAlive() {
        return (timeLeft > 0);
    }
}
