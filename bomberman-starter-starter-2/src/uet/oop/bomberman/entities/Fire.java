package uet.oop.bomberman.entities;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Fire extends Entity{
    private int explodeTime = 23; // because explodeBomb() in Bomb runs sooner than 'this' 1 frame
    private String type;
    private boolean isLast;
    private String lastType;
    private String imgName;

    public Fire(double x, double y, Image img, String type, boolean isLast) {
        super(x, y, img);
        this.type = type;
        this.isLast = isLast;
        this.imgName = "explosion_" + type;
    }

    public Fire(double x, double y, Image img, String type, boolean isLast, String lastType) {
        super(x, y, img);
        this.type = type;
        this.isLast = isLast;
        this.lastType = lastType;
        this.imgName = "explosion_" + type;
        if(isLast) {
            this.imgName += "_" + lastType;
        }
    }

    @Override
    public void update() {
        explodeTime--;
        int k = explodeTime / 6;
        switch (k) {
            case 3:
                this.img = Sprite.sprite(this.imgName).getFxImage();
                break;
            case 2: case 0:
                this.img = Sprite.sprite(this.imgName + "1").getFxImage();
                break;
            default:
                this.img = Sprite.sprite(this.imgName + "2").getFxImage();
                break;
        }
        if(this.explodeTime < 0) BombermanGame.damagesObjects.remove(this);
    }

    @Override
    public void destroy() {

    }
}
