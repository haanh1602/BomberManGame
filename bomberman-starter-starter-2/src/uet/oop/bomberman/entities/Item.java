package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Item extends Entity {
    public Item(double x, double y, Image img) {
        super(x, y, img);
    }
    private boolean flame = false, bombs = false, speed = false, flamePass = false;

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

    public Item FlamePass() {
        Item flamePass = new Item(x, y, Sprite.powerup_flamepass.getFxImage());
        flamePass.flamePass = true;
        return flamePass;
    }

    @Override
    public void update() {
        if(Math.round(BombermanGame.entities.get(BombermanGame.entities.size() -1 ).x) == Math.round(x)
                && Math.round(BombermanGame.entities.get(BombermanGame.entities.size() -1 ).y) == Math.round(y)) {
            if(bombs) {
                if(!(BombermanGame.bombs == BombermanGame.MAX_BOMBS)) BombermanGame.bombs += 1;
            }
            else if(flame) {
                if(!(BombermanGame.flame == BombermanGame.MAX_FLAME)) BombermanGame.flame += 1;
            }
            else if(speed) {
                if(!(BombermanGame.speed == BombermanGame.MAX_SPEED)) BombermanGame.speed += 1;
            }
            else if(flamePass) {
                try {
                    if(BombermanGame.getBomber().flamePass) {
                        BombermanGame.getBomber().flamePassRestart();
                    } else {
                        BombermanGame.getBomber().flamePass = true;
                        BombermanGame.effects.add(
                                new Effect(BombermanGame.getBomber().x, BombermanGame.getBomber().y, null, "FlamePass"));
                    }
                } catch (NullPointerException nullPointerException) {
                    System.out.println("Bomber cannot found!");
                }
            }
            destroy();
        }
    }

    @Override
    public void destroy() {
        BombermanGame.stillObjects.remove(this);
    }
}

// test
