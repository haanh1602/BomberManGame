package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import javax.swing.*;

public class Item extends Entity {
    public Item(double x, double y, Image img) {
        super(x, y, img);
    }
    private boolean flame = false, bombs = false, speed = false;

    public Item Flame() {
        Item flame = new Item(x, y, Sprite.powerup_flames.getFxImage());
        flame.flame = true;
        return flame;
    }

    public Item Bombs() {
        Item bombs = new Item(x, y, Sprite.powerup_bombs.getFxImage());
        bombs.bombs = true;
        return bombs;
    }

    public Item Speed() {
        Item speed = new Item(x, y, Sprite.powerup_speed.getFxImage());
        speed.speed = true;
        return speed;
    }

    @Override
    public void update() {
        if(Math.round(BombermanGame.entities.get(BombermanGame.entities.size() -1 ).x) == Math.round(x)
                && Math.round(BombermanGame.entities.get(BombermanGame.entities.size() -1 ).y) == Math.round(y)) {
            if(bombs) Bomber.bombs += 1;
            else if(flame) Bomber.flame += 1;
            else if(speed) Bomber.speed += 1;
            destroy();
        }
    }

    @Override
    public void destroy() {
        BombermanGame.stillObjects.remove(this);
    }
}
