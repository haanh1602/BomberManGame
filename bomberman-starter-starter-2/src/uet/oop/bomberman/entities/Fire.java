package uet.oop.bomberman.entities;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Fire extends Entity implements Initializable {
    private int explodeTime = 24;
    private String type;
    private boolean isLast;
    private String lastType;
    private String imgName;

    public Fire(double x, double y, Image img, String type, boolean isLast) {
        super(x, y, img);
        this.type = type;
        this.isLast = isLast;
    }

    public Fire(double x, double y, Image img, String type, boolean isLast, String lastType) {
        super(x, y, img);
        this.type = type;
        this.isLast = isLast;
        this.lastType = lastType;
    }

    @Override
    public void update() {
        explodeTime--;
        int k = explodeTime / 6;
        switch (k) {
            case 3:
                this.img = Sprite.bomb.getFxImage();
                break;
            case 2: case 0:
                this.img = Sprite.bomb_exploded1.getFxImage();
                break;
            default:
                this.img = Sprite.bomb_exploded2.getFxImage();
                break;
        }
        if(explodeTime < 0)
        BombermanGame.damagesObjects.remove(this);
    }

    @Override
    public void update(Scene scene, KeyEvent event, List<Entity> entities) {

    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.imgName = "explosion_" + type;
        if(isLast) {
            this.imgName += "_" + lastType;
        }
    }
}
