package uet.oop.bomberman.entities.stillEntities.mortal.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;

public class FlameItem extends Item {
    public FlameItem(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void destroy() {
        if(!(BombermanGame.flame == BombermanGame.MAX_FLAME)) BombermanGame.flame += 1;
        sound.getItem.play();
        BombermanGame.stillObjects.remove(this);
    }
}
