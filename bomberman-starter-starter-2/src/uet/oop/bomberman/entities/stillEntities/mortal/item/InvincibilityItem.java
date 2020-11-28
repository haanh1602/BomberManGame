package uet.oop.bomberman.entities.stillEntities.mortal.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

public class InvincibilityItem extends Item{

    public InvincibilityItem(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void destroy() {
        BombermanGame.getBomber().invincibility = true;
        BombermanGame.getBomber().invincibilityTime = 3000/16;
        BombermanGame.stillObjects.remove(this);
    }
}
